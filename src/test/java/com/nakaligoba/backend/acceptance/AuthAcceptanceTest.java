package com.nakaligoba.backend.acceptance;

import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasKey;

public class AuthAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("이메일과 비밀번호와 닉네임으로 회원가입을 할 수 있다.")
    void givenEmailAndPasswordAndNickName_whenRegister_thenOK() throws JSONException {
        String email = "test@test.com";
        String password = "test123";
        String nickname = "nickname";
        JSONObject requestBody = new JSONObject();
        requestBody.put("email", email);
        requestBody.put("password", password);
        requestBody.put("nickname", nickname);

        given()
                .log().all()
                .body(requestBody.toString())
                .contentType(ContentType.JSON)
        .when()
                .post("/api/v1/auth/signup")
        .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("$", hasKey("message"));
    }

    @Test
    @DisplayName("잘못된 이메일 형식으로 회원가입을 할 수 없다.")
    void givenInvalidEmail_whenRegister_thenBadRequest() throws JSONException {
        String email = "test";
        String password = "test123";
        String nickname = "nickname";
        JSONObject requestBody = new JSONObject();
        requestBody.put("email", email);
        requestBody.put("password", password);
        requestBody.put("nickname", nickname);

        given()
                .log().all()
                .body(requestBody.toString())
                .contentType(ContentType.JSON)
        .when()
                .post("/api/v1/auth/signup")
        .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
