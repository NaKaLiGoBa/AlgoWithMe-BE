package com.nakaligoba.backend.service.impl;

import com.github.dockerjava.api.exception.UnauthorizedException;
import com.nakaligoba.backend.acceptance.fixtures.ProblemFixture;
import com.nakaligoba.backend.controller.payload.request.SolutionRequest;
import com.nakaligoba.backend.controller.payload.response.SolutionResponse;
import com.nakaligoba.backend.controller.payload.response.SolutionsResponse;
import com.nakaligoba.backend.domain.*;
import com.nakaligoba.backend.exception.PermissionDeniedException;
import com.nakaligoba.backend.repository.ProblemRepository;
import com.nakaligoba.backend.repository.ProgrammingLanguageRepository;
import com.nakaligoba.backend.repository.SolutionRepository;
import com.nakaligoba.backend.service.dto.MemberDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class SolutionServiceTest {

    @Autowired
    private ProblemFacade problemFacade;

    @Autowired
    private SolutionService solutionService;

    @Autowired
    private SolutionViewService solutionViewService;

    @Autowired
    private AuthService authService;

    @Autowired
    private ProgrammingLanguageRepository programmingLanguageRepository;

    @Autowired
    private SolutionRepository solutionRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private SubmitService submitService;

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
    @DisplayName("특정 문제에 해당하는 풀이글을 생성할 수 있다.")
    void createSolution() {
        // given
        SolutionRequest request = new SolutionRequest();
        ArrayList<String> languages = new ArrayList<>();
        languages.add("Java");
        request.setLanguages(languages);
        request.setTitle("테스트 풀이글");
        request.setContent("테스트 풀이글 내용");

        // when
        Long solutionId = solutionService.createSolution("test1@test.com", createdProblemId, request);

        // then
        assertNotNull(solutionId);

        Solution savedSolution = solutionService.getSolution(solutionId);
        assertNotNull(savedSolution);
        assertEquals(1L, savedSolution.getId());
    }

    @Test
    @DisplayName("특정 문제에 작성된 풀이글을 수정 후 재조회 시 수정된 풀이글로 조회된다.")
    void updateSolution() {
        //given
        SolutionRequest request = new SolutionRequest();
        ArrayList<String> languages = new ArrayList<>();
        languages.add("Java");
        request.setLanguages(languages);
        request.setTitle("테스트 풀이글");
        request.setContent("테스트 풀이글 내용");
        Long solutionId = solutionService.createSolution("test1@test.com", createdProblemId, request);

        // when
        request.setTitle("수정된 풀이글 제목");
        request.setContent("수정된 풀이글 내용");
        solutionService.updateSolution("test1@test.com", createdProblemId, solutionId, request);

        // then
        Solution updatedSolution = solutionService.getSolution(solutionId);

        assertEquals("수정된 풀이글 제목", updatedSolution.getTitle());
        assertEquals("수정된 풀이글 내용", updatedSolution.getContent());
    }

    @Test
    @DisplayName("특정 문제의 풀이글은 본인이 생성한 풀이글만 삭제할 수 있다.")
    void deleteSolution() {
        //given
        SolutionRequest request = new SolutionRequest();
        ArrayList<String> languages = new ArrayList<>();
        languages.add("Java");
        request.setLanguages(languages);
        request.setTitle("테스트 풀이글");
        request.setContent("테스트 풀이글 내용");
        Long solutionId1 = solutionService.createSolution("test1@test.com", createdProblemId, request);
        Long solutionId2 = solutionService.createSolution("test1@test.com", createdProblemId, request);

        // when
        solutionService.removeSolution("test1@test.com", createdProblemId, solutionId1);

        // then
        try {
            solutionService.removeSolution("test2@test.com", createdProblemId, solutionId2);
        } catch (UnauthorizedException e) {
            Optional<Solution> existedSolution = solutionRepository.findById(solutionId2);
            assertThat(existedSolution).isNotEmpty();
        }

        Optional<Solution> removedSolution = solutionRepository.findById(solutionId1);
        assertThat(removedSolution).isEmpty();
    }

    @Test
    @DisplayName("풀이글 리스트들을 최신순으로 정렬하여 조회한다.")
    void readSolutions_sortByRecent() {
        //given
        SolutionRequest request = new SolutionRequest();
        ArrayList<String> languages = new ArrayList<>();
        languages.add("Java");
        request.setLanguages(languages);
        request.setTitle("테스트 풀이글");
        request.setContent("테스트 풀이글 내용");
        Long firstSolutionId = solutionService.createSolution("test1@test.com", createdProblemId, request);
        Long secondSolutionId = solutionService.createSolution("test2@test.com", createdProblemId, request);
        Long thirdSolutionId = solutionService.createSolution("test3@test.com", createdProblemId, request);

        Long nextCursorId = -100L;
        Integer size = 10;
        String sort = "recent";

        // when
        SolutionsResponse solutionsResponse = solutionService.readSolutions(createdProblemId, nextCursorId, size, sort);

        // then
        assertNotNull(solutionsResponse);
        List<SolutionsResponse.Solutions> getSolutions = solutionsResponse.getSolutions();
        assertTrue(getSolutions.size() > 0);

        assertEquals(thirdSolutionId, getSolutions.get(0).getSolution().getId());
        assertEquals(secondSolutionId, getSolutions.get(1).getSolution().getId());
        assertEquals(firstSolutionId, getSolutions.get(2).getSolution().getId());
    }

    @Test
    @DisplayName("작성된 풀이 글을 두 명의 유저가 조회 시 2만큼 증가된 조회 수를 확인할 수 있다.")
    void validateSolutionViewCount() {
        //given
        SolutionRequest request = new SolutionRequest();
        ArrayList<String> languages = new ArrayList<>();
        languages.add("Java");
        request.setLanguages(languages);
        request.setTitle("테스트 풀이글");
        request.setContent("테스트 풀이글 내용");
        Long createdSolutionId = solutionService.createSolution("test1@test.com", createdProblemId, request);

        // when
        SolutionResponse solutionResponse = solutionService.readSolution("test2@test.com", createdProblemId, createdSolutionId);
        SolutionResponse solutionResponse2 = solutionService.readSolution("test3@test.com", createdProblemId, createdSolutionId);
        Long viewCount = solutionResponse2.getSolution().getViewCount();

        // then
        assertThat(viewCount).isEqualTo(2);
    }

    @Test
    @DisplayName("문제해결에 성공한 유저만 풀이글을 작성할 수 있다.")
    void validateSolutionCreate() {
        SolutionRequest request = new SolutionRequest();
        ArrayList<String> languages = new ArrayList<>();
        languages.add("Java");
        request.setLanguages(languages);
        request.setTitle("테스트 풀이글");
        request.setContent("테스트 풀이글 내용");

        // when
        // 성공 시나리오
        Member member1 = memberService.getMemberByEmail("test1@test.com");
        String code1 = "hello world";

        Long createdProblemId1 = problemFacade.createProblem(ProblemFixture.CREATE_PROBLEM_REQUEST);
        Problem findProblem1 = problemRepository.findById(createdProblemId).orElseThrow(EntityNotFoundException::new);
        submitService.create(code1, Language.JAVA, Result.FAIL, findProblem1, member1, "N/A", "N/A", "N/A", "N/A");
        submitService.create(code1, Language.JAVA, Result.RESOLVED, findProblem1, member1, "N/A", "N/A", "N/A", "N/A");
        submitService.create(code1, Language.JAVA, Result.FAIL, findProblem1, member1, "N/A", "N/A", "N/A", "N/A");

        assertDoesNotThrow(() -> {
            solutionService.createSolution("test1@test.com", createdProblemId, request);
        });

        // 실패 시나리오
        Member member2 = memberService.getMemberByEmail("test2@test.com");
        String code2 = "hello world";

        Long createdProblemId2 = problemFacade.createProblem(ProblemFixture.CREATE_PROBLEM_REQUEST);
        Problem findProblem2 = problemRepository.findById(createdProblemId).orElseThrow(EntityNotFoundException::new);
        submitService.create(code2, Language.JAVA, Result.FAIL, findProblem2, member2, "N/A", "N/A", "N/A", "N/A");
        submitService.create(code2, Language.JAVA, Result.FAIL, findProblem2, member2, "N/A", "N/A", "N/A", "N/A");
        submitService.create(code2, Language.JAVA, Result.FAIL, findProblem2, member2, "N/A", "N/A", "N/A", "N/A");

        assertThrows(UnauthorizedException.class, () -> {
            solutionService.createSolution("test2@test.com", createdProblemId, request);
        });
    }

//    @Test
//    @DisplayName("작성된 풀이 글을 삭제 시 해당 풀이 글에 대한 조회 수 정보도 함께 삭제된다.")
//    void deleteSolutionViewCount() {
//        //given
//        SolutionRequest request = new SolutionRequest();
//        ArrayList<String> languages = new ArrayList<>();
//        languages.add("Java");
//        request.setLanguages(languages);
//        request.setTitle("테스트 풀이글");
//        request.setContent("테스트 풀이글 내용");
//        Long createdSolutionId = solutionService.createSolution("test1@test.com", createdProblemId, request);
//
//        // when
//        SolutionResponse solutionResponse = solutionService.readSolution("test2@test.com", createdProblemId, createdSolutionId);
//        solutionService.removeSolution("test1@test.com", createdProblemId, createdSolutionId);
//
//        // then
//        Long viewCount = solutionViewService.countBySolutionId(createdSolutionId);
//        assertThat(viewCount).isEqualTo(0);
//    }

    private void testSignUp(String email, String password, String nickname, String role) {
        authService.signup(MemberDto.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .role(role)
                .build());
    }
}
