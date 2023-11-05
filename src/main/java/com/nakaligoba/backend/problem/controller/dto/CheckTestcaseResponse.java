package com.nakaligoba.backend.problem.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CheckTestcaseResponse {
    private final int number;
    private final boolean isAnswer;
    private final List<InputResponse> inputs;
    private final String output;
    private final String expected;
}
