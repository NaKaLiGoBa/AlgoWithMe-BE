package com.nakaligoba.backend.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProblemServiceTest {

    @Autowired
    private ProblemService problemService;

    @Autowired
    private ProblemFacade problemFacade;

    @Test
    @DisplayName("문제 식별자로 문제 정보를 읽을 수 있다.")
    void readByProblemId() {

    }
}
