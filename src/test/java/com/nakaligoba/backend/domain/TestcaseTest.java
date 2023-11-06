package com.nakaligoba.backend.domain;

import com.nakaligoba.backend.domain.Testcase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TestcaseTest {

    @Test
    @DisplayName("파라미터 이름, 테스트케이스 입출력, 채점용인지로 테스트케이스 생성")
    void of() {
        Integer number = 1;
        String parameters = "a b result";
        String inputValues = "1 2";
        String output = "3";
        boolean isGrading = false;

        Testcase testcase = Testcase.builder()
                .number(number)
                .inputNames(parameters)
                .inputValues(inputValues)
                .output(output)
                .isGrading(isGrading)
                .build();


        assertThat(testcase.getId()).isNull();
        assertThat(testcase.getInputNames()).isEqualTo("a b result");
        assertThat(testcase.getInputValues()).isEqualTo(inputValues);
        assertThat(testcase.getOutput()).isEqualTo(output);
    }
}
