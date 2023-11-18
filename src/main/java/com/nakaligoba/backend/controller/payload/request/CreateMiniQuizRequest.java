package com.nakaligoba.backend.controller.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateMiniQuizRequest {
    @NotBlank
    private final String type;
    @NotBlank
    private final String answer;
    @NotBlank
    private final String explain;
    @NotBlank
    private final String description;
    @NotNull
    private final List<String> choiceOrInitials;
    @NotEmpty
    private final List<String> tags;
    @NotBlank
    private final String difficulty;
}
