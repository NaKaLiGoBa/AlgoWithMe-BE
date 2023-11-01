package com.nakaligoba.backend.testcase.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TestcaseTest {

    @Test
    @DisplayName("파라미터 이름, 테스트케이스 입출력, 채점용인지로 테스트케이스 생성")
    void of() {
        List<String> parameters = List.of("a", "b", "result");
        String inputAndOutput = "1 2\n3";
        boolean isGrading = false;

        Testcase testcase = Testcase.of(parameters, inputAndOutput, isGrading);

        assertThat(testcase.getId()).isNull();
        assertThat(testcase.getInputNames()).isEqualTo("a b result");
        assertThat(testcase.getInputValues()).isEqualTo("1 2");
        assertThat(testcase.getOutput()).isEqualTo("3");
    }
}
