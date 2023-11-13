package com.nakaligoba.backend.repository;

import com.nakaligoba.backend.domain.Member;
import com.nakaligoba.backend.domain.Reply;
import com.nakaligoba.backend.domain.ReplyLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyLikeRepository extends JpaRepository<ReplyLike, Long> {
    void deleteByReplyId(Long replyId);

    boolean existsByMemberAndReply(Member member, Reply reply);

    void deleteByMemberAndReply(Member member, Reply reply);

    Long countByReplyId(Long replyId);
}
