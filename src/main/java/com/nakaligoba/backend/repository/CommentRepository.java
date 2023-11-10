package com.nakaligoba.backend.repository;

import com.nakaligoba.backend.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Long countBySolutionId(Long solutionId);

    // 최신 순 정렬
    Page<Comment> findBySolutionIdOrderByCreatedAtDesc(Long solutionId, Pageable pageable);

    // 좋아요 순 정렬
    @Query("SELECT c FROM Comment c LEFT JOIN c.commentLikes cl WHERE c.solution.id = :solutionId GROUP BY c.id ORDER BY COUNT(cl) DESC")
    Page<Comment> findBySolutionIdOrderByLikesDesc(Long solutionId, Pageable pageable);
}
