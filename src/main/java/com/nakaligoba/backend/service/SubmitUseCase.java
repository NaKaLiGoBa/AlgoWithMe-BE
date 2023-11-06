package com.nakaligoba.backend.service;

public interface SubmitUseCase {

    boolean isAnswer(String memberEmail, Long problemId, String language, String code);
}
