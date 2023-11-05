package com.nakaligoba.backend.submit.domain;

public enum Result {
    RESOLVED, FAIL, COMPILE_ERROR, RUNTIME_ERROR, UN_RESOLVED;

    public static Result isResolved(boolean isAnswer) {
        return isAnswer ? Result.RESOLVED : Result.UN_RESOLVED;
    }
}
