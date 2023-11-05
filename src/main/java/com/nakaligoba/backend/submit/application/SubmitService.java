package com.nakaligoba.backend.submit.application;

import com.nakaligoba.backend.member.domain.Member;
import com.nakaligoba.backend.problem.domain.Problem;
import com.nakaligoba.backend.submit.domain.Result;
import com.nakaligoba.backend.submit.domain.Submit;
import com.nakaligoba.backend.submit.domain.SubmitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubmitService {

    private final SubmitRepository submitRepository;

    @Transactional
    public void save(String code, Result result, Problem problem, Member member) {
        Submit submit = Submit.builder()
                .code(code)
                .result(result)
                .problem(problem)
                .member(member)
                .build();
        submitRepository.save(submit);
    }
}
