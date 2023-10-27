package com.nakaligoba.backend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class RunFileServiceTest {
    @Test
    @DisplayName("S3에 저장된 파일을 실행시켜 결과를 얻을 수 있다.")
    void givenSavedFile_whenRun_thenReturnResult() {

    }
}
