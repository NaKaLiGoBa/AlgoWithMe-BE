package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.domain.Member;
import com.nakaligoba.backend.domain.Problem;
import com.nakaligoba.backend.domain.Result;
import com.nakaligoba.backend.domain.Submit;
import com.nakaligoba.backend.repository.SubmitRepository;
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