package com.nakaligoba.backend.controller.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ReplyRequest {

    @NotBlank
    private String content;
}
