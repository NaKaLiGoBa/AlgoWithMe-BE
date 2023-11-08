package com.nakaligoba.backend.exception;

public class PasswordResetCheckFailException extends RuntimeException {
    public PasswordResetCheckFailException() {
        super("비밀번호 재설정에 실패하였습니다.");
    }
}
