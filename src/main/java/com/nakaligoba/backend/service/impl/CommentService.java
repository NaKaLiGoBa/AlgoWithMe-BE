package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.controller.payload.request.CommentRequest;
import com.nakaligoba.backend.controller.payload.response.CommentsResponse;
import com.nakaligoba.backend.controller.payload.response.CommentsResponse.Comments;
import com.nakaligoba.backend.controller.payload.response.CommentsResponse.CommentsData;
import com.nakaligoba.backend.domain.Comment;
import com.nakaligoba.backend.domain.CommentSort;
import com.nakaligoba.backend.domain.Member;
import com.nakaligoba.backend.domain.Reply;
import com.nakaligoba.backend.exception.PermissionDeniedException;
import com.nakaligoba.backend.repository.CommentRepository;
import com.nakaligoba.backend.service.dto.AuthorDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final MemberService memberService;
    private final SolutionService solutionService;
    private final ReplyService replyService;
    private final ReplyLikeService replyLikeService;
    private final CommentLikeService commentLikeService;
    private final CommentRepository commentRepository;

    @Transactional
    public Long createComment(String writerEmail, Long solutionId, CommentRequest request) {
        Comment comment = Comment.builder()
                .content(request.getContent())
                .member(memberService.findByEmail(writerEmail)
                        .orElseThrow(EntityNotFoundException::new))
                .solution(solutionService.findById(solutionId)
                        .orElseThrow(EntityNotFoundException::new))
                .build();

        commentRepository.save(comment);

        return comment.getId();
    }

    @Transactional
    public void updateComment(String loggedInEmail, Long commentId, CommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);

        if (!isPermission(loggedInEmail, comment.getMember().getEmail())) {
            throw new PermissionDeniedException();
        }

        comment.update(request.getContent());
    }

    @Transactional
    public void deleteComment(String loggedInEmail, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);

        if (!isPermission(loggedInEmail, comment.getMember().getEmail())) {
            throw new PermissionDeniedException();
        }

        /**
         * ※ 삭제 순서
         * 1. 답글 좋아요
         * 2. 답글
         * 3. 댓글 좋아요
         * 4. 댓글
         */

        for (Reply reply : comment.getReplies()) {
            replyLikeService.deleteByReplyId(reply.getId());
        }
        replyService.deleteByCommentId(commentId);
        commentLikeService.deleteByCommentId(commentId);
        commentRepository.deleteById(commentId);
    }

    private boolean isPermission(String loggedInEmail, String writer) {
        return StringUtils.equals(loggedInEmail, writer);
    }

    @Transactional(readOnly = true)
    public CommentsResponse readComments(String loggedInEmail, Long solutionId, Pageable pageable, String sort) {
        Member member = memberService.findByEmail(loggedInEmail)
                .orElseThrow(EntityNotFoundException::new);
        String defaultCommentSort = CommentSort.RECENT.getName();
        Page<Comment> commentPage;

        if (StringUtils.equals(defaultCommentSort, sort)) {
            commentPage = commentRepository.findBySolutionIdOrderByCreatedAtDesc(solutionId, pageable);
        } else {
            commentPage = commentRepository.findBySolutionIdOrderByLikesDesc(solutionId, pageable);
        }

        Long totalCount = commentRepository.countBySolutionId(solutionId);
        List<Comments> comments = getComments(member.getId(), commentPage);

        return CommentsResponse.builder()
                .totalCount(totalCount)
                .comments(comments)
                .pageNumber(commentPage.getNumber())
                .totalPages(commentPage.getTotalPages())
                .size(commentPage.getSize())
                .numberOfElements(commentPage.getNumberOfElements())
                .first(commentPage.isFirst())
                .last(commentPage.isLast())
                .build();
    }

    private List<Comments> getComments(Long memberId, Page<Comment> commentPage) {
        return commentPage.getContent().stream()
                .map(comment -> Comments.builder()
                        .author(AuthorDto.builder()
                                .avatar("")
                                .nickname(comment.getMember().getNickname())
                                .build())
                        .comment(CommentsData.builder()
                                .id(comment.getId())
                                .content(comment.getContent())
                                .likeCount(commentLikeService.getCommentLikeCount(comment.getId()))
                                .isLike(commentLikeService.getIsCommentLike(memberId, comment.getId()))
                                .build())
                        .build())
                .collect(Collectors.toList());
    }
}
