package com.nakaligoba.backend.service.impl;

import com.nakaligoba.backend.controller.payload.response.SigninResponse;
import com.nakaligoba.backend.domain.JwtDetails;
import com.nakaligoba.backend.domain.Member;
import com.nakaligoba.backend.domain.Role;
import com.nakaligoba.backend.exception.AuthEmailFailException;
import com.nakaligoba.backend.exception.DifferentAuthNumberException;
import com.nakaligoba.backend.exception.DuplicateEmailException;
import com.nakaligoba.backend.exception.DuplicateNicknameException;
import com.nakaligoba.backend.exception.PasswordResetAuthFailException;
import com.nakaligoba.backend.exception.PasswordResetCheckFailException;
import com.nakaligoba.backend.repository.MemberRepository;
import com.nakaligoba.backend.service.SignUpUseCase;
import com.nakaligoba.backend.service.component.KakaoWebClient;
import com.nakaligoba.backend.service.component.RedisUtils;
import com.nakaligoba.backend.service.component.jwt.JwtProvider;
import com.nakaligoba.backend.service.dto.AuthEmailCheckDto;
import com.nakaligoba.backend.service.dto.AuthEmailDto;
import com.nakaligoba.backend.service.dto.KakaoSigninTokenResponse;
import com.nakaligoba.backend.service.dto.KakaoSigninUserInfoResponse;
import com.nakaligoba.backend.service.dto.MemberDto;
import com.nakaligoba.backend.service.dto.PasswordResetAuthDto;
import com.nakaligoba.backend.service.dto.PasswordResetCheckDto;
import com.nakaligoba.backend.service.dto.PasswordResetDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityNotFoundException;
import java.util.Random;
import java.util.UUID;

@Slf4j
@PropertySource("classpath:application.yml")
@RequiredArgsConstructor
@Service
public class AuthService implements SignUpUseCase {

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${spring.redis.auth-num-valid-time}")
    private int authNumberValidSeconds;

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final KakaoWebClient kakaoWebClient;
    private final JwtProvider jwtProvider;
    private final RedisUtils redisUtils;

    @Transactional
    public void signup(MemberDto memberDto) {
        if (memberRepository.existsByEmail(memberDto.getEmail())) {
            throw new DuplicateEmailException();
        }

        if (memberRepository.existsByNickname(memberDto.getNickname())) {
            throw new DuplicateNicknameException();
        }

        Role role = Role.ROLE_USER;

        if(!StringUtils.isEmpty(memberDto.getRole())
                && "admin".equals(memberDto.getRole())) {
            role = Role.ROLE_ADMIN;
        }

        Member memberEntity = Member.builder()
                .email(memberDto.getEmail())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .nickname(memberDto.getNickname())
                .role(role)
                .build();

        memberRepository.save(memberEntity);
    }

    @Transactional
    public SigninResponse kakaoSignin(String kakaoAuthCode) {
        KakaoSigninTokenResponse kakaoSigninToken = getKakaoSigninToken(kakaoAuthCode);
        KakaoSigninUserInfoResponse kakaoSigninUserInfo = getKakaoSigninUserInfo(kakaoSigninToken.getAccess_token());

        if (memberRepository.existsByEmail(kakaoSigninUserInfo.getKakao_account().getEmail())) {
            String jwt = getMemberJwt(kakaoSigninUserInfo.getKakao_account().getEmail());
            log.info("jwt : " + jwt);

            return new SigninResponse(jwt, "로그인이 완료되었습니다.", kakaoSigninUserInfo.getKakao_account().getEmail(), kakaoSigninUserInfo.getKakao_account().getProfile().getNickname());
        } else {
            return new SigninResponse("", "회원가입이 필요합니다", "", "");
        }
    }

    private KakaoSigninTokenResponse getKakaoSigninToken(String kakaoAuthCode) {
        return kakaoWebClient.getKakaoSigninToken(kakaoAuthCode);
    }

    private KakaoSigninUserInfoResponse getKakaoSigninUserInfo(String accessToken) {
        return kakaoWebClient.getKakaoSigninUserInfo(accessToken);
    }

