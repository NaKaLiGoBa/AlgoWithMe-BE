package com.nakaligoba.backend.solution.application;

import com.github.dockerjava.api.exception.UnauthorizedException;
import com.nakaligoba.backend.member.domain.Member;
import com.nakaligoba.backend.member.domain.MemberRepository;
import com.nakaligoba.backend.problem.domain.Problem;
import com.nakaligoba.backend.problem.domain.ProblemRepository;
import com.nakaligoba.backend.programminglanguage.domain.ProgrammingLanguage;
import com.nakaligoba.backend.programminglanguage.domain.ProgrammingLanguageRepository;
import com.nakaligoba.backend.solution.controller.dto.SolutionRequest;
import com.nakaligoba.backend.solution.application.dto.AuthorDto;
import com.nakaligoba.backend.solution.controller.dto.SolutionsResponse;
import com.nakaligoba.backend.solution.controller.dto.SolutionsResponse.Link;
import com.nakaligoba.backend.solution.controller.dto.SolutionsResponse.Solutions;
import com.nakaligoba.backend.solution.controller.dto.SolutionsResponse.SolutionsData;
import com.nakaligoba.backend.solution.domain.Solution;
import com.nakaligoba.backend.solution.domain.SolutionRepository;
import com.nakaligoba.backend.solutionlanguage.domain.SolutionLanguage;
import com.nakaligoba.backend.solutionlanguage.domain.SolutionLanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SolutionService {

    private final SolutionRepository solutionRepository;
    private final MemberRepository memberRepository;
    private final ProblemRepository problemRepository;
    private final ProgrammingLanguageRepository programmingLanguageRepository;
    private final SolutionLanguageRepository solutionLanguageRepository;

    @Transactional
    public long createSolution(String writerEmail, long problemId, SolutionRequest request) {
        Member member = memberRepository.findByEmail(writerEmail);
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(IllegalArgumentException::new);
        Solution solution = Solution.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .member(member)
                .problem(problem)
                .build();

        solutionRepository.save(solution);

        ArrayList<SolutionLanguage> solutionLanguages = new ArrayList<>();

        for (String language : request.getLanguages()) {
            ProgrammingLanguage programmingLanguage = programmingLanguageRepository.findByName(language)
                    .orElseThrow(IllegalArgumentException::new);

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
        Member member = memberRepository.findByEmail(writerEmail);
        if (member == null) {
            throw new EntityNotFoundException("회원을 찾을 수 없습니다.");
        }
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(IllegalAccessError::new);
        Solution solution = solutionRepository.findById(solutionId)
                .orElseThrow(IllegalAccessError::new);

        solution.changeTitle(request.getTitle());
        solution.changeContent(request.getContent());

        List<ProgrammingLanguage> programmingLanguages = request.getLanguages().stream()
                .map(language -> programmingLanguageRepository.findByName(language)
                        .orElseThrow(IllegalAccessError::new))
                .collect(Collectors.toList());

        solution.updateSolutionLanguages(programmingLanguages);
        solutionRepository.save(solution);

        return solution.getId();
    }

    public void removeSolution(String writerEmail, Long problemId, Long solutionId) {
        Member member = memberRepository.findByEmail(writerEmail);
        if (member == null) {
            throw new IllegalAccessError();
        }
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
}
