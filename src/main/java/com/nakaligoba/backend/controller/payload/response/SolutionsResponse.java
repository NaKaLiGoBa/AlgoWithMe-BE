package com.nakaligoba.backend.controller.payload.response;

import com.nakaligoba.backend.service.dto.AuthorDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class SolutionsResponse {
    public static final int READ_SOLUTIONS_INIT = -100;
    public static final int READ_SOLUTIONS_EMPTY = -1;

    private final Long totalCount;
    private final List<Solutions> solutions;
    private final Link _link;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Solutions {
        private AuthorDto author;
        private SolutionsData solution;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SolutionsData {
        private Long id;
        private String title;
        private List<String> languages;
        private Long likeCount;
        private Long viewCount;
        private Long commentCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Link {
        private Long nextCursor;
    }
}
