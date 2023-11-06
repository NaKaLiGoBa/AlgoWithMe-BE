package com.nakaligoba.backend.exception;

public class DuplicateNicknameException extends SignUpException{
    public DuplicateNicknameException() {
        super("이미 존재하는 닉네임입니다.", "200");
    }
}
