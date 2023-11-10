package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.repository.CommentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;

    @Transactional(readOnly = true)
    public Long getCommentLikeCount(Long commentId) {
        return commentLikeRepository.countByCommentId(commentId);
    }

    @Transactional(readOnly = true)
    public boolean getIsCommentLike(Long memberId, Long commentId) {
        return commentLikeRepository.existsByMemberIdAndCommentId(memberId, commentId);
    }
}
