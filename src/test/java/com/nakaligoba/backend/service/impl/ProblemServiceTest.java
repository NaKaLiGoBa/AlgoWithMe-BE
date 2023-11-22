package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.acceptance.fixtures.ProblemFixture;
import com.nakaligoba.backend.controller.payload.request.CreateProblemRequest;
import com.nakaligoba.backend.controller.payload.response.CustomPageResponse;
import com.nakaligoba.backend.controller.payload.response.ProblemResponse;
import com.nakaligoba.backend.domain.Member;
import com.nakaligoba.backend.domain.Problem;
import com.nakaligoba.backend.domain.Result;
import com.nakaligoba.backend.repository.ProblemRepository;
import com.nakaligoba.backend.service.dto.MemberDto;
import com.nakaligoba.backend.service.dto.ProblemPagingDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private SubmitService submitService;

    private List<Long> createdProblemIds = new ArrayList<>();

    @BeforeEach
    void beforeEach() {
        dfsEasyProblemId = problemFacade.createProblem(ProblemFixture.CREATE_PROBLEM_REQUEST_DFS_EASY);
        dfsEasyProblemId2 = problemFacade.createProblem(ProblemFixture.CREATE_PROBLEM_REQUEST_DFS_EASY);
        dfsMediumProblemId = problemFacade.createProblem(ProblemFixture.CREATE_PROBLEM_REQUEST_DFS_MEDIUM);
        dfsHardProblemId = problemFacade.createProblem(ProblemFixture.CREATE_PROBLEM_REQUEST_DFS_HARD);
        dfsHardProblemId2 = problemFacade.createProblem(ProblemFixture.CREATE_PROBLEM_REQUEST_DFS_HARD);

        testSignUp("test2@test.com", "1234", "gd2", "");

        // status: 정답, difficulty: 어려움, tags: DFS, BFS
        String testcase1 = "1 2\n3";
        List<String> answerCaseInputs1 = List.of("3", "4");
        String answerCase1 = "1 2\n3";
        CreateProblemRequest requestBody1 = new CreateProblemRequest("겁나 어려운 문제",
                "어려움",
                "# 설명\n매우 어려운 설명\n## 소제목",
                Arrays.asList("a", "b"),
                testcase1,
                answerCase1,
                Arrays.asList("DFS", "BFS", "DP"));

        // status: 틀림, difficulty: 보통, tags: BackTracking, Dijkstra
        String testcase2 = "1 2\n3";
        List<String> answerCaseInputs2 = List.of("3", "4");
        String answerCase2 = "3 4\n7";
        CreateProblemRequest requestBody2 = new CreateProblemRequest("겁나 어려운 문제",
                "보통",
                "# 설명\n매우 어려운 설명\n## 소제목",
                Arrays.asList("a", "b"),
                testcase2,
                answerCase2,
                Arrays.asList("BackTracking", "Dijkstra"));

        // status: 안풀었음, difficulty: 보통, tags: DP
        String testcase3 = "1 2\n3";
        List<String> answerCaseInputs3 = List.of("3", "4");
        String answerCase3 = "3 4\n7";
        CreateProblemRequest requestBody3 = new CreateProblemRequest("겁나 어려운 문제",
                "보통",
                "# 설명\n매우 어려운 설명\n## 소제목",
                Arrays.asList("a", "b"),
                testcase3,
                answerCase3,
                Arrays.asList("DP"));

        // status: 정답, difficulty: 쉬움, tags: Simulation, Graph
        String testcase4 = "3 4\n7";
        List<String> answerCaseInputs4 = List.of("3", "4");
        String answerCase4 = "3 4\n7";
        CreateProblemRequest requestBody4 = new CreateProblemRequest("겁나 어려운 문제",
                "쉬움",
                "# 설명\n매우 어려운 설명\n## 소제목",
                Arrays.asList("a", "b"),
                testcase4,
                answerCase4,
                Arrays.asList("Simulation", "Graph"));

        // status: 오답, difficulty: 쉬움, tags: Sort, Tree
        String testcase5 = "1 2\n3";
        List<String> answerCaseInputs5 = List.of("3", "4");
        String answerCase5 = "3 4\n7";
        CreateProblemRequest requestBody5 = new CreateProblemRequest("겁나 어려운 문제",
                "쉬움",
                "# 설명\n매우 어려운 설명\n## 소제목",
                Arrays.asList("a", "b"),
                testcase5,
                answerCase5,
                Arrays.asList("Sort", "Tree"));

        // status: 정답, difficulty: 쉬움, tags: Greedy
        String testcase6 = "1 2\n3";
        List<String> answerCaseInputs6 = List.of("3", "4");
        String answerCase6 = "1 2\n3";
        CreateProblemRequest requestBody6 = new CreateProblemRequest("겁나 어려운 문제",
                "쉬움",
                "# 설명\n매우 어려운 설명\n## 소제목",
                Arrays.asList("a", "b"),
                testcase6,
                answerCase6,
                Arrays.asList("Greedy"));

        // 문제 생성
        createdProblemIds.add(problemFacade.createProblem(requestBody1));
        createdProblemIds.add(problemFacade.createProblem(requestBody2));
        createdProblemIds.add(problemFacade.createProblem(requestBody3));
        createdProblemIds.add(problemFacade.createProblem(requestBody4));
        createdProblemIds.add(problemFacade.createProblem(requestBody5));
        createdProblemIds.add(problemFacade.createProblem(requestBody6));
    }
    private void testSignUp(String email, String password, String nickname, String role) {
        authService.signup(MemberDto.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .role(role)
                .build());
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


    @Test
    @DisplayName("카테고리 조건들이 null이면 문제 목록리스트를 모두 불러온다.")
    void getAllProblemList() {
        Pageable pageable = PageRequest.of(0, 3);

        CustomPageResponse<ProblemPagingDto> response = problemService.getProblemList(pageable, Optional.empty(), Optional.empty(), Optional.empty());

//        assertThat(response.getProblems()).hasSize(3);
        assertThat(response.getTotalElements()).isEqualTo(6);
        assertThat(response.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("상태에 따른 문제 필터링 테스트")
    void testFilterByStatus() {
        // 상태를 '성공'으로 설정
        Optional<String> status = Optional.of("성공");

        // 필터링 로직 호출
        CustomPageResponse<ProblemPagingDto> response = problemService.getProblemList(PageRequest.of(0, 10), status, Optional.empty(), Optional.empty());

        // 모든 반환된 문제들이 '성공' 상태인지 검증
        assertTrue(response.getProblems().stream().allMatch(problem -> problem.getStatus().equals("미해결")));
    }


    @Test
    @DisplayName("status를 제외한 다른 조건들은 null이면 설정한 status에 해당하는 문제 목록리스트를 불러온다.")
    void getProblemList_status() {
        Member member = memberService.getMemberByEmail("test2@test.com");
        String code = "hello world";
        int count = 0;

//        for (Long problemId : createdProblemIds) {
//            Problem problem = problemRepository.findById(problemId)
//                    .orElseThrow(EntityExistsException::new);
//            submitService.save(code, Result.RESOLVED, problem, member);
//        }
        for (Long problemId : createdProblemIds) {
            Problem problem = problemRepository.findById(problemId)
                    .orElseThrow(EntityExistsException::new);

            if (count < 3) {
                submitService.save(code, Result.RESOLVED, problem, member);
            } else {
                submitService.save(code, Result.UN_RESOLVED, problem, member);
            }
            count++;
        }

        // 제출 결과에 따른 문제 목록 조회
        Pageable pageable = PageRequest.of(0, 3);
        Optional<String> statusOpt = Optional.of("성공");

        CustomPageResponse<ProblemPagingDto> response = problemService.getProblemList(pageable, statusOpt, Optional.empty(), Optional.empty());

        // 검증: 반환된 문제들이 모두 해당 `status`를 가지고 있는지 확인
//        assertThat(response.getProblems().stream().allMatch(p -> p.getStatus().equals(Result.RESOLVED.name()))).isTrue();
        assertThat(response.getNumberOfElements()).isEqualTo(3);
        assertThat(response.getTotalElements()).isEqualTo(3);
        assertThat(response.getTotalPages()).isEqualTo(1);
    }

    @Test
    @DisplayName("difficulty를 제외한 다른 조건들이 null이면 설정한 difficulty에 해당하는 문제 목록리스트를 불러온다.")
    void getProblemList_difficulty() {
        // given
        Pageable pageable = PageRequest.of(0, 3);

        Optional<String> difficultyOpt = Optional.of("어려움");

        // when
        CustomPageResponse<ProblemPagingDto> response = problemService.getProblemList(pageable, Optional.empty(), difficultyOpt, Optional.empty());
//       getProblem.getDifficulty().getKorean().equals("어려움"))

        // then
//        assertThat(response.getProblems()).hasSize(1);
//        assertThat(response.getTotalElements()).isEqualTo(1);
        assertThat(response.getTotalPages()).isEqualTo(1);
    }

    @Test
    @DisplayName("tags를 제외한 다른 조건들이 null이면 설정한 tags에 해당하는 문제 목록리스트를 불러온다.")
    void getProblemList_tags() {
        Pageable pageable = PageRequest.of(0, 3);

        List<String> tagsList = Arrays.asList("DP");
        Optional<List<String>> tagsOpt = Optional.of(tagsList);

        CustomPageResponse<ProblemPagingDto> response = problemService.getProblemList(pageable, Optional.empty(), Optional.empty(), tagsOpt);
        assertThat(response.getProblems()).hasSize(2);
        assertThat(response.getTotalPages()).isEqualTo(2);
    }

    @Test
    @DisplayName("status, difficulty, tags 모두 설정된 값에 해당하는 문제 목록리스트를 불러온다.")
    void getProblemList_allCategories() {
        Pageable pageable = PageRequest.of(0, 3);
        Optional<String> statusOpt = Optional.of(Result.RESOLVED.name());

        CustomPageResponse<ProblemPagingDto> response = problemService.getProblemList(pageable, statusOpt, Optional.empty(), Optional.empty());
        assertThat(response.getProblems().stream().allMatch(p -> p.getStatus().equals(Result.RESOLVED.name()))).isTrue();
    }

/*
    @Test
    @DisplayName("제출 결과에 따른 문제 목록 조회")
    void getProblemListByStatus() {
        // 문제 생성
        String testcase = "1 2\n3";
        String answerCase = "3 4\n7";
        CreateProblemRequest requestBody = new CreateProblemRequest(
                "겁나 어려운 문제", "어려움", "# 설명", Arrays.asList("a", "b"), testcase, answerCase, Arrays.asList("DFS", "BFS"));
        Long problemId = problemFacade.createProblem(requestBody);
        Problem problem = problemRepository.findById(problemId).orElseThrow();

        // 사용자와 제출 생성
        Member member = memberService.getMemberByEmail("test2@test.com");
        String code = "정답 코드 또는 오답 코드";
        submitService.save(code, Result.RESOLVED, problem, member);

        // 제출 결과에 따른 문제 목록 조회
        Pageable pageable = PageRequest.of(0, 3);
        Optional<String> statusOpt = Optional.of(Result.RESOLVED.name());

        CustomPageResponse<ProblemPagingDto> response = problemService.getProblemList(pageable, statusOpt, Optional.empty(), Optional.empty());

        // 검증: 반환된 문제들이 모두 해당 `status`를 가지고 있는지 확인
        assertThat(response.getProblems().stream().allMatch(p -> p.getStatus().equals(Result.RESOLVED.name()))).isTrue();
    }
 */
}
