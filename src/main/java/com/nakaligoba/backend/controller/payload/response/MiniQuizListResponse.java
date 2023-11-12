package com.nakaligoba.backend.controller.payload.response;

import com.nakaligoba.backend.service.dto.MiniQuizDto;
import lombok.Data;

import java.util.List;

@Data
public class MiniQuizListResponse {
    private final Integer quizCount;
    private final List<MiniQuizDto> quizzes;
}
