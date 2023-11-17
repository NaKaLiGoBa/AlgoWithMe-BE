package com.nakaligoba.backend.repository;

import com.nakaligoba.backend.domain.SolutionView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolutionViewRepository extends JpaRepository<SolutionView, Long> {
    Long countBySolutionId(Long solutionId);
}
