package com.nakaligoba.backend.repository;

import com.nakaligoba.backend.domain.Solution;
import com.nakaligoba.backend.domain.SolutionView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SolutionViewRepository extends JpaRepository<SolutionView, Long> {
    Optional<Long> countBySolutionId(Long solutionId);

    void deleteBySolutionId(Long solutionId);
}
