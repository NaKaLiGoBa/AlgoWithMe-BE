package com.nakaligoba.backend.service;

import com.nakaligoba.backend.service.dto.MemberDto;

public interface SignUpUseCase {
    void signup(MemberDto memberDto);
}
