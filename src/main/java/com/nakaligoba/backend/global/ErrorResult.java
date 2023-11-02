package com.nakaligoba.backend.global;

import lombok.Data;

@Data
public class ErrorResult {
    private String message;
    private String code;

    public ErrorResult(String message) {
        this.message = message;
    }
}
