package com.nakaligoba.backend.controller;

import com.nakaligoba.backend.controller.payload.request.CommentRequest;
import com.nakaligoba.backend.service.impl.CommentService;
import com.nakaligoba.backend.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/v1/solutions")
@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{solutionId}/comments")
    public ResponseEntity<Void> createComment(@PathVariable Long solutionId, @Valid @RequestBody CommentRequest request) {
        String writerEmail = JwtUtils.getEmailFromSpringSession();
        long createdCommentId = commentService.createComment(writerEmail, solutionId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", String.valueOf(createdCommentId))
                .build();
    }

    @PutMapping("/{solutionId}/comments/{commentId}")
    public ResponseEntity<Void> updateComment(@PathVariable Long solutionId, @PathVariable Long commentId,  @Valid @RequestBody CommentRequest request) {
        String loggedInEmail = JwtUtils.getEmailFromSpringSession();
        commentService.updateComment(loggedInEmail, commentId, request);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
