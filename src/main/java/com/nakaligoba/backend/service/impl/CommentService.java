package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.controller.payload.request.CommentCreateRequest;
import com.nakaligoba.backend.domain.Comment;
import com.nakaligoba.backend.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final MemberService memberService;
    private final SolutionService solutionService;
    private final CommentRepository commentRepository;

    @Transactional
    public long createComment(String writerEmail, Long solutionId, CommentCreateRequest request) {
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
}
