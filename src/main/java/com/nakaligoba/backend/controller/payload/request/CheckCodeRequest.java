package com.nakaligoba.backend.controller.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CheckCodeRequest {
    @NotBlank
    private final String language;
    @NotBlank
    private final String code;
}
