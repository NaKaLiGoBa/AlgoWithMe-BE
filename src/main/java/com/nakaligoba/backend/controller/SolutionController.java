package com.nakaligoba.backend.controller;

import com.nakaligoba.backend.controller.payload.response.LikeResponse;
import com.nakaligoba.backend.controller.payload.response.SolutionResponse;
import com.nakaligoba.backend.service.impl.SolutionLikeService;
import com.nakaligoba.backend.service.impl.SolutionService;
import com.nakaligoba.backend.controller.payload.request.SolutionRequest;
import com.nakaligoba.backend.controller.payload.response.SolutionsResponse;
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
    private final SolutionLikeService solutionLikeService;

    @PostMapping("/{id}/solutions")
    public ResponseEntity<Void> createSolution(@PathVariable("id") Long id, @RequestBody @Valid SolutionRequest request) {
        String loggedInEmail = JwtUtils.getEmailFromSpringSession();
        Long createdSolutionId = solutionService.createSolution(loggedInEmail, id, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "https://k881facf0dd88a.user-app.krampoline.com/api/v1/problems/" + id + "/solutions/" + createdSolutionId)
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
                .header("Location", "https://k881facf0dd88a.user-app.krampoline.com/api/v1/problems/" + problemId + "/solutions/" + updatedSolutionId)
                .build();
    }

    @DeleteMapping("/{problemId}/solutions/{solutionId}")
    public ResponseEntity<Void> removeSolution(
            @PathVariable Long problemId,
            @PathVariable Long solutionId
    ) {
        String writerEmail = JwtUtils.getEmailFromSpringSession();
        solutionService.removeSolution(writerEmail, problemId, solutionId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}/solutions")
    public ResponseEntity<SolutionsResponse> readSolutions(
            @PathVariable("id") Long id,
            @RequestParam Long cursor,
            @RequestParam Integer size,
            @RequestParam String sort
    ) {
        SolutionsResponse solutionsResponse = solutionService.readSolutions(id, cursor, size, sort);

        return ResponseEntity.status(HttpStatus.OK).body(solutionsResponse);
    }

    @GetMapping("/{problemId}/solutions/{solutionId}")
    public ResponseEntity<SolutionResponse> readSolution(@PathVariable("problemId") Long problemId, @PathVariable("solutionId") Long solutionId) {
        String loggedInEmail = JwtUtils.getEmailFromSpringSession();
        SolutionResponse solutionResponse = solutionService.readSolution(loggedInEmail, problemId, solutionId);

        return ResponseEntity.status(HttpStatus.OK).body(solutionResponse);
    }


    @PostMapping("/{problemId}/solutions/{solutionId}/like")
    public ResponseEntity<LikeResponse> like(
            @PathVariable Long problemId,
            @PathVariable Long solutionId
    ) {
        String email = JwtUtils.getEmailFromSpringSession();
        boolean isLike = solutionLikeService.toggleLike(email, problemId, solutionId);
        return ResponseEntity.ok(new LikeResponse(isLike));
    }
}
