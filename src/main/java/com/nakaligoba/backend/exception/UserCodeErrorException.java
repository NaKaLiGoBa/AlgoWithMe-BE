package com.nakaligoba.backend.exception;

public class UserCodeErrorException extends RuntimeException {
    public UserCodeErrorException(String message) {
        super(message);
    }
}
