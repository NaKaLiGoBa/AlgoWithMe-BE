package com.nakaligoba.backend.controller.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class CreateProblemRequest {
    @NotBlank
    private final String title;
    @NotBlank
    private final String difficulty;
    @NotBlank
    private final String description;
    @NotEmpty
    private final List<String> parameters;
    @NotBlank
    private final String testcases;
    @NotBlank
    private final String answerCases;
    private final List<String> tags;
}
