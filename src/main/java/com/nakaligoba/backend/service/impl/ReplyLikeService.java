package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.repository.ReplyLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReplyLikeService {

    private final ReplyLikeRepository replyLikeRepository;

    @Transactional
    public void deleteByReplyId(Long replyId) {
        replyLikeRepository.deleteByReplyId(replyId);
    }
}
