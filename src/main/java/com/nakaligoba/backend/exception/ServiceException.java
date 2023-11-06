package com.nakaligoba.backend.exception;

public class ServiceException extends RuntimeException{

    private final String code;

    public ServiceException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
