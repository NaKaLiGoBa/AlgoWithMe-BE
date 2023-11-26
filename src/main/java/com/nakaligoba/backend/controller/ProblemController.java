package com.nakaligoba.backend.controller;

import com.nakaligoba.backend.controller.payload.request.CreateProblemRequest;
import com.nakaligoba.backend.controller.payload.response.CustomPageResponse;
import com.nakaligoba.backend.controller.payload.response.ProblemResponse;
import com.nakaligoba.backend.service.dto.ProblemPagingDto;
import com.nakaligoba.backend.service.impl.ProblemFacade;
import com.nakaligoba.backend.service.impl.ProblemService;
import com.nakaligoba.backend.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/problems")
public class ProblemController {

    private final ProblemService problemService;
    private final ProblemFacade problemAggregateService;

    @GetMapping
    public ResponseEntity<CustomPageResponse<ProblemPagingDto>> readAllProblems(
            Pageable pageable,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false) List<String> tags
            ) {
        CustomPageResponse<ProblemPagingDto> response = problemService.getProblemList(pageable, status, difficulty, tags);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProblemResponse> readProblem(@PathVariable Long id) {
        String email = JwtUtils.getEmailFromSpringSession();
        ProblemResponse response = problemService.readProblem(email, id);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> createProblem(@Valid @RequestBody CreateProblemRequest request) {
        Long id = problemAggregateService.createProblem(request);
        return ResponseEntity.created(URI.create("/api/v1/problems/" + id))
                .build();
    }
}
