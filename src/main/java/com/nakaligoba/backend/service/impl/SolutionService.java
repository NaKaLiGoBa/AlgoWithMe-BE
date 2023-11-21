package com.nakaligoba.backend.service.impl;

import com.github.dockerjava.api.exception.UnauthorizedException;
import com.nakaligoba.backend.controller.payload.request.SolutionRequest;
import com.nakaligoba.backend.controller.payload.response.SolutionResponse;
import com.nakaligoba.backend.controller.payload.response.SolutionsResponse;
import com.nakaligoba.backend.controller.payload.response.SolutionsResponse.Link;
import com.nakaligoba.backend.controller.payload.response.SolutionsResponse.Solutions;
import com.nakaligoba.backend.controller.payload.response.SolutionsResponse.SolutionsData;
import com.nakaligoba.backend.domain.*;
import com.nakaligoba.backend.repository.*;
import com.nakaligoba.backend.service.dto.AuthorDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SolutionService {

    private final MemberService memberService;
    private final ProblemService problemService;
    private final SolutionViewService solutionViewService;

    private final SolutionRepository solutionRepository;
    private final CommentRepository commentRepository;
    private final ProgrammingLanguageRepository programmingLanguageRepository;
    private final SolutionLanguageRepository solutionLanguageRepository;
    private final SolutionLikeRepository solutionLikeRepository;
    private final AvailableLanguageRepository availableLanguageRepository;

    @Transactional(readOnly = true)
    public Solution getSolution(Long id) {
        return solutionRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public Long createSolution(String loggedInEmail, Long problemId, SolutionRequest request) {
        Member member = memberService.getMemberByEmail(loggedInEmail);
        Problem problem = problemService.getProblem(problemId);
        Solution solution = Solution.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .member(member)
                .problem(problem)
                .build();

        Solution savedSolution = solutionRepository.save(solution);
        log.info("Save new Solution. Id: {}", savedSolution.getId());

        ArrayList<SolutionLanguage> solutionLanguages = new ArrayList<>();

        for (String languageName : request.getLanguages()) {
            Language language = Language.findByName(languageName)
                            .orElseThrow(NoSuchElementException::new);
            ProgrammingLanguage programmingLanguage = programmingLanguageRepository.findByName(language)
                    .orElseThrow(EntityNotFoundException::new);
            log.info("ProgrammingLanguage Id: {}", programmingLanguage.getId());
            List<AvailableLanguage> availableLanguages = problem.getAvailableLanguages();

            for (AvailableLanguage al : availableLanguages) {
                ProgrammingLanguage pl = al.getProgrammingLanguage();
                if (pl.equals(programmingLanguage)) {
                    SolutionLanguage solutionLanguage = SolutionLanguage.builder()
                            .solution(savedSolution)
                            .availableLanguage(al)
                            .build();

                    solutionLanguages.add(solutionLanguage);
                    break;
                }
            }
        }

        solutionLanguageRepository.saveAll(solutionLanguages);

        return solution.getId();
    }

    @Transactional
    public Long updateSolution(String writerEmail, Long problemId, Long solutionId, SolutionRequest request) {
        Member member = memberService.getMemberByEmail(writerEmail);
        Problem problem = problemService.getProblem(problemId);
        Solution solution = getSolution(solutionId);

        solution.changeTitle(request.getTitle());
        solution.changeContent(request.getContent());

        List<AvailableLanguage> availableLanguages = new ArrayList<>();
        List<ProgrammingLanguage> programmingLanguages = request.getLanguages().stream()
                .map(language -> programmingLanguageRepository.findByName(Language.findByName(language).orElseThrow())
                        .orElseThrow(EntityNotFoundException::new))
                .collect(Collectors.toList());

        for (ProgrammingLanguage programmingLanguage : programmingLanguages) {
            availableLanguageRepository.findByProgrammingLanguage(programmingLanguage)
                    .ifPresent(availableLanguages::add);
        }

        solution.updateSolutionLanguages(availableLanguages);
        solutionRepository.save(solution);

        return solution.getId();
    }

    @Transactional
    public void removeSolution(String writerEmail, Long problemId, Long solutionId) {
        Member member = memberService.getMemberByEmail(writerEmail);
        Problem problem = problemService.getProblem(problemId);
        Solution solution = getSolution(solutionId);

        if (!solution.getMember().equals(member)) {
            throw new UnauthorizedException("권한이 없습니다.");
        }

        solutionViewService.deleteBySolution(solution);
        solutionRepository.delete(solution);
    }

    @Transactional(readOnly = true)
    public SolutionsResponse readSolutions(Long problemId, Long nextCursorId, Integer size, String sort) {
        Long solutionTotalCount = solutionRepository.countByProblemId(problemId);
        List<Solution> solutions = getSolutions(problemId, nextCursorId, size, sort);
        List<Solutions> responseSolutions = getResponseSolutions(solutions);
        Long nextCursor = getNextCursorId(responseSolutions, sort);

        return SolutionsResponse.builder()
                .totalCount(solutionTotalCount)
                .solutions(responseSolutions)
                ._link(Link.builder()
                        .nextCursor(nextCursor)
                        .build())
                .build();
    }

    private List<Solution> getSolutions(Long problemId, Long nextCursorId, Integer size, String sort) {
        Pageable pageable = PageRequest.of(0, size);
        String defaultSort = SolutionSort.RECENT.getName();

        // todo : 정렬 기준 추가 시 이에 따른 다른 쿼리문 참조 필요
        if (nextCursorId == SolutionsResponse.READ_SOLUTIONS_INIT) {
            return solutionRepository.findByProblemIdOrderByCreatedAtDesc(problemId, pageable);
        }

        if (nextCursorId > SolutionsResponse.READ_SOLUTIONS_EMPTY) {
            return solutionRepository.findByProblemIdAndIdLessThanOrderByCreatedAtDesc(problemId, nextCursorId, pageable);
        }

        return Collections.emptyList();
    }

    private List<Solutions> getResponseSolutions(List<Solution> solutions) {
        return solutions.stream().map(solution ->
                        Solutions.builder()
                                .author(AuthorDto.builder()
                                        .avatar("")
                                        .nickname(solution.getMember().getNickname())
                                        .build())
                                .solution(SolutionsData.builder()
                                        .id(solution.getId())
                                        .title(solution.getTitle())
                                        .likeCount(getLikeCount(solution.getId()))
                                        .viewCount(getViewCount(solution.getId()))
                                        .commentCount(getCommentCount(solution.getId()))
                                        .build())
                                .build())
                .collect(Collectors.toList());
    }

    private Long getNextCursorId(List<Solutions> responseSolutions, String sort) {
        Long nextCursor = responseSolutions.size() != 0 ?
                responseSolutions.get(responseSolutions.size() - 1).getSolution().getId() : -1;

        /* todo : 기존의 최신 순 외에 정렬 기준 추가 시 nextCursorId도 다르게 적용 필요
        if(SolutionSort.HOT.getName().equals(sort)) {
            nextCursor = responseSolutions.size() != 0 ?
                    responseSolutions.get(responseSolutions.size() - 1).getSolution().getViewCount() : -1;
        }
         */

        return nextCursor;
    }

    public SolutionResponse readSolution(String loggedInEmail, Long problemId, Long solutionId) {
        Member member = memberService.getMemberByEmail(loggedInEmail);
        Solution solution = solutionRepository.findById(solutionId)
                .orElseThrow(EntityNotFoundException::new);

        SolutionView solutionView = SolutionView.builder()
                .member(member)
                .solution(solution)
                .build();

        solutionViewService.createSolutionView(solutionView);

        return solutionRepository.findById(solutionId).map(
                        value -> SolutionResponse.builder()
                                .author(AuthorDto.builder()
                                        .avatar("")
                                        .nickname(value.getMember().getNickname())
                                        .build())
                                .solution(SolutionResponse.SolutionData.builder()
                                        .title(value.getTitle())
                                        .createdAt(convertLocalDateTimeToString(value.getCreatedAt()))
                                        .content(value.getContent())
                                        .languages(getLanguages(value.getSolutionLanguages(), solutionId))
                                        .likeCount(getLikeCount(solutionId))
                                        .viewCount(getViewCount(solutionId))
                                        .commentCount(getCommentCount(solutionId))
                                        .isLike(solutionLikeRepository.existsByMemberIdAndSolutionId(member.getId(), solutionId))
                                        .build())
                                .build()
                )
                .orElseThrow(() -> new EntityNotFoundException("해당 풀이 글을 찿을 수 없습니다"));
    }

    private String convertLocalDateTimeToString(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private List<String> getLanguages(List<SolutionLanguage> solutionLanguages, Long solutionId) {
        return solutionLanguages.stream()
                .map(solutionLanguage -> solutionLanguage.getAvailableLanguage().getProgrammingLanguage().getName().getName())
                .collect(Collectors.toList());
    }

    private Long getLikeCount(Long solutionId) {
        return solutionLikeRepository.countBySolutionId(solutionId);
    }

    private Long getCommentCount(Long solutionId) {
        return commentRepository.countBySolutionId(solutionId);
    }

    private Long getViewCount(Long solutionId) {
        return solutionViewService.countBySolutionId(solutionId);
    }
}
