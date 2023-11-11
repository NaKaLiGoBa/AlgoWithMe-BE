package com.nakaligoba.backend.repository;

import com.nakaligoba.backend.domain.SolutionLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolutionLikeRepository extends JpaRepository<SolutionLike, Long> {
    Long countBySolutionId(Long solutionId);

    boolean existsByMemberIdAndSolutionId(Long memberId, Long solutionId);
}
