package com.nakaligoba.backend.problem.controller;

import com.nakaligoba.backend.problem.application.dto.CheckTestcaseResult;
import com.nakaligoba.backend.problem.application.usecase.CheckTestcasesUseCase;
import com.nakaligoba.backend.problem.application.usecase.SubmitUseCase;
import com.nakaligoba.backend.problem.controller.dto.CheckCodeRequest;
import com.nakaligoba.backend.problem.controller.dto.CheckTestcaseResponse;
import com.nakaligoba.backend.problem.controller.dto.InputResponse;
import com.nakaligoba.backend.problem.controller.dto.SubmitResponse;
import com.nakaligoba.backend.problem.controller.dto.UserCodeErrorResponse;
import com.nakaligoba.backend.problem.exception.UserCodeCompileErrorException;
import com.nakaligoba.backend.problem.exception.UserCodeErrorException;
import com.nakaligoba.backend.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/problems/{id}/code")
public class ProblemCodeController {

    private final CheckTestcasesUseCase checkTestcasesUseCase;
    private final SubmitUseCase submitUseCase;

    @PostMapping("/test")
    public ResponseEntity<List<CheckTestcaseResponse>> checkTestcase(
            @PathVariable Long id,
            @RequestBody CheckCodeRequest checkCodeRequest) {
        String language = checkCodeRequest.getLanguage();
        String code = checkCodeRequest.getCode();

        List<CheckTestcaseResult> checkTestcaseResults = checkTestcasesUseCase.checkTestcases(id, language, code);
        List<CheckTestcaseResponse> responses = new ArrayList<>();

        for (CheckTestcaseResult result : checkTestcaseResults) {
            CheckTestcaseResponse response = CheckTestcaseResponse.builder()
                    .number(result.getNumber())
                    .isAnswer(result.getIsAnswer())
                    .inputs(getInputResponses(result))
                    .output(result.getOutput())
                    .expected(result.getExpected())
                    .build();
            responses.add(response);
        }

        return ResponseEntity.ok(responses);
    }

    private static List<InputResponse> getInputResponses(CheckTestcaseResult result) {
        return result.getInputs().stream()
                .map(i -> new InputResponse(i.getName(), i.getValue()))
                .collect(Collectors.toList());
    }

    @PostMapping("/submit")
    public ResponseEntity<SubmitResponse> submit(
            @PathVariable Long id,
            @RequestBody CheckCodeRequest checkCodeRequest) {
        String email = JwtUtils.getEmailFromSpringSession();
        String language = checkCodeRequest.getLanguage();
        String code = checkCodeRequest.getCode();

        boolean isAnswer = submitUseCase.isAnswer(email, id, language, code);

        SubmitResponse responses = new SubmitResponse(isAnswer);
        return ResponseEntity.ok(responses);
    }

    @ExceptionHandler(UserCodeErrorException.class)
    public ResponseEntity<UserCodeErrorResponse> userCodeError(UserCodeErrorException e) {
        UserCodeErrorResponse response;
        if (e instanceof UserCodeCompileErrorException) {
            response = new UserCodeErrorResponse(e.getMessage(), "Compile Error");
        } else {
            response = new UserCodeErrorResponse(e.getMessage(), "Runtime Error");
        }
        return ResponseEntity.badRequest()
                .body(response);
    }
}
