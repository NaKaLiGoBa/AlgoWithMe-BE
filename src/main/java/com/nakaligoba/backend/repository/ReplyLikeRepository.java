package com.nakaligoba.backend.repository;

import com.nakaligoba.backend.domain.ReplyLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyLikeRepository extends JpaRepository<ReplyLike, Long> {
    void deleteByReplyId(Long replyId);
}
