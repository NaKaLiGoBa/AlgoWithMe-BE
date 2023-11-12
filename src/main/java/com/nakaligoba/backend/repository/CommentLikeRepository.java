package com.nakaligoba.backend.repository;

import com.nakaligoba.backend.domain.Comment;
import com.nakaligoba.backend.domain.CommentLike;
import com.nakaligoba.backend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Long countByCommentId(Long commentId);

    boolean existsByMemberIdAndCommentId(Long memberId, Long commentId);

    void deleteByCommentId(Long commentId);

    Optional<CommentLike> findByMemberAndComment(Member member, Comment comment);
}
