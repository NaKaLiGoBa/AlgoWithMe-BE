package com.nakaligoba.backend.problemtag.application;

import com.nakaligoba.backend.problem.domain.Problem;
import com.nakaligoba.backend.problemtag.domain.ProblemTag;
import com.nakaligoba.backend.problemtag.domain.ProblemTagRepository;
import com.nakaligoba.backend.tag.domain.Tag;
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
