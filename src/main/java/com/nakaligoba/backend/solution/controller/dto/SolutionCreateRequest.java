package com.nakaligoba.backend.solution.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

@Data
public class SolutionCreateRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private ArrayList<String> languages;
}
