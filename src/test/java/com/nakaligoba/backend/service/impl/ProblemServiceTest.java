package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.acceptance.fixtures.ProblemFixture;
import com.nakaligoba.backend.controller.payload.request.CreateProblemRequest;
import com.nakaligoba.backend.controller.payload.response.ProblemResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class ProblemServiceTest {

    private static final String baseUrl = "https://kde05c63df3aaa.user-app.krampoline.com/api/v1/problems/";

    @Autowired
    private ProblemService problemService;

    @Autowired
    private ProblemFacade problemFacade;

    private Long dfsEasyProblemId;
    private Long dfsEasyProblemId2;
    private Long dfsMediumProblemId;
    private Long dfsHardProblemId;
    private Long dfsHardProblemId2;

    @BeforeEach
    void beforeEach() {
        dfsEasyProblemId = problemFacade.createProblem(ProblemFixture.CREATE_PROBLEM_REQUEST_DFS_EASY);
        dfsEasyProblemId2 = problemFacade.createProblem(ProblemFixture.CREATE_PROBLEM_REQUEST_DFS_EASY);
        dfsMediumProblemId = problemFacade.createProblem(ProblemFixture.CREATE_PROBLEM_REQUEST_DFS_MEDIUM);
        dfsHardProblemId = problemFacade.createProblem(ProblemFixture.CREATE_PROBLEM_REQUEST_DFS_HARD);
        dfsHardProblemId2 = problemFacade.createProblem(ProblemFixture.CREATE_PROBLEM_REQUEST_DFS_HARD);
    }

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

    @Test
    @DisplayName("쉬운 난이도의 문제를 조회 시, 동일한 태그의 쉬운 난이도의 문제와 보통 난이도의 문제를 반환한다.")
    void readEasyProblem_withEasyProblemAndMediumProblem() {
        ProblemResponse easyProblemResponse = problemService.readProblem(dfsEasyProblemId);

        assertThat(easyProblemResponse.getEasierProblemUrl()).isEqualTo(baseUrl + dfsEasyProblemId2);
        assertThat(easyProblemResponse.getHarderProblemUrl()).isEqualTo(baseUrl + dfsMediumProblemId);
    }

    @Test
    @DisplayName("보통 난이도의 문제를 조회 시, 동일한 태그의 쉬운 난이도의 문제와 어려움 난이도의 문제를 반환한다.")
    void readMediumProblem_withEasyProblemAndHardProblem() {
        ProblemResponse mediumProblemResponse = problemService.readProblem(dfsMediumProblemId);

        assertThat(mediumProblemResponse.getEasierProblemUrl()).isEqualTo(baseUrl + dfsEasyProblemId);
        assertThat(mediumProblemResponse.getHarderProblemUrl()).isEqualTo(baseUrl + dfsHardProblemId);
    }

    @Test
    @DisplayName("어려운 난이도의 문제를 조회 시, 동일한 태그의 보통 난이도의 문제와 어려운 난이도의 문제를 반환한다.")
    void readHardProblem_withMediumProblemAndHardProblem() {
        ProblemResponse hardProblemResponse = problemService.readProblem(dfsHardProblemId);

        assertThat(hardProblemResponse.getEasierProblemUrl()).isEqualTo(baseUrl + dfsMediumProblemId);
        assertThat(hardProblemResponse.getHarderProblemUrl()).isEqualTo(baseUrl + dfsHardProblemId2);

    }

    @DisplayName("카테고리 조건들이 null이면 문제 목록리스트를 모두 불러온다.")
    void getAllProblemList() {

    }

    @Test
    @DisplayName("status를 제외한 다른 조건들은 null이면 설정한 status에 해당하는 문제 목록리스트를 불러온다.")
    void getProblemList_status() {

    }

    @Test
    @DisplayName("difficulty를 제외한 다른 조건들이 null이면 설정한 difficulty에 해당하는 문제 목록리스트를 불러온다.")
    void getProblemList_difficulty() {

    }

    @Test
    @DisplayName("tags를 제외한 다른 조건들이 null이면 설정한 tags에 해당하는 문제 목록리스트를 불러온다.")
    void getProblemList_tags() {

    }

    @Test
    @DisplayName("status, difficulty, tags 모두 설정된 값에 해당하는 문제 목록리스트를 불러온다.")
    void getProblemList_allCategories() {

    }
}
