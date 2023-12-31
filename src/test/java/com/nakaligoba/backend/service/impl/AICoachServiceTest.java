package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.acceptance.fixtures.ProblemFixture;
import com.nakaligoba.backend.domain.ProgrammingLanguage;
import com.nakaligoba.backend.repository.ProgrammingLanguageRepository;
import com.nakaligoba.backend.service.dto.MemberDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class AICoachServiceTest {

    private static final String testCode =
            "class Solution {\\n    " +
            "private int count = 0;\\n\\n    " +
                "public int solution(int N, int S, int[] nums) {\\n        " +
                    "dfs(nums, S, 0, 0);\\n        " +
                    "return S == 0 ? count - 1 : count; \\n    " +
                "}\\n\\n    " +
            "private void dfs(int[] nums, int target, int depth, int sum) {\\n        " +
                "if (depth == nums.length) {\\n            " +
                    "if (sum == target) {\\n                " +
                        "count++;\\n            " +
                "   }\\n            " +
                "return;\\n        }\\n\\n        " +
            "dfs(nums, target, depth + 1, sum + nums[depth]); \\n        " +
            "dfs(nums, target, depth + 1, sum); \\n    " +
            "}\\n}";


    @Autowired
    private ProblemFacade problemFacade;

    @Autowired
    private AuthService authService;

    @Autowired
    private ProgrammingLanguageRepository programmingLanguageRepository;

    @Autowired
    private AICoachService aiCoachService;

    private Long createdProblemId;

    @BeforeEach
    void beforeEach() {
        // 회원 가입
        testSignUp("test1@test.com", "1234", "gd1", "admin");
        testSignUp("test2@test.com", "1234", "gd2", "");
        testSignUp("test3@test.com", "1234", "gd3", "");

        // 문제 생성
        createdProblemId = problemFacade.createProblem(ProblemFixture.CREATE_PROBLEM_REQUEST);
        programmingLanguageRepository.save(new ProgrammingLanguage("JAVA"));
    }

    @Test
    @DisplayName("AI Coach와 나눴던 질문과 답변을 리스트로 조회를 할 수 있다.")
    void readAiCoachList() {
        aiCoachService.createAnswer("test1@test.com", createdProblemId, "알고리즘 선택과 설계", testCode);
        aiCoachService.createAnswer("test1@test.com", createdProblemId, "엣지 케이스", testCode);
        aiCoachService.createAnswer("test1@test.com", createdProblemId, "코드 구조와 가독성", testCode);
        aiCoachService.createAnswer("test1@test.com", createdProblemId, "코드 최적화", testCode);

        Long count = aiCoachService.getAnswerList("test1@test.com", createdProblemId).getAnswerCount();
        assertThat(count).isEqualTo(4);
    }

    private void testSignUp(String email, String password, String nickname, String role) {
        authService.signup(MemberDto.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .role(role)
                .build());
    }
}
