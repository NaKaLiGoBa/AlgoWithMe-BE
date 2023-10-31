package com.nakaligoba.backend.problem.controller.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private final List<Testcase> testcases;
    private final List<String> tags;

    @Data
    @Builder
    public static class Testcase {
        private final int number;
        private final List<InputDto> inputs;
        private final String expected;
    }

}
