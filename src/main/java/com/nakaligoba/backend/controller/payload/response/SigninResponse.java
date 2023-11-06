package com.nakaligoba.backend.controller.payload.response;

import lombok.Data;

@Data
public class SigninResponse {
    private final String accessToken;
    private final String message;
    private final String email;
    private final String nickname;
}
