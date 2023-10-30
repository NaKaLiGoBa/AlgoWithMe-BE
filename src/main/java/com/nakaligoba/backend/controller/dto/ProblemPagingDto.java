package com.nakaligoba.backend.controller.dto;

import com.nakaligoba.backend.entity.Problem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProblemPagingDto {

    private Long id;
    private Long number;
    private String status;
    private String title;
    private BigDecimal acceptance;
    private String difficulty;
    private List<String> tags;

    // Todo. 정답률, status 및 tags 수정하기
    public ProblemPagingDto(Problem problem) {
        this.id = problem.getId();
        this.number = problem.getNumber();
        this.status = "성공";
        this.title = problem.getTitle();
        this.acceptance = problem.getAcceptance();
        this.difficulty = problem.getDifficulty();
        this.tags = Arrays.asList("DFS", "BFS");
    }
}
