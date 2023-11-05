package com.nakaligoba.backend.solution.application;

import com.nakaligoba.backend.member.domain.Member;
import com.nakaligoba.backend.member.domain.MemberRepository;
import com.nakaligoba.backend.problem.domain.Problem;
import com.nakaligoba.backend.problem.domain.ProblemRepository;
import com.nakaligoba.backend.programminglanguage.domain.ProgrammingLanguage;
import com.nakaligoba.backend.programminglanguage.domain.ProgrammingLanguageRepository;
import com.nakaligoba.backend.solution.controller.dto.SolutionRequest;
import com.nakaligoba.backend.solution.domain.Solution;
import com.nakaligoba.backend.solution.domain.SolutionRepository;
import com.nakaligoba.backend.solutionlanguage.domain.SolutionLanguage;
import com.nakaligoba.backend.solutionlanguage.domain.SolutionLanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
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
        
        if (!solution.getMember().getId().equals(member.getId())) {
            throw new UnauthorizedException("권한이 없습니다.");
        }

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
}
