package com.nakaligoba.backend.problem.controller.dto;

import lombok.Data;

@Data
public class UserCodeErrorResponse {
    private final String message;
    private final String type;
}
