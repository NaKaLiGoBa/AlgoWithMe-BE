package com.nakaligoba.backend.domain;

import com.nakaligoba.backend.domain.Difficulty;
import com.nakaligoba.backend.domain.Problem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ProblemTest {

    @Test
    @DisplayName("문제 번호, 제목, 설명, 난이도, 파라미터 이름, 테스트케이스로 문제 생성")
    void createProblem() {
        int number = 1;
        String title = "겁나 어려운 문제";
        String description = "# 설명\n매우 어려운 설명\n## 소제목";
        Difficulty difficulty = Difficulty.HARD;

        Problem problem = Problem.defaultBuilder()
                .number(number)
                .title(title)
                .description(description)
                .difficulty(difficulty)
                .build();

        assertThat(problem.getNumber()).isEqualTo(number);
        assertThat(problem.getTitle()).isEqualTo(title);
        assertThat(problem.getAcceptance()).isEqualTo(BigDecimal.ZERO);
    }
}
