package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.acceptance.fixtures.ProblemFixture;
import com.nakaligoba.backend.controller.payload.request.CommentRequest;
import com.nakaligoba.backend.controller.payload.request.ReplyRequest;
import com.nakaligoba.backend.controller.payload.request.SolutionRequest;
import com.nakaligoba.backend.controller.payload.response.RepliesResponse;
import com.nakaligoba.backend.domain.ProgrammingLanguage;
import com.nakaligoba.backend.domain.Reply;
import com.nakaligoba.backend.exception.PermissionDeniedException;
import com.nakaligoba.backend.repository.ProgrammingLanguageRepository;
import com.nakaligoba.backend.repository.ReplyRepository;
import com.nakaligoba.backend.service.dto.MemberDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReplyServiceTest {

    private static final String testContent1 = "어떻게해야 잘할 수 있을까요? ㅜㅜ";
    private static final String testReply1 = "하루에 한문제씩 꾸준히 푸시다보면 잘할 수 있습니다!";
    private static final String testReply2 = "감사합니다! 꾸준함의 힘!";
    private static final String testReply3 = "저도 감이 안잡혔는데,저도 일단 꾸준히 해보고 판단해야겠습니다!!";


    @Autowired
    private SolutionService solutionService;

    @Autowired
    private ProblemFacade problemFacade;

    @Autowired
    private AuthService authService;

    @Autowired
    private ProgrammingLanguageRepository programmingLanguageRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private ReplyRepository replyRepository;

    private Long createdProblemId;
    private Long createdSolutionId;
    private Long createdCommentId;

    @BeforeEach
    void beforeEach() {
        // 회원 가입
        testSignUp("test1@test.com", "1234", "gd1", "admin");
        testSignUp("test2@test.com", "1234", "gd2", "");
        testSignUp("test3@test.com", "1234", "gd3", "");

        // 문제 생성
        createdProblemId = problemFacade.createProblem(ProblemFixture.CREATE_PROBLEM_REQUEST);
        programmingLanguageRepository.save(new ProgrammingLanguage("JAVA"));

        ArrayList<String> languages = new ArrayList<>();
        languages.add("java");
        SolutionRequest solutionRequest = new SolutionRequest();
        solutionRequest.setTitle("풀이글 제목 테스트1");
        solutionRequest.setContent("풀이글 내용 테스트1");
        solutionRequest.setLanguages(languages);

        // 풀이 생성
        createdSolutionId = solutionService.createSolution("test1@test.com", createdProblemId, solutionRequest);

        // 댓글 생성
        createdCommentId = commentService.createComment("test2@test.com", createdSolutionId, getTestCommentRequest(testContent1));

    }

    @Test
    @DisplayName("특정 댓글에 여러 대댓글을 작성할 수 있다.")
    void createReply() {
        //given
        Long reply1 = replyService.createReply("test1@test.com", createdCommentId, getTestReplyRequest(testReply1));
        Long reply2 = replyService.createReply("test2@test.com", createdCommentId, getTestReplyRequest(testReply2));
        Long reply3 = replyService.createReply("test3@test.com", createdCommentId, getTestReplyRequest(testReply3));

        // when
        assertNotNull(reply1);
        assertNotNull(reply2);
        assertNotNull(reply3);

        Reply byId1 = replyRepository.findById(reply1).orElseThrow();
        Reply byId2 = replyRepository.findById(reply2).orElseThrow();
        Reply byId3 = replyRepository.findById(reply3).orElseThrow();

        //then
        assertEquals(1L, byId1.getId());
        assertEquals(2L, byId2.getId());
        assertEquals(3L, byId3.getId());
    }

    @Test
    @DisplayName("본인이 작성한 대댓글을 본인만이 수정가능하고, 수정된 대댓글로 조회된다. ")
    void updateReply() {
        //given
        Long reply1 = replyService.createReply("test1@test.com", createdCommentId, getTestReplyRequest(testReply1));

        // when
        String updatedReplyContent = "하루에 두 문제씩은 꾸준히 푸시죠!";
        replyService.updateReply("test1@test.com",createdCommentId ,reply1, getTestReplyRequest(updatedReplyContent));

        String deniedReplyContent = "불특정 유저가 맘대로 불특정 대댓글 내용 바꾸기 시도";

        //then
        try {
            replyService.updateReply("test2@test.com",createdCommentId ,reply1, getTestReplyRequest(deniedReplyContent));
        } catch (PermissionDeniedException e) {
            Reply updatedReply = replyRepository.findById(reply1).orElseThrow();
            assertNotEquals(deniedReplyContent, updatedReply.getContent());
        }

        Reply updatedReply = replyRepository.findById(reply1).orElseThrow();
        assertEquals(updatedReplyContent, updatedReply.getContent());
    }

    @Test
    @DisplayName("본인이 작성한 대댓글을 본인만이 삭제 가능하고, 삭제 후 해당 대댓글은 조회되지 않는다.")
    void deleteReply() {
        //given
        Long reply1 = replyService.createReply("test1@test.com", createdCommentId, getTestReplyRequest(testReply1));
        Long reply2 = replyService.createReply("test2@test.com", createdCommentId, getTestReplyRequest(testReply2));
        Long reply3 = replyService.createReply("test3@test.com", createdCommentId, getTestReplyRequest(testReply3));

        // when
        replyService.deleteReply("test3@test.com", createdCommentId, reply3);

        //then
        try {
            replyService.deleteReply("test2@test.com",createdCommentId ,reply1);
        } catch (PermissionDeniedException e) {
            Optional<Reply> existedReply = replyRepository.findById(reply1);
            assertThat(existedReply).isNotEmpty();
        }

        Optional<Reply> removedReply = replyRepository.findById(reply3);
        assertThat(removedReply).isEmpty();
    }

    @Test
    @DisplayName("답글 조회 시 최근에 작성된 답글이 밑으로 가도록 정렬하여 조회한다.")
    void readReply_sortByCreatedDate() {
        // given
        Long reply1 = replyService.createReply("test1@test.com", createdCommentId, getTestReplyRequest(testReply1));
        Long reply2 = replyService.createReply("test2@test.com", createdCommentId, getTestReplyRequest(testReply2));
        Long reply3 = replyService.createReply("test3@test.com", createdCommentId, getTestReplyRequest(testReply3));

        // when
        RepliesResponse repliesResponse = replyService.readReplies("test3@test.com", createdCommentId);

        // then
        assertThat(repliesResponse.getReplies().get(0).getContent()).isEqualTo(testReply1);
        assertThat(repliesResponse.getReplies().get(1).getContent()).isEqualTo(testReply2);
        assertThat(repliesResponse.getReplies().get(2).getContent()).isEqualTo(testReply3);
    }

    private void testSignUp(String email, String password, String nickname, String role) {
        authService.signup(MemberDto.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .role(role)
                .build());
    }

    private CommentRequest getTestCommentRequest(String testContent) {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setContent(testContent);

        return commentRequest;
    }

    private ReplyRequest getTestReplyRequest(String testReply) {
        ReplyRequest replyRequest = new ReplyRequest();
        replyRequest.setContent(testReply);

        return replyRequest;
    }
}
