package com.nakaligoba.backend.member.controller.dto;

import lombok.Data;

@Data
public class SignupResponse {
    public static final String COMPLETE_SIGNUP = "1";
    public static final String DUPLICATE_EMAIL = "2";
    public static final String DUPLICATE_NICKNAME = "3";

    private final String message;
}