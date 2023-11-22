package com.nakaligoba.backend.domain;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum Result {
    RESOLVED("성공"),
    FAIL("실패"),
    COMPILE_ERROR("컴파일 에러"),
    RUNTIME_ERROR("런타임 에러"),
    UN_RESOLVED("미해결");

    private final String korean;

    public static Result isResolved(boolean isAnswer) {
        return isAnswer ? Result.RESOLVED : Result.FAIL;
    }

    Result(String korean) {
        this.korean = korean;
    }

    public static Result getByKorean(String korean) {
        return Arrays.stream(Result.values())
                .filter(d -> d.korean.equals(korean))
                .findAny()
                .orElseThrow(NoSuchElementException::new);
    }

    public String getKorean() {
        return korean;
    }
}