    private String getMemberJwt(String email) {
        Member memberEntity = memberRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        JwtDetails jwtDetails = new JwtDetails(memberEntity);
        Authentication authentication = new UsernamePasswordAuthenticationToken(jwtDetails, null, jwtDetails.getAuthorities());

        return jwtProvider.createJwt(authentication);
    }

    @Transactional
    public void authEmail(AuthEmailDto authEmailDto) {
        if (memberRepository.existsByEmail(authEmailDto.getEmail())) {
            throw new AuthEmailFailException("인증에 실패하였습니다. 이미 등록된 이메일입니다.");
        }

        String authNumber = getAuthNumber();

        redisUtils.setData(authEmailDto.getEmail(), authNumber, authNumberValidSeconds);
        sendAuthEmail(authEmailDto, authNumber);
    }

    private String getAuthNumber() {
        Random random = new Random();
        return String.valueOf(111111 + random.nextInt(888889));
    }

    private void sendAuthEmail(AuthEmailDto authEmailDto, String authNumber) {
        String title = "NakaLiGoBa 회원가입 인증 메일입니다.";
        String contents = "";
        contents += "안녕하세요. NakaLiGoBa 입니다.<br/>";
        contents += "NakaLiGoBa 회원가입을 진심으로 환영합니다.<br/><br/>";
        contents += "아래의 인증번호를 입력하여 인증을 하시면 NakaLiGoBa 회원가입이 완료됩니다.<br/><br/>";
        contents += "회원가입 인증번호 : ";
        contents += authNumber;

        sendEmail(title, contents, authEmailDto.getEmail());
    }

    private void sendEmail(String title, String contents, String toEmail) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(toEmail);
            mimeMessageHelper.setSubject(title);
            mimeMessageHelper.setText(contents, true);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void authEmailCheck(AuthEmailCheckDto authEmailCheckDto) {
        String savedAuthNumber = redisUtils.getData(authEmailCheckDto.getEmail());

        if (StringUtils.isEmpty(savedAuthNumber)) {
            throw new AuthEmailFailException("인증번호를 찾을 수 없습니다.");
        }

        if (!savedAuthNumber.equals(authEmailCheckDto.getAuthNumber())) {
            throw new DifferentAuthNumberException();
        }

        redisUtils.deleteData(authEmailCheckDto.getEmail());
    }

    @Transactional
    public void passwordReset(PasswordResetDto passwordResetDto) {
        String resetPasswordToken = getUUID();

        redisUtils.setData(resetPasswordToken, passwordResetDto.getEmail(), authNumberValidSeconds);
        passwordResetEmail(passwordResetDto, resetPasswordToken);
    }

    private String getUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    private void passwordResetEmail(PasswordResetDto passwordResetDto, String resetPasswordToken) {
        String title = "[NakaLiGoBa] 비밀번호 재설정 메일입니다.";
        String passwordResetAuthLink = "https://k881facf0dd88a.user-app.krampoline.com/api/v1/auth/password/reset/email/" + resetPasswordToken;
        String contents = "";
        contents += "NakaLiGoBa 비밀번호 재설정 안내 메일입니다.<br/>";
        contents += "비밀번호 재발급을 원하시면 아래의 버튼을 누르세요.<br/><br/>";
        contents += "<a href=\"" + passwordResetAuthLink + "\">비밀번호 재설정</a>";

        sendEmail(title, contents, passwordResetDto.getEmail());
    }

    @Transactional
    public void passwordResetAuth(PasswordResetAuthDto passwordResetAuthDto) {
        String email = redisUtils.getData(passwordResetAuthDto.getToken());
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(PasswordResetAuthFailException::new);
    }

    @Transactional
    public void passwordResetCheck(PasswordResetCheckDto passwordResetCheckDto) {
        String email = redisUtils.getData(passwordResetCheckDto.getToken());
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(PasswordResetCheckFailException::new);

        member.setPassword(passwordEncoder.encode(passwordResetCheckDto.getNewPassword()));
        redisUtils.deleteData(passwordResetCheckDto.getToken());
    }
}
