package com.nakaligoba.backend.problem.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SubmitResponse {
    @JsonProperty("isAnswer")
    private final boolean isAnswer;
}
