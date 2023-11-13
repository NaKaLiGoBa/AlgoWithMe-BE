package com.nakaligoba.backend.controller.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReplyLikeResponse {
    private boolean isLike;
}
