package com.nakaligoba.backend.exception;

public class AuthEmailFailException extends RuntimeException {
    public AuthEmailFailException(String message) {
        super(message);
    }
}
