package com.nakaligoba.backend.controller.payload.response;

import com.nakaligoba.backend.service.dto.ReplyDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepliesResponse {
    private List<ReplyDto> replies;
    private long totalCount;
}