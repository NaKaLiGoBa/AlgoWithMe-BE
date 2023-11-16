package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.exception.DuplicateEmailException;
import com.nakaligoba.backend.exception.DuplicateNicknameException;
import com.nakaligoba.backend.repository.MemberRepository;
import com.nakaligoba.backend.service.dto.MemberDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void beforeEach() {
        testSignUp("nakaligoba@gmail.com", "1234567", "nakaligoba", "");
    }

    @Test
    @DisplayName("회원가입 후 동일한 이메일로 회원가입 요청 시 회원가입은 불가능하다.")
    void signUpByDuplicateEmail() {
        assertThrows(DuplicateEmailException.class, () -> {
            testSignUp("nakaligoba@gmail.com", "123456", "hello", "");
        });
    }

    @Test
    @DisplayName("회원가입 후 동일한 닉네임으로 회원가입 요청 시 회원가입은 불가능하다.")
    void signUpByDuplicateNickname() {
        assertThrows(DuplicateNicknameException.class, () -> {
            testSignUp("tnh3113@gmail.com", "123456", "nakaligoba", "");
        });
    }

    private void testSignUp(String email, String password, String nickname, String role) {
        authService.signup(MemberDto.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .role(role)
                .build());
    }
}
