package com.nakaligoba.backend.repository;

import com.nakaligoba.backend.domain.Solution;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface SolutionRepository extends JpaRepository<Solution, Long> {
    Long countByProblemId(Long problemId);

    // 최신순
    ArrayList<Solution> findByProblemIdOrderByCreatedAtDesc(Long problemId, Pageable pageable); // 처음

    ArrayList<Solution> findByProblemIdAndIdLessThanOrderByCreatedAtDesc(Long problemId, Long id, Pageable pageable); // 이후

    // todo : 조회수 순
    // ArrayList<Solution> findByProblemIdOrderByViewCountDesc(Long problemId, Pageable pageable); // 처음

    // ArrayList<Solution> findByProblemIdAndViewCountLessThanEqualAndIdLessThanOrderByViewCountDescIdDesc(Long problemId, Long viewCount, Long solutionId, Pageable pageable); // 이후
}
