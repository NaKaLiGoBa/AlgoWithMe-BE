package com.nakaligoba.backend.controller;

import com.nakaligoba.backend.controller.payload.request.ReplyRequest;
import com.nakaligoba.backend.service.impl.ReplyService;
import com.nakaligoba.backend.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/comments")
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/{commentId}/replies")
    public ResponseEntity<Void> createReply(
            @PathVariable Long commentId,
            @Valid @RequestBody ReplyRequest request
    ) {
        String writerEmail = JwtUtils.getEmailFromSpringSession();
        Long createdReplyId = replyService.createReply(writerEmail, commentId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", String.valueOf(createdReplyId))
                .build();
    }

    @PutMapping("/{commentId}/replies/{replyId}")
    public ResponseEntity<Void> updateReply(
            @PathVariable Long commentId,
            @PathVariable Long replyId,
            @Valid @RequestBody ReplyRequest request
    ) {
        String writerEmail = JwtUtils.getEmailFromSpringSession();
        replyService.updateReply(writerEmail, commentId, replyId, request);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
