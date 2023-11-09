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
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SolutionService {

    private final SolutionRepository solutionRepository;
    private final MemberRepository memberRepository;
    private final ProblemRepository problemRepository;
    private final ProgrammingLanguageRepository programmingLanguageRepository;
    private final SolutionLanguageRepository solutionLanguageRepository;

    @Transactional(readOnly = true)
    public Optional<Solution> findById(Long id) {
        return solutionRepository.findById(id);
    }

    @Transactional
    public long createSolution(String writerEmail, long problemId, SolutionRequest request) {
        Member member = memberRepository.findByEmail(writerEmail)
                .orElseThrow(EntityNotFoundException::new);
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(EntityNotFoundException::new);
        Solution solution = Solution.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .member(member)
                .problem(problem)
                .build();

        solutionRepository.save(solution);

        ArrayList<SolutionLanguage> solutionLanguages = new ArrayList<>();

        for (String language : request.getLanguages()) {
            ProgrammingLanguage programmingLanguage = programmingLanguageRepository.findByName(Language.findByName(language).orElseThrow())
                    .orElseThrow(EntityNotFoundException::new);

            SolutionLanguage solutionLanguage = SolutionLanguage.builder()
                    .solution(solution)
                    .programmingLanguage(programmingLanguage)
                    .build();

            solutionLanguages.add(solutionLanguage);
        }

        solutionLanguageRepository.saveAll(solutionLanguages);

        return solution.getId();
    }

    public Long updateSolution(String writerEmail, Long problemId, Long solutionId, SolutionRequest request) {
        Member member = memberRepository.findByEmail(writerEmail)
                .orElseThrow(EntityNotFoundException::new);
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(IllegalAccessError::new);
        Solution solution = solutionRepository.findById(solutionId)
                .orElseThrow(IllegalAccessError::new);

        solution.changeTitle(request.getTitle());
        solution.changeContent(request.getContent());

        List<ProgrammingLanguage> programmingLanguages = request.getLanguages().stream()
                .map(language -> programmingLanguageRepository.findByName(Language.findByName(language).orElseThrow())
                        .orElseThrow(IllegalAccessError::new))
                .collect(Collectors.toList());

        solution.updateSolutionLanguages(programmingLanguages);
        solutionRepository.save(solution);

        return solution.getId();
    }

    public void removeSolution(String writerEmail, Long problemId, Long solutionId) {
        Member member = memberRepository.findByEmail(writerEmail)
                .orElseThrow(IllegalAccessError::new);
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(IllegalArgumentException::new);
        Solution solution = solutionRepository.findById(solutionId)
                .orElseThrow(IllegalAccessError::new);

        if (!solution.getMember().equals(member)) {
            throw new UnauthorizedException("권한이 없습니다.");
        }

        solutionRepository.delete(solution);
    }

    @Transactional(readOnly = true)
    public SolutionsResponse readSolutions(Long problemId, Long nextCursorId, Integer size) {
        long solutionTotalCount = solutionRepository.countByProblemId(problemId);
        List<Solution> solutions = getSolutions(problemId, nextCursorId, size);
        List<Solutions> responseSolutions = getResponseSolutions(solutions);
        long nextCursor = responseSolutions.size() != 0 ?
                responseSolutions.get(responseSolutions.size() - 1).getSolution().getId() : -1;

        return SolutionsResponse.builder()
                .totalCount(solutionTotalCount)
                .solutions(responseSolutions)
                ._link(Link.builder()
                        .nextCursor(nextCursor)
                        .build())
                .build();
    }

    private List<Solution> getSolutions(long problemId, long nextCursorId, int size) {
        Pageable pageable = PageRequest.of(0, size);

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
                                        .build())
                                .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public SolutionResponse readSolution(long problemId, long solutionId) {
        return solutionRepository.findById(solutionId).map(
                        solution -> SolutionResponse.builder()
                                .author(AuthorDto.builder()
                                        .avatar("")
                                        .nickname(solution.getMember().getNickname())
                                        .build())
                                .solution(SolutionResponse.SolutionData.builder()
                                        .title(solution.getTitle())
                                        .createdAt(convertLocalDateTimeToString(solution.getCreatedAt()))
                                        .content(solution.getContent())
                                        .languages(getLanguages(solution.getSolutionLanguages(), solutionId))
                                        .build())
                                .build()
                )
                .orElseThrow(() -> new EntityNotFoundException("해당 풀이 글을 찿을 수 없습니다"));
    }

    private List<String> getLanguages(List<SolutionLanguage> solutionLanguages, long solutionId) {
        return solutionLanguages.stream()
                .map(solutionLanguage -> solutionLanguage.getProgrammingLanguage().getName().getName())
                .collect(Collectors.toList());
    }

    private String convertLocalDateTimeToString(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
