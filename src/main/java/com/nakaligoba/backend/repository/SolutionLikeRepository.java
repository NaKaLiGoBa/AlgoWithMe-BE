package com.nakaligoba.backend.repository;

import com.nakaligoba.backend.domain.Member;
import com.nakaligoba.backend.domain.Solution;
import com.nakaligoba.backend.domain.SolutionLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SolutionLikeRepository extends JpaRepository<SolutionLike, Long> {
    Long countBySolutionId(Long solutionId);

    boolean existsByMemberIdAndSolutionId(Long memberId, Long solutionId);

    Optional<SolutionLike> findByMemberAndSolution(Member member, Solution solution);
}
