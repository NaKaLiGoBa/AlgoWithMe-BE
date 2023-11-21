package com.nakaligoba.backend.controller;

import com.nakaligoba.backend.controller.payload.request.CreateProblemRequest;
import com.nakaligoba.backend.controller.payload.response.CustomPageResponse;
import com.nakaligoba.backend.controller.payload.response.ProblemResponse;
import com.nakaligoba.backend.service.dto.ProblemPagingDto;
import com.nakaligoba.backend.service.impl.ProblemFacade;
import com.nakaligoba.backend.service.impl.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/problems")
public class ProblemController {

    private final ProblemService problemService;
    private final ProblemFacade problemAggregateService;

    @GetMapping
    public ResponseEntity<CustomPageResponse<ProblemPagingDto>> readAllProblems(
            Pageable pageable,
            @RequestParam Optional<String> status,
            @RequestParam Optional<String> difficulty,
            @RequestParam Optional<List<String>> tags
            ) {
        CustomPageResponse<ProblemPagingDto> response = problemService.getProblemList(pageable, status, difficulty, tags);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProblemResponse> readProblem(@PathVariable Long id) {
        ProblemResponse response = problemService.readProblem(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> createProblem(@Valid @RequestBody CreateProblemRequest request) {
        Long id = problemAggregateService.createProblem(request);
        return ResponseEntity.created(URI.create("/api/v1/problems/" + id))
                .build();
    }
}
