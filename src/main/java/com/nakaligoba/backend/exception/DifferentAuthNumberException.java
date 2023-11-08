package com.nakaligoba.backend.exception;

public class DifferentAuthNumberException extends RuntimeException {
    public DifferentAuthNumberException() {
        super("인증번호가 일치하지 않습니다");
    }
}
