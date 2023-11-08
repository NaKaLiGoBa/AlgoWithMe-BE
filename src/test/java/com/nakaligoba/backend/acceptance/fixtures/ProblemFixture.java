package com.nakaligoba.backend.acceptance.fixtures;

import com.nakaligoba.backend.controller.payload.request.CreateProblemRequest;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.Arrays;

import static io.restassured.RestAssured.given;

public class ProblemFixture {

    public final static CreateProblemRequest CREATE_PROBLEM_REQUEST = new CreateProblemRequest("겁나 어려운 문제",
            "어려움",
            "# 설명\n매우 어려운 설명\n## 소제목",
            Arrays.asList("a", "b"),
            "1 2\n3",
            "3 4\n7",
            Arrays.asList("DFS", "BFS"));

    public static ExtractableResponse<Response> createProblem() {
        return given()
                .log().all()
                .body(CREATE_PROBLEM_REQUEST)
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/problems")
                .then().log().all()
                .extract();
    }
}
