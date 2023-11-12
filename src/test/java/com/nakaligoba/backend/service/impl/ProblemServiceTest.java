package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.acceptance.fixtures.ProblemFixture;
import com.nakaligoba.backend.controller.payload.request.CreateProblemRequest;
import com.nakaligoba.backend.controller.payload.response.ProblemResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

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

    @Test
    @DisplayName("문제 식별자로 문제 조회 시 채점용 테스트케이스는 반환되지 않는다.")
    void readProblem_withOutAnswerCase() {
        String testcase = "1 2\n3";
        List<String> answerCaseInputs = List.of("3", "4");
        String answerCase = "3 4\n7";
        CreateProblemRequest requestBody = new CreateProblemRequest("겁나 어려운 문제",
                "어려움",
                "# 설명\n매우 어려운 설명\n## 소제목",
                Arrays.asList("a", "b"),
                testcase,
                answerCase,
                Arrays.asList("DFS", "BFS"));
        Long problemId = problemFacade.createProblem(requestBody);

        ProblemResponse problem = problemService.readProblem(problemId);

        problem.getTestcases()
                .stream()
                .map(ProblemResponse.TestcaseResponse::getInputs)
                .flatMap(Collection::stream)
                .forEach((v) -> assertThat(v).isNotIn("3", "4"));
    }
}
