package com.nakaligoba.backend.problem.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CheckTestcaseRequest {
    @NotBlank
    private final String language;
    @NotBlank
    private final String code;
}
