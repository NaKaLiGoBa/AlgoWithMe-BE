package com.nakaligoba.backend.exception;

public class DuplicateEmailException extends SignUpException {
    public DuplicateEmailException() {
        super("이미 사용 중입니다. 다른 이메일을 입력해주세요.", "100");
    }
}
