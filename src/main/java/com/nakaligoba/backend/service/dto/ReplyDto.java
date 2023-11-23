package com.nakaligoba.backend.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDto {

    private Long replyId;
    private AuthorDto author;
    private String content;
    private Long likeCount;
    private boolean isLike;
}
