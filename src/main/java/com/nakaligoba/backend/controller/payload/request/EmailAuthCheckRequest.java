package com.nakaligoba.backend.controller.payload.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class EmailAuthCheckRequest {
    @NotBlank
    private String email;

    @NotBlank
    private String authNumber;
}
