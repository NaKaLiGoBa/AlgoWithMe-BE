package com.nakaligoba.backend.repository;

import com.nakaligoba.backend.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    void deleteByCommentId(Long commentId);

    Long countByCommentId(Long commentId);

    List<Reply> findByCommentIdOrderByCreatedAtAsc(Long commentId);
}
