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

    @Transactional
    public boolean likeReply(String loggedInEmail, Long commentId, Long replyId, LocalDateTime time) {
        Member member = memberService.findByEmail(loggedInEmail);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(EntityNotFoundException::new);

        boolean isAlreadyLiked = replyLikeRepository.existsByMemberAndReply(member, reply);

        if (isAlreadyLiked) {
            replyLikeRepository.deleteByMemberAndReply(member, reply);
            return false;
        }

        ReplyLike newLike = new ReplyLike(reply, member);
        replyLikeRepository.save(newLike);
        return true;
    }
}
