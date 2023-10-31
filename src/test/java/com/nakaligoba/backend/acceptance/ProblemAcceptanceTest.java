package com.nakaligoba.backend.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class ProblemAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("문제 식별자로 문제 설명을 볼 수 있다.")
    void givenProblemId_whenRequest_thenProblemData() {
        long problemId = 1L;

        given()
                .log().all()
        .when()
                .get("/api/v1/problems/" + problemId)
        .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }
}
