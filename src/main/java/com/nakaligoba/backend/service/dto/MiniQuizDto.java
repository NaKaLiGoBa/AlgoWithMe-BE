package com.nakaligoba.backend.service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MiniQuizDto {
    private final String type;
    private final String answer;
    private final String explain;
    private final String description;
    private final List<String> choiceOrInitials;
}
