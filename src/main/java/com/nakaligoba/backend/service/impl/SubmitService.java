package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.domain.*;
import com.nakaligoba.backend.repository.MemberRepository;
import com.nakaligoba.backend.repository.ProblemRepository;
import com.nakaligoba.backend.repository.SubmitRepository;
import com.nakaligoba.backend.service.dto.SubmitDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubmitService {

    private final ProblemService problemService;

    private final SubmitRepository submitRepository;
    private final MemberRepository memberRepository;
    private final ProblemRepository problemRepository;

    @Transactional
    public void create(String code, Language language, Result result, Problem problem, Member member, String runtime, String memory, String timeComplexity, String spaceComplexity) {
        Submit submit = Submit.builder()
                .code(code)
                .language(language)
                .result(result)
                .problem(problem)
                .member(member)
                .runtime(runtime)
                .memory(memory)
                .timeComplexity(timeComplexity)
                .spaceComplexity(spaceComplexity)
                .build();
        submitRepository.save(submit);
        problem.addSubmit(submit);
        problemService.updateProblemAcceptance(problem.getId());
    }

    @Transactional(readOnly = true)
    public Collection<SubmitDto> readAll(String email, Long problemId) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(EntityNotFoundException::new);

        List<Submit> submits = submitRepository.findAllByMemberAndProblem(member, problem);

        return submits.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private SubmitDto mapToDto(Submit submit) {
        return SubmitDto.builder()
                .id(submit.getId())
                .status(submit.getResult().getKorean())
                .language(submit.getLanguage().getName())
                .runtime(submit.getRuntime())
                .memory(submit.getMemory())
                .timeComplexity(submit.getTimeComplexity())
                .spaceComplexity(submit.getSpaceComplexity())
                .build();
    }

    public String getSubmitCode(Long submitId) {
        Submit submit = submitRepository.findById(submitId)
                .orElseThrow(EntityNotFoundException::new);

        return submit.getCode();
    }
}
