package com.nakaligoba.backend.problem.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DifficultyTest {

    @Test
    @DisplayName("한글 난이도로 Difficulty 객체 찾기")
    void getByKorean() {
        String korean = "쉬움";

        Difficulty difficulty = Difficulty.getByKorean(korean);

        assertThat(difficulty).isEqualTo(Difficulty.EASY);
    }

    @Test
    @DisplayName("없는 한글 난이도로 Difficulty 객체 찾기 실패")
    void getByKorean_fail() {
        String korean = "초고난이도";

        assertThatThrownBy(() -> Difficulty.getByKorean(korean))
                .isInstanceOf(NoSuchElementException.class);
    }
}
