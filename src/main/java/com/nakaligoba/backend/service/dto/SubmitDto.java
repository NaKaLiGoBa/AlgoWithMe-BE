package com.nakaligoba.backend.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubmitDto {
    private final Long id;
    private final String status;
    private final String language;
    private final String runtime;
    private final String memory;
    private final String timeComplexity;
    private final String spaceComplexity;
}
