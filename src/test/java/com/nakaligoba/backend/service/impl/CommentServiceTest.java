package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.acceptance.fixtures.ProblemFixture;
import com.nakaligoba.backend.controller.payload.request.CommentRequest;
import com.nakaligoba.backend.controller.payload.request.SolutionRequest;
import com.nakaligoba.backend.controller.payload.response.CommentsResponse;
import com.nakaligoba.backend.domain.ProgrammingLanguage;
import com.nakaligoba.backend.exception.PermissionDeniedException;
import com.nakaligoba.backend.repository.ProgrammingLanguageRepository;
import com.nakaligoba.backend.service.dto.MemberDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@Transactional
@SpringBootTest
class CommentServiceTest {

    private static final String testContent1 = "좋은 풀이 감사합니다!";
    private static final String testContent2 = "저도 더 열심히 해야겠네요..";
    private static final String testContent3 = "대단하십니다!";

    @Autowired
    private ProblemFacade problemFacade;

    @Autowired
    private SolutionService solutionService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentLikeService commentLikeService;

    @Autowired
    private AuthService authService;

    @Autowired
    private ProgrammingLanguageRepository programmingLanguageRepository;

    private Long createdProblemId;
    private Long createdSolutionId;

    @BeforeEach
    void beforeEach() {
        // 회원가입
        testSignUp("nakaligoba@gmail.com", "1234567", "nakaligoba", "");
        testSignUp("tnh3113@gmail.com", "123456", "tnh3113", "");

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
        createdSolutionId = solutionService.createSolution("nakaligoba@gmail.com", createdProblemId, solutionRequest);
    }

    @Test
    @DisplayName("특정 풀이에 작성된 댓글의 내용 수정 후 재 조회 시 수정된 댓글 내용으로 조회된다.")
    void updateComment() {
        // given
        commentService.createComment("tnh3113@gmail.com", createdSolutionId, getTestCommentRequest(testContent1));
        commentService.createComment("tnh3113@gmail.com", createdSolutionId, getTestCommentRequest(testContent2));

        // when
        Pageable pageable = PageRequest.of(0, 2);
        CommentsResponse commentsResponse = commentService.readComments("tnh3113@gmail.com", createdSolutionId, pageable, "recent");

        String updatedContent = "너무 어렵습니다ㅠ";
        commentService.updateComment("tnh3113@gmail.com", commentsResponse.getComments().get(0).getComment().getId(), getTestCommentRequest(updatedContent));

        // then
        CommentsResponse updatedCommentsResponse = commentService.readComments("tnh3113@gmail.com", createdSolutionId, pageable, "recent");
        assertThat(updatedCommentsResponse.getComments().get(0).getComment().getContent()).isEqualTo(updatedContent);
    }

    @Test
    @DisplayName("특정 풀이에 대한 댓글 생성 후 작성자가 아닌 다른 사람은 작성된 댓글을 수정할 수 없다.")
    void updateCommentByOther() {
        // given
        Long commentId = commentService.createComment("tnh3113@gmail.com", createdSolutionId, getTestCommentRequest(testContent1));

        // when
        assertThrows(PermissionDeniedException.class, () -> {
            commentService.updateComment("nakaligoba@gmail.com", commentId, getTestCommentRequest("내용 수정"));
        });
    }

    @Test
    @DisplayName("특정 풀이에 특정 댓글을 삭제 시 조회되지 않는다.")
    void deleteComment() {
        // given
        commentService.createComment("tnh3113@gmail.com", createdSolutionId, getTestCommentRequest(testContent1));
        commentService.createComment("tnh3113@gmail.com", createdSolutionId, getTestCommentRequest(testContent2));

        // when
        Pageable pageable = PageRequest.of(0, 2);
        CommentsResponse commentsResponse = commentService.readComments("tnh3113@gmail.com", createdSolutionId, pageable, "recent");
        commentService.deleteComment("tnh3113@gmail.com", commentsResponse.getComments().get(0).getComment().getId());

        // then
        CommentsResponse deletedCommentsResponse = commentService.readComments("tnh3113@gmail.com", createdSolutionId, pageable, "recent");
        assertThat(deletedCommentsResponse.getComments().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("특정 풀이에 대한 댓글 생성 후 작성자가 아닌 다른 사람은 작성된 댓글을 삭제할 수 없다.")
    void deleteCommentByOther() {
        // given
        Long commentId = commentService.createComment("tnh3113@gmail.com", createdSolutionId, getTestCommentRequest(testContent1));

        // when
        assertThrows(PermissionDeniedException.class, () -> {
            commentService.deleteComment("nakaligoba@gmail.com", commentId);
        });
    }

    @Test
    @DisplayName("문제 식별자로 조회된 문제에 대한 풀이 글 작성 후 생성 일자 순으로 정렬하여 조회한다.")
    void readComments_sortByCreatedDate() {
        // given
        commentService.createComment("tnh3113@gmail.com", createdSolutionId, getTestCommentRequest(testContent1));
        commentService.createComment("tnh3113@gmail.com", createdSolutionId, getTestCommentRequest(testContent2));
        commentService.createComment("nakaligoba@gmail.com", createdSolutionId, getTestCommentRequest(testContent3));

        // when
        Pageable pageable = PageRequest.of(0, 3);
        CommentsResponse commentsResponse = commentService.readComments("tnh3113@gmail.com", createdSolutionId, pageable, "recent");

        // then
        assertThat(commentsResponse.getComments().get(0).getComment().getContent()).isEqualTo(testContent3);
        assertThat(commentsResponse.getComments().get(1).getComment().getContent()).isEqualTo(testContent2);
        assertThat(commentsResponse.getComments().get(2).getComment().getContent()).isEqualTo(testContent1);
    }

    @Test
    @DisplayName("문제 식별자로 조회된 문제에 대한 풀이 글 작성 후 좋아요 순으로 정렬하여 조회한다.")
    void readComments_sortByLike() {
        // given
        commentService.createComment("tnh3113@gmail.com", createdSolutionId, getTestCommentRequest(testContent1));
        commentService.createComment("tnh3113@gmail.com", createdSolutionId, getTestCommentRequest(testContent2));
        commentService.createComment("nakaligoba@gmail.com", createdSolutionId, getTestCommentRequest(testContent3));

        // when
        Pageable pageable = PageRequest.of(0, 3);
        CommentsResponse commentsResponse = commentService.readComments("tnh3113@gmail.com", createdSolutionId, pageable, "recent");

        commentLikeService.toggleLike("nakaligoba@gmail.com", createdSolutionId, commentsResponse.getComments().get(0).getComment().getId());
        commentLikeService.toggleLike("tnh3113@gmail.com", createdSolutionId, commentsResponse.getComments().get(1).getComment().getId());
        commentLikeService.toggleLike("nakaligoba@gmail.com", createdSolutionId, commentsResponse.getComments().get(1).getComment().getId());

        // then
        CommentsResponse commentsResponseSortByLike = commentService.readComments("tnh3113@gmail.com", createdSolutionId, pageable, "like");
        assertThat(commentsResponseSortByLike.getComments().get(0).getComment().getContent()).isEqualTo(testContent2);
        assertThat(commentsResponseSortByLike.getComments().get(1).getComment().getContent()).isEqualTo(testContent3);
        assertThat(commentsResponseSortByLike.getComments().get(2).getComment().getContent()).isEqualTo(testContent1);
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
}
