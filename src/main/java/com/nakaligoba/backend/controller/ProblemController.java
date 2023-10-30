package com.nakaligoba.backend.controller;

import com.nakaligoba.backend.controller.dto.CustomPageResponse;
import com.nakaligoba.backend.controller.dto.ProblemPagingDto;
import com.nakaligoba.backend.entity.Problem;
import com.nakaligoba.backend.repository.ProblemRepository;
import com.nakaligoba.backend.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Arrays;

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
}
