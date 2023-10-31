package com.nakaligoba.backend.problem.application;

import com.nakaligoba.backend.problem.application.dto.ProblemPagingDto;
import com.nakaligoba.backend.problem.domain.Problem;
import com.nakaligoba.backend.problem.domain.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;

    @Transactional
    public Page<ProblemPagingDto> getProblemList(Pageable pageable) {
        Page<Problem> page = problemRepository.findAll(pageable);
        return page.map(problem -> new ProblemPagingDto(problem));
    }
}
