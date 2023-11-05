package com.nakaligoba.backend.problem.controller;

import com.nakaligoba.backend.problem.application.dto.CheckTestcaseResult;
import com.nakaligoba.backend.problem.application.usecase.CheckTestcasesUseCase;
import com.nakaligoba.backend.problem.controller.dto.CheckCodeRequest;
import com.nakaligoba.backend.problem.controller.dto.CheckTestcaseResponse;
import com.nakaligoba.backend.problem.controller.dto.InputResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}
