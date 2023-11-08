package com.nakaligoba.backend.controller.payload.response;

import com.nakaligoba.backend.service.dto.AuthorDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class SolutionResponse {
    private AuthorDto author;
    private SolutionData solution;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SolutionData {
        private String title;
        private String createdAt;
        private String content;
        private List<String> languages;
    }
}
