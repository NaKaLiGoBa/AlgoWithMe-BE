package com.nakaligoba.backend.problem.controller;

import com.nakaligoba.backend.problem.controller.dto.CustomPageResponse;
import com.nakaligoba.backend.problem.application.dto.ProblemPagingDto;
import com.nakaligoba.backend.problem.application.ProblemService;
import com.nakaligoba.backend.problem.controller.dto.ProblemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
//    private final ProblemRepository problemRepository;

    @GetMapping
    public CustomPageResponse<ProblemPagingDto> readAllProblems(@PageableDefault(value = 3) Pageable pageable) {
        Page<ProblemPagingDto> problemPage = problemService.getProblemList(pageable);
        return new CustomPageResponse<>(problemPage);
    }
/*
    @PostConstruct
    public void init() {
        for (int i = 1; i <= 10; i++) {
            Problem problem = new Problem((long) i, "default description","Problem " + i,"쉬움", new BigDecimal("66.6"));
            problemRepository.save(problem);
        }
    }*/

    @GetMapping("/{id}")
    public ResponseEntity<ProblemResponse> readProblem(@PathVariable Long id) {
        ProblemResponse response = problemService.readProblem(id);

        return ResponseEntity.ok(response);
    }
}
