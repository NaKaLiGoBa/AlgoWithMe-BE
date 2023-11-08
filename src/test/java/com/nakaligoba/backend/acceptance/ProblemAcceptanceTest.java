package com.nakaligoba.backend.acceptance;

import com.nakaligoba.backend.acceptance.fixtures.ProblemFixture;
import com.nakaligoba.backend.controller.payload.request.CreateProblemRequest;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class ProblemAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("문제 식별자로 문제 설명을 볼 수 있다.")
    void givenProblemId_whenRequest_thenProblemData() {
        String problemLocation = ProblemFixture.createProblem()
                .header(HttpHeaders.LOCATION);

        given()
                .log().all()
        .when()
                .get(problemLocation)
        .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("문제 생성")
    @WithMockUser
    void createProblem() {
        given()
                .log().all()
                .body(ProblemFixture.CREATE_PROBLEM_REQUEST)
                .contentType(ContentType.JSON)
        .when()
                .post("/api/v1/problems")
        .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", Matchers.notNullValue());
    }

    @Test
    @DisplayName("문제 생성 시 태그는 비어있을 수 있다.")
    @WithMockUser
    void createProblemWithEmptyTags() {
        CreateProblemRequest requestBody = new CreateProblemRequest(
                "겁나 어려운 문제",
                "어려움",
                "# 설명\n매우 어려운 설명\n## 소제목",
                Arrays.asList("a", "b"),
                "1 2\n3",
                "3 4\n7",
                List.of()
        );

        given()
                .log().all()
                .body(requestBody)
                .contentType(ContentType.JSON)
        .when()
                .post("/api/v1/problems")
        .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", Matchers.notNullValue());
    }

    @Test
    @DisplayName("문제 생성 시 제목, 난이도, 설명, 테스트케이스, 정답케이스는 반드시 작성해야한다.")
    @WithMockUser
    void createProblemWithoutTitle_fail() {
        CreateProblemRequest requestBody = new CreateProblemRequest(
                "",
                "",
                "",
                List.of(),
                "",
                "",
                List.of()
        );

        given()
                .log().all()
                .body(requestBody)
                .contentType(ContentType.JSON)
        .when()
                .post("/api/v1/problems")
        .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
