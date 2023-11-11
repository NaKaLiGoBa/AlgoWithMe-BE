package com.nakaligoba.backend.repository;

import com.nakaligoba.backend.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    void deleteByCommentId(Long commentId);
}
