package com.nakaligoba.backend.controller.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CheckTestcaseResponse {
    private final int number;
    @JsonProperty("isAnswer") private final boolean isAnswer;
    private final List<InputResponse> inputs;
    private final String output;
    private final String expected;
}
