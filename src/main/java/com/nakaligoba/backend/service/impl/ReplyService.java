package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.controller.payload.request.ReplyRequest;
import com.nakaligoba.backend.domain.Comment;
import com.nakaligoba.backend.domain.Member;
import com.nakaligoba.backend.domain.Reply;
import com.nakaligoba.backend.repository.CommentRepository;
import com.nakaligoba.backend.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final MemberService memberService;
    private final CommentRepository commentRepository;

    @Transactional
    public Long createReply(String writerEmail, Long commentId, ReplyRequest request) {
        Member member = memberService.findByEmail(writerEmail)
                .orElseThrow(EntityNotFoundException::new);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);

        Reply reply = Reply.builder()
                .content(request.getContent())
                .member(member)
                .comment(comment)
                .build();
        replyRepository.save(reply);

        return reply.getId();
    }
}
