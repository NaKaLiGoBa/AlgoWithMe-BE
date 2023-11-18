package com.nakaligoba.backend.controller;

import com.nakaligoba.backend.controller.payload.request.CreateMiniQuizRequest;
import com.nakaligoba.backend.controller.payload.response.MiniQuizListResponse;
import com.nakaligoba.backend.service.dto.MiniQuizDto;
import com.nakaligoba.backend.service.impl.MiniQuizService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@Slf4j
@RequestMapping("/api/v1/problems/{problemId}/quizzes")
@RestController
@RequiredArgsConstructor
public class MiniQuizController {

    private final MiniQuizService miniQuizService;

    @GetMapping
    public ResponseEntity<MiniQuizListResponse> listMiniQuizzes(@PathVariable Long problemId) {
        List<MiniQuizDto> quizzes = miniQuizService.findAllQuizByProblemId(problemId);
        Integer quizCount = quizzes.size();
        MiniQuizListResponse response = new MiniQuizListResponse(quizCount, quizzes);
        return ResponseEntity.ok(response);
    }
}
