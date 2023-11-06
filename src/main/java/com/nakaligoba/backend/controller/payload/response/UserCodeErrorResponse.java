package com.nakaligoba.backend.controller.payload.response;

import lombok.Data;

@Data
public class UserCodeErrorResponse {
    private final String message;
    private final String type;
}
