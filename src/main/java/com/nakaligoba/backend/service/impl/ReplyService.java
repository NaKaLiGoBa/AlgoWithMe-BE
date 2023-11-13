package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.controller.payload.request.ReplyRequest;
import com.nakaligoba.backend.controller.payload.response.RepliesResponse;
import com.nakaligoba.backend.repository.ReplyLikeRepository;
import com.nakaligoba.backend.service.dto.ReplyDto;
import com.nakaligoba.backend.domain.Comment;
import com.nakaligoba.backend.domain.Member;
import com.nakaligoba.backend.domain.Reply;
import com.nakaligoba.backend.exception.PermissionDeniedException;
import com.nakaligoba.backend.repository.CommentRepository;
import com.nakaligoba.backend.repository.ReplyRepository;
import com.nakaligoba.backend.service.dto.AuthorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final MemberService memberService;
    private final CommentRepository commentRepository;
    private final ReplyLikeService replyLikeService;
    private final ReplyLikeRepository replyLikeRepository;

    @Transactional
    public void deleteByCommentId(Long commentId) {
        replyRepository.deleteByCommentId(commentId);
    }

    @Transactional
    public Long createReply(String writerEmail, Long commentId, ReplyRequest request) {
        Member member = memberService.findByEmail(writerEmail);
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

    @Transactional
    public void updateReply(String writerEmail, Long commentId, Long replyId, ReplyRequest request) {
        Member member = memberService.findByEmail(writerEmail);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(EntityNotFoundException::new);

        if (!reply.getMember().equals(member)) {
            throw new PermissionDeniedException(member);
        }

        reply.changeReplyContent(request.getContent());
    }

    public RepliesResponse readReplies(String loggedInEmail, Long commentId) {
        Member member = memberService.findByEmail(loggedInEmail);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);

        List<Reply> replies = replyRepository.findByCommentIdOrderByCreatedAtAsc(commentId);
        Long totalCount = replyRepository.countByCommentId(commentId);

        List<ReplyDto> replyDtos = replies.stream()
                .map(reply ->
                    ReplyDto.builder()
                            .commentId(commentId)
                            .content(reply.getContent())
                            .author(AuthorDto.builder()
                                    .avatar("")
                                    .nickname(reply.getMember().getNickname())
                                    .build())
                            .likeCount(replyLikeService.getReplyLikeCount(reply.getId()))
                            .build()
                )
                .collect(Collectors.toList());

        return RepliesResponse.builder()
                .totalCount(totalCount)
                .replies(replyDtos)
                .build();
    }

    @Transactional
    public void deleteReply(String loggedInEmail, Long commentId, Long replyId) {
        Member member = memberService.findByEmail(loggedInEmail);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(EntityNotFoundException::new);

        if (!reply.getMember().equals(member)) {
            throw new PermissionDeniedException(member);
        }

        replyLikeRepository.deleteByReplyId(replyId);
        replyRepository.delete(reply);
    }
}
