package com.nakaligoba.backend.problem.application.usecase;

import com.nakaligoba.backend.problem.application.dto.CheckTestcaseResult;

import java.util.List;

public interface CheckTestcasesUseCase {
    List<CheckTestcaseResult> checkTestcases(Long problemId, String language, String code);
}
