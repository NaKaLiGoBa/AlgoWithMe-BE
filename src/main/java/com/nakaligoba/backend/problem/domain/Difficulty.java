package com.nakaligoba.backend.problem.domain;

import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.NoSuchElementException;

public enum Difficulty {
    EASY("쉬움"),
    MEDIUM("중간"),
    HARD("어려움");

    private final String korean;

    Difficulty(String korean) {
        this.korean = korean;
    }

    public static Difficulty getByKorean(String korean) {
        return Arrays.stream(Difficulty.values())
                .filter(d -> d.korean.equals(korean))
                .findAny()
                .orElseThrow(NoSuchElementException::new);
    }

    public String getKorean() {
        return korean;
    }

}
