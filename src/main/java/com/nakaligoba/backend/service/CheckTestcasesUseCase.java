package com.nakaligoba.backend.service;

import com.nakaligoba.backend.service.dto.CheckTestcaseResult;

import java.util.List;

public interface CheckTestcasesUseCase {
    List<CheckTestcaseResult> checkTestcases(Long problemId, String language, String code);
}
