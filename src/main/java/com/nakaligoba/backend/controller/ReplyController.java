package com.nakaligoba.backend.controller;

import com.nakaligoba.backend.controller.payload.request.ReplyLikeRequest;
import com.nakaligoba.backend.controller.payload.request.ReplyRequest;
import com.nakaligoba.backend.controller.payload.response.RepliesResponse;
import com.nakaligoba.backend.controller.payload.response.ReplyLikeResponse;
import com.nakaligoba.backend.service.dto.ReplyDto;
import com.nakaligoba.backend.service.impl.ReplyLikeService;
import com.nakaligoba.backend.service.impl.ReplyService;
import com.nakaligoba.backend.utils.JwtUtils;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.Path;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/comments")
public class ReplyController {

    private final ReplyService replyService;
    private final ReplyLikeService replyLikeService;

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

    @GetMapping("{commentId}/replies")
    public ResponseEntity<RepliesResponse> readReplies(@PathVariable Long commentId) {
        String loggedInEmail = JwtUtils.getEmailFromSpringSession();
        RepliesResponse repliesResponse = replyService.readReplies(loggedInEmail, commentId);

        return ResponseEntity.status(HttpStatus.OK).body(repliesResponse);
    }

    @PostMapping("/{commentId}/replies/{replyId}/like")
    public ResponseEntity<ReplyLikeResponse> likeReply(
            @PathVariable Long commentId,
            @PathVariable Long replyId,
            @RequestBody ReplyLikeRequest request
    ) {
        String loggedInEmail = JwtUtils.getEmailFromSpringSession();
        boolean isLiked = replyLikeService.likeReply(loggedInEmail, commentId, replyId, request.getRequestDateTime());

        return ResponseEntity.status(HttpStatus.OK).body(new ReplyLikeResponse(isLiked));
    }

    @DeleteMapping("/{commentId}/replies/{replyId}")
    public ResponseEntity<Void> deleteReply(
            @PathVariable Long commentId,
            @PathVariable Long replyId
    ) {
        String loggedInEmail = JwtUtils.getEmailFromSpringSession();
        replyService.deleteReply(loggedInEmail, commentId, replyId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
