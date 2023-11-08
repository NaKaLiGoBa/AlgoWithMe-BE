package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.acceptance.fixtures.ProblemFixture;
import com.nakaligoba.backend.controller.payload.response.ProblemResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProblemServiceTest {

    @Autowired
    private ProblemService problemService;

    @Autowired
    private ProblemFacade problemFacade;

    @Test
    @DisplayName("문제 식별자로 문제 정보를 읽을 수 있다.")
    void readByProblemId() {
        Long problemId = problemFacade.createProblem(ProblemFixture.CREATE_PROBLEM_REQUEST);

        ProblemResponse problem = problemService.readProblem(problemId);

        System.out.println(problem);
        assertThat(problem.getDefaultCodes()).isNotEmpty();
        assertThat(problem.getDefaultCodes().get("java")).contains("class Solution {\n" +
                "    public String solve(String a, String b) {\n" +
                "        String answer = \"\";\n" +
                "        return answer;\n" +
                "    }\n" +
                "}");
    }
}
