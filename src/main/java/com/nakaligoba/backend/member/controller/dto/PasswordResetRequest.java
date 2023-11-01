package com.nakaligoba.backend.member.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class PasswordResetRequest {
    @NotBlank
    private String email;
}