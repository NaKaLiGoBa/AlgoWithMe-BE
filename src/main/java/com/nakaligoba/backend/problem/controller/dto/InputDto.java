package com.nakaligoba.backend.problem.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InputDto {
    private final String name;
    private final String value;
}
