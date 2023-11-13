package com.nakaligoba.backend.exception;

import com.nakaligoba.backend.domain.Member;

public class PermissionDeniedException extends RuntimeException {

    private final Member deniedMember;

    public PermissionDeniedException(Member deniedMember) {
        super("접근 권한이 없습니다.");
        this.deniedMember = deniedMember;
    }

    public Member getDeniedMember() {
        return deniedMember;
    }
}
