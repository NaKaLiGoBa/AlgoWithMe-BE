package com.nakaligoba.backend.solution.controller.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class SolutionCreateRequest {
    private String title;
    private String content;
    private ArrayList<String> languages;
}
