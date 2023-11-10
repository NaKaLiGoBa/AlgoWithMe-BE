package com.nakaligoba.backend.repository;

import com.nakaligoba.backend.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Long countByCommentId(Long commentId);

    boolean existsByMemberIdAndCommentId(Long memberId, Long commentId);
}
