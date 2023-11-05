package com.nakaligoba.backend.problem.application.usecase;

public interface SubmitUseCase {

    boolean isAnswer(String memberEmail, Long problemId, String language, String code);
}
