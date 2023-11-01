package com.nakaligoba.backend.member.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SignupRequest {
    @NotBlank
    private final String email;

    @NotBlank
    private final String password;

    @NotBlank
    private final String nickname;
}