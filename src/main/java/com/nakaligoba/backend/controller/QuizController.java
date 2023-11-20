package com.nakaligoba.backend.controller;

import com.nakaligoba.backend.controller.payload.request.CreateMiniQuizRequest;
import com.nakaligoba.backend.service.dto.MiniQuizDto;
import com.nakaligoba.backend.service.impl.MiniQuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@Slf4j
@RequestMapping("/api/v1/quizzes")
@RestController
@RequiredArgsConstructor
public class QuizController {

    private final MiniQuizService miniQuizService;

    @PostMapping
    public ResponseEntity<Void> createMiniQuiz(@Valid @RequestBody CreateMiniQuizRequest request) {
        MiniQuizDto dto = MiniQuizDto.builder()
                .type(request.getType())
                .description(request.getDescription())
                .explain(request.getExplain())
                .answer(request.getAnswer())
                .choiceOrInitials(request.getChoiceOrInitials())
                .build();
        Long miniQuizId = miniQuizService.createMiniQuiz(dto, request.getDifficulty(), request.getTags());
        return ResponseEntity.created(URI.create("/api/v1/quizzes/" + miniQuizId))
                .build();
    }
}
