package com.nakaligoba.backend.repository;

import com.nakaligoba.backend.domain.Member;
import com.nakaligoba.backend.domain.Reply;
import com.nakaligoba.backend.domain.ReplyLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReplyLikeRepository extends JpaRepository<ReplyLike, Long> {
    void deleteByReplyId(Long replyId);

    Optional<ReplyLike> findByMemberAndReply(Member member, Reply reply);


   boolean existsByMemberIdAndReplyId(Long memberId, Long replyId);

    Long countByReplyId(Long replyId);
}
