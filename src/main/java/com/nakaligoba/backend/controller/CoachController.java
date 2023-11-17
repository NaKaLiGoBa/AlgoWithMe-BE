package com.nakaligoba.backend.controller;

import com.nakaligoba.backend.controller.payload.request.CoachRequest;
import com.nakaligoba.backend.controller.payload.response.CoachResponse;
import com.nakaligoba.backend.service.dto.AnswerDto;
import com.nakaligoba.backend.service.impl.AICoachService;
import com.nakaligoba.backend.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/v1/problems/{problemId}/answers")
@RestController
@RequiredArgsConstructor
public class CoachController {

    private final AICoachService aiCoachService;

    @PostMapping
    public ResponseEntity<CoachResponse> aiCoach(
            @PathVariable Long problemId,
            @RequestBody CoachRequest request
            ) {
        String email = JwtUtils.getEmailFromSpringSession();
        AnswerDto answer = aiCoachService.createAnswer(email, problemId, request.getQuestion(), request.getCode());
        CoachResponse response = new CoachResponse(answer.getAnswer());
        return ResponseEntity.ok(response);
    }

}
