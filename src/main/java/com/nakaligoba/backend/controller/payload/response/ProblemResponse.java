package com.nakaligoba.backend.controller.payload.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class ProblemResponse {
    private final int number;
    private final String title;
    private final String status;
    private final String acceptance;
    private final String difficulty;
    private final String description;
    private final Map<String, String> defaultCodes;
    private final List<TestcaseResponse> testcases;
    private final List<String> tags;
    private Long totalSolutionCount;
    private Long totalSubmitCount;
    private String easierProblemUrl;
    private String harderProblemUrl;

    @Data
    @Builder
    public static class TestcaseResponse {
        private final int number;
        private final List<InputResponse> inputs;
        private final String expected;
    }

    @Data
    @Builder
    public static class InputResponse {
        private final String name;
        private final String value;
    }
}
