package com.nakaligoba.backend.problem.controller;

import com.nakaligoba.backend.problem.controller.dto.CustomPageResponse;
import com.nakaligoba.backend.problem.application.dto.ProblemPagingDto;
import com.nakaligoba.backend.problem.application.ProblemService;
import com.nakaligoba.backend.problem.controller.dto.ProblemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/problems")
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping
    public ResponseEntity<CustomPageResponse<ProblemPagingDto>> readAllProblems(Pageable pageable) {
        CustomPageResponse<ProblemPagingDto> response = problemService.getProblemList(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProblemResponse> readProblem(@PathVariable Long id) {
        ProblemResponse response = problemService.readProblem(id);

        return ResponseEntity.ok(response);
    }
}
