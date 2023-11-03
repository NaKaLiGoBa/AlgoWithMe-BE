package com.nakaligoba.backend.solution.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;

@Data
public class SolutionRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotEmpty
    private ArrayList<String> languages;
}
