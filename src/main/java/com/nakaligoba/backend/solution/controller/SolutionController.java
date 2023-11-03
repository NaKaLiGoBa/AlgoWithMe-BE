package com.nakaligoba.backend.solution.controller;

import com.nakaligoba.backend.solution.application.SolutionService;
import com.nakaligoba.backend.solution.controller.dto.SolutionRequest;
import com.nakaligoba.backend.utils.JwtUtils;
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
    public ResponseEntity<Void> createSolution(@PathVariable("id") long id, @RequestBody @Valid SolutionRequest request) {
        String writerEmail = JwtUtils.getEmailFromSpringSession();
        long createdSolutionId = solutionService.createSolution(writerEmail, id, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "https://k08e0a348244ea.user-app.krampoline.com/api/v1/problems/" + id + "/solutions/" + createdSolutionId)
                .build();
    }

    @PutMapping("{problemId}/solutions/{solutionId}")
    public ResponseEntity<Void> updateSolution(
            @PathVariable Long problemId,
            @PathVariable Long solutionId,
            @RequestBody SolutionRequest request
    ) {
        String writerEmail = JwtUtils.getEmailFromSpringSession();
        Long updatedSolutionId = solutionService.updateSolution(writerEmail, problemId, solutionId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "https://k08e0a348244ea.user-app.krampoline.com/api/v1/problems/" + problemId + "/solutions/" + updatedSolutionId)
                .build();
    }

    @DeleteMapping("/{problemId}/solutions/{solutionId}")
    public ResponseEntity<Void> removeSolution(
            @PathVariable Long problemId,
            @PathVariable Long solutionId
    ){
        String writerEmail = JwtUtils.getEmailFromSpringSession();
        solutionService.removeSolution(writerEmail, problemId, solutionId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
