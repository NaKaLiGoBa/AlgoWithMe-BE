package com.nakaligoba.backend.acceptance;

import com.nakaligoba.backend.acceptance.fixtures.MemberFixture;
import com.nakaligoba.backend.acceptance.fixtures.ProblemFixture;
import com.nakaligoba.backend.controller.payload.request.CoachRequest;
import com.nakaligoba.backend.repository.MemberRepository;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class AICoachAcceptanceTest extends AcceptanceTest {

//    private static String problemId;

//    @BeforeAll
//    static void setEntity() {
//        MemberFixture.signup("test@test.com", "password123", "nick", "");
//        problemId = ProblemFixture.createProblem()
//                .response()
//                .getHeader(HttpHeaders.LOCATION);
//    }

    @Autowired
    MemberRepository memberRepository;


    @Test
    @DisplayName("문제에 맞는 알고리즘 및 자료구조 선택")
    void algorithmSelection() {
        String token = MemberFixture.signinAndGetToken("admin@admin.com", "admin123");
        String problemId = ProblemFixture.createProblemByAdmin(token)
                .response()
                .getHeader(HttpHeaders.LOCATION);
        CoachRequest request = new CoachRequest("알고리즘 선택과 설계", "");
        Header header = new Header(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        given()
                .log().all()
                .body(request)
                .contentType(ContentType.JSON)
                .header(header)
        .when()
                .post("/api/v1/problems/" + problemId + "/coaches")
        .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }
}
