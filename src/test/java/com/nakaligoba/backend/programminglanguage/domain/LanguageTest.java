package com.nakaligoba.backend.programminglanguage.domain;

import com.nakaligoba.backend.domain.Language;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LanguageTest {

    @Test
    @DisplayName("인자 a, b가 주어졌을 때 생성되는 Java 기본 코드 생성")
    void defaultJavaCodeByArgumentAAndB() {
        String[] args = {"a", "b"};
        Language language = Language.JAVA;

        String defaultCode = language.getDefaultCode(args);

        assertThat(defaultCode).isEqualTo("class Solution {\n    public String solve(String a, String b) {\n        String answer = \"\";\n        return answer;\n    }\n}");
    }
}
