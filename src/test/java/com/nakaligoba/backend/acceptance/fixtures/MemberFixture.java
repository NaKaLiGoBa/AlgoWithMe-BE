package com.nakaligoba.backend.acceptance.fixtures;

import com.nakaligoba.backend.controller.payload.request.SigninRequest;
import com.nakaligoba.backend.controller.payload.response.SigninResponse;
import com.nakaligoba.backend.controller.payload.request.SignupRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class MemberFixture {

    public static ExtractableResponse<Response> signup(String email, String password, String nickname, String role) {
        SignupRequest request = new SignupRequest(email, password, nickname, role);
        return RestAssured.given()
                .log().all()
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/auth/signup")
                .then().log().all()
                .extract();
    }

    public static String signinAndGetToken(String email, String password) {
        SigninRequest request = new SigninRequest(email, password);
        return RestAssured.given()
                .log().all()
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/auth/signin")
                .then().log().all()
                .extract()
                .as(SigninResponse.class)
                .getAccessToken();
    }
}
