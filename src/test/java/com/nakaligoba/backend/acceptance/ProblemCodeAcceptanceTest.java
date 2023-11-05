package com.nakaligoba.backend.acceptance;

import com.nakaligoba.backend.acceptance.fixtures.MemberFixture;
import com.nakaligoba.backend.acceptance.fixtures.ProblemFixture;
import com.nakaligoba.backend.problem.controller.dto.CheckCodeRequest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ProblemCodeAcceptanceTest extends AcceptanceTest {

    @BeforeAll
    static void init() {
        MemberFixture.signup("test@test.com", "password123", "nick");
    }

    @Test
    @DisplayName("문제 식별자, 프로그래밍 언어, 코드로 해당 문제의 테스트케이스들의 성공 실패 여부를 알 수 있다.")
    void testcaseCheck() {
        String location = ProblemFixture.createProblem()
                .response()
                .getHeader(HttpHeaders.LOCATION);
        String language = "Java";
        String code = "class Solution {\npublic String solve(String a, String b) {\n        return String.valueOf(Integer.parseInt(a) + Integer.parseInt(b));\n    }\n}";

        CheckCodeRequest requestBody = new CheckCodeRequest(language, code);

        given()
                .log().all()
                .body(requestBody)
                .contentType(ContentType.JSON)
        .when()
                .post(location + "/code/test")
        .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("코드에 문법적 오류가 있다면 테스트 시 컴파일 에러가 발생한다.")
    void testcaseCheck_compileError() {
        String token = MemberFixture.signinAndGetToken("test@test.com", "password123");
        String location = ProblemFixture.createProblem()
                .response()
                .getHeader(HttpHeaders.LOCATION);
        String language = "Java";
        String code = "class Solution {\npublic Strin solve(String a, String b) {\n        return String.valueOf(Integer.parseInt(a) + Integer.parseInt(b));\n    }\n}";

        CheckCodeRequest requestBody = new CheckCodeRequest(language, code);

        given()
                .log().all()
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(requestBody)
                .contentType(ContentType.JSON)
                .when()
                .post(location + "/code/test")
                .then()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("type", equalTo("Compile Error"));
    }

    @Test
    @DisplayName("문제 식별자, 프로그래밍 언어, 코드로 해당 문제 통과 여부를 알 수 있다.")
    void answerCaseCheck() {
        String token = MemberFixture.signinAndGetToken("test@test.com", "password123");
        String location = ProblemFixture.createProblem()
                .response()
                .getHeader(HttpHeaders.LOCATION);
        String language = "Java";
        String code = "class Solution {\npublic String solve(String a, String b) {\n        return String.valueOf(Integer.parseInt(a) + Integer.parseInt(b));\n    }\n}";

        CheckCodeRequest requestBody = new CheckCodeRequest(language, code);

        given()
                .log().all()
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(requestBody)
                .contentType(ContentType.JSON)
        .when()
                .post(location + "/code/submit")
        .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("isAnswer", equalTo(true));
    }

    @Test
    @DisplayName("코드에 문법적 오류가 있다면 오답으로 처리된다.")
    void answerCaseCheck_compileError() {
        String token = MemberFixture.signinAndGetToken("test@test.com", "password123");
        String location = ProblemFixture.createProblem()
                .response()
                .getHeader(HttpHeaders.LOCATION);
        String language = "Java";
        String code = "class Solution {\npublic Strin solve(String a, String b) {\n        return String.valueOf(Integer.parseInt(a) + Integer.parseInt(b));\n    }\n}";

        CheckCodeRequest requestBody = new CheckCodeRequest(language, code);

        given()
                .log().all()
                .header(HttpHeaders.AUTHORIZATION, token)
                .body(requestBody)
                .contentType(ContentType.JSON)
        .when()
                .post(location + "/code/submit")
        .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .body("isAnswer", equalTo(false));
    }
}
