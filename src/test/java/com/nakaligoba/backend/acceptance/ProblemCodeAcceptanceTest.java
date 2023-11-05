package com.nakaligoba.backend.acceptance;

import com.nakaligoba.backend.acceptance.fixtures.ProblemFixture;
import com.nakaligoba.backend.problem.controller.dto.CheckCodeRequest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class ProblemCodeAcceptanceTest extends AcceptanceTest {

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
}
