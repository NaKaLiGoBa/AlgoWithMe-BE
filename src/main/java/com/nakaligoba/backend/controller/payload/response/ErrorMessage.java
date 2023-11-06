package com.nakaligoba.backend.controller.payload.response;

import lombok.Data;

@Data
public class ErrorMessage {
    private final String message;
    private final String code;
}
