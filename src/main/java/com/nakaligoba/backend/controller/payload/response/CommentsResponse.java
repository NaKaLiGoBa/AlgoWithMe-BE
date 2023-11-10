package com.nakaligoba.backend.controller.payload.response;

import com.nakaligoba.backend.service.dto.AuthorDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class CommentsResponse {
    private final long totalCount;
    private final List<Comments> comments;
    private int pageNumber;
    private int totalPages;
    private int size;
    private int numberOfElements;
    private boolean first;
    private boolean last;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Comments {
        private AuthorDto author;
        private CommentsData comment;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentsData {
        private Long id;
        private String content;
        private Long likeCount;
        private boolean isLike;
    }
}
