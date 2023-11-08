package com.nakaligoba.backend.controller;

import com.nakaligoba.backend.controller.payload.request.*;
import com.nakaligoba.backend.controller.payload.response.ErrorMessage;
import com.nakaligoba.backend.controller.payload.response.SigninResponse;
import com.nakaligoba.backend.exception.*;
import com.nakaligoba.backend.service.SignUpUseCase;
import com.nakaligoba.backend.service.dto.*;
import com.nakaligoba.backend.service.impl.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@RestController
public class MemberController {

    private final AuthService authService;
    private final SignUpUseCase signUpUseCase;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest request) {
        MemberDto memberDto = MemberDto.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .nickname(request.getNickname())
                .build();

        signUpUseCase.signup(memberDto);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/signin/kakao")
    public ResponseEntity<SigninResponse> kakaoSignin(@Valid @RequestBody KakaoSigninRequest request) {
        SigninResponse signinResponse = authService.kakaoSignin(request.getAuthCode());

        if (StringUtils.hasText(signinResponse.getAccessToken())) {
            return ResponseEntity.status(HttpStatus.OK).body(signinResponse);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(signinResponse);
        }
    }

    @PostMapping("/email")
    public ResponseEntity<Void> authEmail(@Valid @RequestBody EmailAuthRequest request) {
        AuthEmailDto authEmailDto = AuthEmailDto.builder()
                .email(request.getEmail())
                .build();

        authService.authEmail(authEmailDto);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/email/check")
    public ResponseEntity<Void> authEmailCheck(@Valid @RequestBody EmailAuthCheckRequest request) {
        AuthEmailCheckDto authEmailCheckDto = AuthEmailCheckDto.builder()
                .email(request.getEmail())
                .authNumber(request.getAuthNumber())
                .build();

        authService.authEmailCheck(authEmailCheckDto);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/password/reset/email")
    public ResponseEntity<Void> passwordReset(@Valid @RequestBody PasswordResetRequest request) {
        PasswordResetDto passwordResetDto = PasswordResetDto.builder()
                .email(request.getEmail())
                .build();

        authService.passwordReset(passwordResetDto);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/password/reset/email/{token}")
    public ResponseEntity<?> passwordResetAuth(@PathVariable("token") String token) {
        String resetPageUrl = "https://k881facf0dd88a.user-app.krampoline.com/password/reset?token=" + token;
        PasswordResetAuthDto passwordResetAuthDto = PasswordResetAuthDto.builder()
                .token(token)
                .build();

        authService.passwordResetAuth(passwordResetAuthDto);

        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header("Location", resetPageUrl).build();
    }

    @PostMapping("/password/reset/check")
    public ResponseEntity<Void> passwordResetCheck(@Valid @RequestBody PasswordResetCheckRequest request) {
        PasswordResetCheckDto passwordResetCheckDto = PasswordResetCheckDto.builder()
                .newPassword(request.getNewPassword())
                .token(request.getToken())
                .build();

        authService.passwordResetCheck(passwordResetCheckDto);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ExceptionHandler(SignUpException.class)
    public ResponseEntity<ErrorMessage> signUpFail(SignUpException e) {
        ErrorMessage errorMessage = new ErrorMessage(e.getMessage(), e.getCode());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }

    @ExceptionHandler(AuthEmailFailException.class)
    public ResponseEntity<Void> authEmailFail() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(DifferentAuthNumberException.class)
    public ResponseEntity<Void> differentAuthNumber() {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }

    @ExceptionHandler(PasswordResetAuthFailException.class)
    public ResponseEntity<Void> passwordResetAuthFail() {
        String resetPageUrl = "https://k881facf0dd88a.user-app.krampoline.com/password";
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header("Location", resetPageUrl).build();
    }

    @ExceptionHandler(PasswordResetCheckFailException.class)
    public ResponseEntity<Void> passwordResetCheckFail() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
