package com.nakaligoba.backend.exception;

public class PasswordResetAuthFailException extends RuntimeException {
    public PasswordResetAuthFailException() {
        super("비밀번호 재설정을 위한 인증에 실패하였습니다.");
    }
}
