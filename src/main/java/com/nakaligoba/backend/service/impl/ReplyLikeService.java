package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.domain.*;
import com.nakaligoba.backend.repository.CommentRepository;
import com.nakaligoba.backend.repository.ReplyLikeRepository;
import com.nakaligoba.backend.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ReplyLikeService {

    private final ReplyLikeRepository replyLikeRepository;
    private final MemberService memberService;
    private final CommentRepository commentRepository;
    private final ReplyRepository replyRepository;

    @Transactional
    public void deleteByReplyId(Long replyId) {
        replyLikeRepository.deleteByReplyId(replyId);
    }

    public Long getReplyLikeCount(Long replyId) {
        return replyLikeRepository.countByReplyId(replyId);
    }

    public boolean getIsReplyLike(Long memberId, Long replyId) {
        return replyLikeRepository.existsByMemberIdAndReplyId(memberId, replyId);
    }

    @Transactional
    public boolean toggleLike(String loggedInEmail, Long commentId, Long replyId, LocalDateTime time) {
        Member member = memberService.getMemberByEmail(loggedInEmail);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(EntityNotFoundException::new);

        Optional<ReplyLike> optionalReplyLike = replyLikeRepository.findByMemberAndReply(member, reply);

        if (optionalReplyLike.isEmpty()) {
            ReplyLike replyLike = new ReplyLike(member, reply);
            replyLikeRepository.save(replyLike);
            return true;
        }

        ReplyLike replyLike = optionalReplyLike.get();
        replyLikeRepository.delete(replyLike);
        return false;
    }
}
