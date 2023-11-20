package com.nakaligoba.backend.controller.payload.response;

import com.nakaligoba.backend.service.dto.AuthorDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class CoachAnswersResponse {
    private Long answerCount;
    private List<AnswerInfo> answers;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerInfo {
        private String question;
        private String answer;
    }
}
