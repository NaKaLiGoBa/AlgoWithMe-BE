package com.nakaligoba.backend.controller.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CoachRequest {
    @NotBlank private final String question;
    private final String code;
}
