package com.nakaligoba.backend.problem.exception;

public class UserCodeErrorException extends RuntimeException {
    public UserCodeErrorException(String message) {
        super(message);
    }
}
