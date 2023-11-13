package com.nakaligoba.backend.controller.payload.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReplyLikeRequest {
    private LocalDateTime requestDateTime;
}
