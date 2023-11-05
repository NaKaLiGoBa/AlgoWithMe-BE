package com.nakaligoba.backend.solution.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface SolutionRepository extends JpaRepository<Solution, Long> {
    long countByProblemId(long problemId);

    ArrayList<Solution> findByProblemIdAndIdLessThanOrderByCreatedAtDesc(long problemId, long id, Pageable pageable);

    ArrayList<Solution> findByProblemIdOrderByCreatedAtDesc(long problemId, Pageable pageable);
}
