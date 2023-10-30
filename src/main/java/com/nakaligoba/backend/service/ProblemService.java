package com.nakaligoba.backend.service;

import com.nakaligoba.backend.controller.dto.ProblemPagingDto;
import com.nakaligoba.backend.entity.Problem;
import com.nakaligoba.backend.repository.ProblemRepository;
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
