package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.domain.Solution;
import com.nakaligoba.backend.domain.SolutionView;
import com.nakaligoba.backend.repository.SolutionViewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SolutionViewService {

    private final SolutionViewRepository solutionViewRepository;

    public Long countBySolutionId(Long solutionId) {
        return solutionViewRepository.countBySolutionId(solutionId).orElse(0L);
    }

    public void createSolutionView(SolutionView solutionView) {
        solutionViewRepository.save(solutionView);
    }

    public void deleteBySolutionId(Long solutionId) {
        solutionViewRepository.deleteBySolutionId(solutionId);
    }
}
