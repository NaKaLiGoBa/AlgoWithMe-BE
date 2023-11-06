package com.nakaligoba.backend.controller.payload.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class KakaoSigninRequest {
    @NotBlank
    private String authCode;
}
