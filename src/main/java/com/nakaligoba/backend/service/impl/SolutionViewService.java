package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.domain.SolutionView;
import com.nakaligoba.backend.repository.SolutionViewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SolutionViewService {

    private final SolutionViewRepository solutionViewRepository;

    public Long countBySolutionId(Long solutionId) {
        return solutionViewRepository.countBySolutionId(solutionId);
    }

    public void createSolutionView(SolutionView solutionView) {
        solutionViewRepository.save(solutionView);
    }
}
