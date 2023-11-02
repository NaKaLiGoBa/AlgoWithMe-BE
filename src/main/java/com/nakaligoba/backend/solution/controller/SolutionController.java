package com.nakaligoba.backend.solution.controller;

import com.nakaligoba.backend.solution.application.SolutionService;
import com.nakaligoba.backend.solution.controller.dto.SolutionCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequestMapping("/api/v1/problems")
@RequiredArgsConstructor
@RestController
public class SolutionController {

    private final SolutionService solutionService;

    @PostMapping("/{id}/solutions")
    public ResponseEntity<Void> createSolution(@PathVariable("id") long id, @RequestBody @Valid SolutionCreateRequest request) {
        long createdSolutionId = solutionService.createSolution(id, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "https://k08e0a348244ea.user-app.krampoline.com/api/v1/problems/" + id + "/solutions/" + createdSolutionId)
                .build();
    }
}
