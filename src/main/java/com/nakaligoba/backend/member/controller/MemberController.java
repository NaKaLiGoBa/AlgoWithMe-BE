package com.nakaligoba.backend.member.controller;

import com.nakaligoba.backend.member.application.MemberService;
import com.nakaligoba.backend.member.application.dto.*;
import com.nakaligoba.backend.member.controller.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest request) {
        MemberDto memberDto = MemberDto.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .nickname(request.getNickname())
                .build();

        String signUpResult = memberService.signup(memberDto);

        if (SignupResponse.DUPLICATE_EMAIL.equals(signUpResult)) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new SignupResponse("이미 사용 중입니다. 다른 이메일을 입력해주세요."));
        } else if (SignupResponse.DUPLICATE_NICKNAME.equals(signUpResult)) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new SignupResponse("닉네임 중복"));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new SignupResponse("회원가입이 완료되었습니다."));
        }
    }

    @PostMapping("/signin/kakao")
    public ResponseEntity<SigninResponse> kakaoSignin(@Valid @RequestBody KakaoSigninRequest request) {
        SigninResponse signinResponse = memberService.kakaoSignin(request.getAuthCode());

        if (StringUtils.hasText(signinResponse.getAccessToken())) {
            return ResponseEntity.status(HttpStatus.OK).body(signinResponse);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(signinResponse);
        }
    }

    @PostMapping("/email")
    public ResponseEntity<EmailAuthResponse> authEmail(@Valid @RequestBody EmailAuthRequest request) {
        AuthEmailDto authEmailDto = AuthEmailDto.builder()
                .email(request.getEmail())
                .build();

        if (memberService.authEmail(authEmailDto)) {
            return ResponseEntity.status(HttpStatus.OK).body(new EmailAuthResponse("인증번호를 전송하였습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new EmailAuthResponse("이미 사용 중입니다. 다른 이메일을 입력해주세요."));
        }
    }

    @PostMapping("/email/check")
    public ResponseEntity<EmailAuthCheckResponse> authEmailCheck(@Valid @RequestBody EmailAuthCheckRequest request) {
        AuthEmailCheckDto authEmailCheckDto = AuthEmailCheckDto.builder()
                .email(request.getEmail())
                .authNumber(request.getAuthNumber())
                .build();

        if (memberService.authEmailCheck(authEmailCheckDto)) {
            return ResponseEntity.status(HttpStatus.OK).body(new EmailAuthCheckResponse("인증에 성공하였습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new EmailAuthCheckResponse("인증에 실패하였습니다."));
        }
    }

    @PostMapping("/password/reset/email")
    public ResponseEntity<PasswordResetResponse> passwordReset(@Valid @RequestBody PasswordResetRequest request) {
        PasswordResetDto passwordResetDto = PasswordResetDto.builder()
                .email(request.getEmail())
                .build();
        memberService.passwordReset(passwordResetDto);

        return ResponseEntity.status(HttpStatus.OK).body(new PasswordResetResponse("비밀번호 재설정을 위한 이메일이 전송되었습니다."));
    }

    @GetMapping("/password/reset/email/{token}")
    public ResponseEntity<?> passwordResetAuth(@PathVariable("token") String token) throws IOException {
        String resetPageUrl = "";
        PasswordResetAuthDto passwordResetAuthDto = PasswordResetAuthDto.builder()
                .token(token)
                .build();

        if (memberService.passwordResetAuth(passwordResetAuthDto)) {
            resetPageUrl = "https://k08e0a348244ea.user-app.krampoline.com/password/reset?token=" + token;
        } else {
            resetPageUrl = "https://k08e0a348244ea.user-app.krampoline.com/password";
        }

        return ResponseEntity.status(HttpStatus.FOUND).header("Location", resetPageUrl).build();
    }

    @PostMapping("/password/reset/check")
    public ResponseEntity<PasswordResetCheckResponse> passwordResetCheck(@Valid @RequestBody PasswordResetCheckRequest request) {
        PasswordResetCheckDto passwordResetCheckDto = PasswordResetCheckDto.builder()
                .newPassword(request.getNewPassword())
                .token(request.getToken())
                .build();

        if (memberService.passwordResetCheck(passwordResetCheckDto)) {
            return ResponseEntity.status(HttpStatus.OK).body(new PasswordResetCheckResponse("비밀번호 재설정이 완료되었습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PasswordResetCheckResponse("비밀번호 재설정에 실패하였습니다."));
        }
    }
}