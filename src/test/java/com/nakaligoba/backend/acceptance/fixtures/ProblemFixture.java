package com.nakaligoba.backend.acceptance.fixtures;

import com.nakaligoba.backend.controller.payload.request.CreateProblemRequest;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpHeaders;

import java.util.Arrays;

import static io.restassured.RestAssured.given;

public class ProblemFixture {

    public final static CreateProblemRequest CREATE_PROBLEM_REQUEST = new CreateProblemRequest("겁나 어려운 문제",
            "어려움",
            "## 문제\n" +
                    "\n" +
                    "N개의 정수로 이루어진 수열이 있을 때,\n" +
                    "크기가 양수인 부분수열 중에서 그 수열의 원소를 다 더한 값이 S가 되는 경우의 수를 구하는 프로그램을 작성하시오.\n" +
                    "\n" +
                    "## 입력\n" +
                    "\n" +
                    "첫째 줄에 정수의 개수를 나타내는 N과 정수 S가 주어진다.\n" +
                    "(1 ≤ N ≤ 20, |S| ≤ 1,000,000) 둘째 줄에 N개의 정수가 빈 칸을 사이에 두고 주어진다.\n" +
                    "주어지는 정수의 절댓값은 100,000을 넘지 않는다.\n" +
                    "\n" +
                    "## 출력\n" +
                    "\n" +
                    "첫째 줄에 합이 S가 되는 부분수열의 개수를 출력한다.\n" +
                    "\n" +
                    "## 예제1:\n" +
                    "> 입력: 5 0 [-7, -3, -2, 5, 8]\n" +
                    "> 출력: 1",
            Arrays.asList("a", "b"),
            "1 2\n3",
            "3 4\n7",
            Arrays.asList("DFS", "BFS"));

    public final static CreateProblemRequest CREATE_PROBLEM_REQUEST_DFS_EASY = new CreateProblemRequest("쉬운 DFS 문제",
            "쉬움",
            "# 설명\\n#쉬운 난이도의 DFS 문제 설명\\n## 소제목",
            Arrays.asList("a", "b"),
            "1 2\n3",
            "3 4\n7",
            Arrays.asList("DFS"));

    public final static CreateProblemRequest CREATE_PROBLEM_REQUEST_DFS_MEDIUM = new CreateProblemRequest("보통 난이도 DFS 문제",
            "보통",
            "# 설명\\n#보통 난이도의 DFS 문제 설명\\n## 소제목",
            Arrays.asList("a", "b"),
            "1 2\n3",
            "3 4\n7",
            Arrays.asList("DFS"));

    public final static CreateProblemRequest CREATE_PROBLEM_REQUEST_DFS_HARD = new CreateProblemRequest("어려운 DFS 문제",
            "어려움",
            "# 설명\\n#어려운 난이도의 DFS 문제 설명\\n## 소제목",
            Arrays.asList("a", "b"),
            "1 2\n3",
            "3 4\n7",
            Arrays.asList("DFS"));

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

    public static ExtractableResponse<Response> createProblemByAdmin(String adminToken) {
        Header header = new Header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken);
        return given()
                .log().all()
                .body(CREATE_PROBLEM_REQUEST)
                .header(header)
                .contentType(ContentType.JSON)
                .when()
                .post("/api/v1/problems")
                .then().log().all()
                .extract();
    }
}
