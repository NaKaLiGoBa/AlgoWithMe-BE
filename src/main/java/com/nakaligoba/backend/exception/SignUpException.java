package com.nakaligoba.backend.exception;

public class SignUpException extends ServiceException {
    public SignUpException(String message, String code) {
        super(message, code);
    }
}
