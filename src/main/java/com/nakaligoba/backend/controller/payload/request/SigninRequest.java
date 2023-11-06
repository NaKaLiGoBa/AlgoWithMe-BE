package com.nakaligoba.backend.controller.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SigninRequest {
    @NotBlank
    private final String email;

    @NotBlank
    private final String password;
}
