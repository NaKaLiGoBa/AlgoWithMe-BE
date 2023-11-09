package com.nakaligoba.backend.exception;

public class PermissionDeniedException extends RuntimeException{
    public PermissionDeniedException() {
        super("접근 권한이 없습니다.");
    }
}
