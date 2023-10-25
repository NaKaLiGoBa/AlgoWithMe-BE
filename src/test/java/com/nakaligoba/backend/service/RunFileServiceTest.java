package com.nakaligoba.backend.service;

import com.nakaligoba.backend.repository.FileRepository;
import com.nakaligoba.backend.repository.ProjectRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class RunFileServiceTest {

    @Autowired
    RunFileService runFileService;

    @MockBean
    FileRepository fileRepository;

    @MockBean
    ProjectRepository projectRepository;

    @Test
    @DisplayName("S3에 저장된 파일을 실행시켜 결과를 얻을 수 있다.")
    void givenSavedFile_whenRun_thenReturnResult() {

    }

}
