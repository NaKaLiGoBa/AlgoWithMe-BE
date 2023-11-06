package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.domain.Problem;
import com.nakaligoba.backend.domain.ProblemTag;
import com.nakaligoba.backend.repository.ProblemTagRepository;
import com.nakaligoba.backend.domain.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ProblemTagService {

    private final ProblemTagRepository problemTagRepository;

    @Transactional
    public void tag(Problem problem, Tag tag) {
        ProblemTag problemTag = new ProblemTag(problem, tag);
        problemTagRepository.save(problemTag);
    }
}
