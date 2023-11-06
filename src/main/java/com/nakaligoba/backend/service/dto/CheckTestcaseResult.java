package com.nakaligoba.backend.service.dto;

import com.nakaligoba.backend.service.dto.InputDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CheckTestcaseResult {
    private final Integer number;
    private final Boolean isAnswer;
    private final List<InputDto> inputs;
    private final String output;
    private final String expected;
}
