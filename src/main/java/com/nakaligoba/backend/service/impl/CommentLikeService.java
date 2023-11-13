package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.domain.Comment;
import com.nakaligoba.backend.domain.CommentLike;
import com.nakaligoba.backend.domain.Member;
import com.nakaligoba.backend.domain.Solution;
import com.nakaligoba.backend.repository.CommentLikeRepository;
import com.nakaligoba.backend.repository.CommentRepository;
import com.nakaligoba.backend.repository.SolutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentLikeService {

    private final MemberService memberService;
    private final CommentRepository commentRepository;
    private final SolutionRepository solutionRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Transactional(readOnly = true)
    public Long getCommentLikeCount(Long commentId) {
        return commentLikeRepository.countByCommentId(commentId);
    }

    @Transactional(readOnly = true)
    public boolean getIsCommentLike(Long memberId, Long commentId) {
        return commentLikeRepository.existsByMemberIdAndCommentId(memberId, commentId);
    }

    @Transactional
    public void deleteByCommentId(Long commentId) {
        commentLikeRepository.deleteByCommentId(commentId);
    }

    @Transactional
    public boolean toggleLike(String email, Long solutionId, Long commentId) {
        Member member = memberService.findByEmail(email)
                .orElseThrow(NoSuchElementException::new);
        Solution solution = solutionRepository.findById(solutionId)
                .orElseThrow(NoSuchElementException::new);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(NoSuchElementException::new);

        Optional<CommentLike> optionalCommentLike = commentLikeRepository.findByMemberAndComment(member, comment);

        if (optionalCommentLike.isEmpty()) {
            CommentLike commentLike = new CommentLike(member, comment);
            commentLikeRepository.save(commentLike);
            return true;
        }

        CommentLike commentLike = optionalCommentLike.get();
        commentLikeRepository.delete(commentLike);
        return false;
    }
}
