package com.nakaligoba.backend.service;

import com.nakaligoba.backend.controller.MemberController.*;
import com.nakaligoba.backend.domain.JwtDetails;
import com.nakaligoba.backend.entity.Member;
import com.nakaligoba.backend.jwt.JwtProvider;
import com.nakaligoba.backend.network.KakaoWebClient;
import com.nakaligoba.backend.repository.MemberRepository;
import com.nakaligoba.backend.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Slf4j
@PropertySource("classpath:application.yml")
@RequiredArgsConstructor
@Service
public class MemberService {

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
    public boolean signup(MemberDto memberDto) {
        if (!memberRepository.existsByEmail(memberDto.getEmail())) {
            Member memberEntity = Member.builder()
                    .email(memberDto.getEmail())
                    .password(passwordEncoder.encode(memberDto.getPassword()))
                    .nickname(memberDto.getNickname())
                    .build();
            memberRepository.save(memberEntity);

            return true;
        } else {
            return false;
        }
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
        Member memberEntity = memberRepository.findByEmail(email);
        JwtDetails jwtDetails = new JwtDetails(memberEntity);
        Authentication authentication = new UsernamePasswordAuthenticationToken(jwtDetails, null, jwtDetails.getAuthorities());

        return jwtProvider.createJwt(authentication);
    }

    @Transactional
    public boolean authEmail(AuthEmailDto authEmailDto) {
        if (!memberRepository.existsByEmail(authEmailDto.getEmail())) {
            String authNumber = getAuthNumber();
            redisUtils.setData(authEmailDto.getEmail(), authNumber, authNumberValidSeconds);
            sendAuthEmail(authEmailDto, authNumber);

            return true;
        } else {
            return false;
        }
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
    public boolean authEmailCheck(AuthEmailCheckDto authEmailCheckDto) {
        return Optional.ofNullable(redisUtils.getData(authEmailCheckDto.getEmail()))
                .map(value -> {
                    if (value.equals(authEmailCheckDto.getAuthNumber())) {
                        redisUtils.deleteData(authEmailCheckDto.getEmail());
                        return true;
                    } else {
                        return false;
                    }
                })
                .orElse(false);
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
        String passwordResetAuthLink = "https://k08e0a348244ea.user-app.krampoline.com/api/v1/auth/password/reset/email/" + resetPasswordToken;
        String contents = "";
        contents += "NakaLiGoBa 비밀번호 재설정 안내 메일입니다.<br/>";
        contents += "비밀번호 재발급을 원하시면 아래의 버튼을 누르세요.<br/><br/>";
        contents += "<a href=\"" + passwordResetAuthLink + "\">비밀번호 재설정</a>";

        sendEmail(title, contents, passwordResetDto.getEmail());
    }

    @Transactional
    public boolean passwordResetAuth(PasswordResetAuthDto passwordResetAuthDto) {
        String email = redisUtils.getData(passwordResetAuthDto.getToken());
        return Optional.ofNullable(memberRepository.findByEmail(email)).isPresent();
    }

    @Transactional
    public boolean passwordResetCheck(PasswordResetCheckDto passwordResetCheckDto) {
        String email = redisUtils.getData(passwordResetCheckDto.getToken());
        log.info("email : " + email);

        return Optional.ofNullable(memberRepository.findByEmail(email))
                .map(updatedMemberEntity -> {
                    updatedMemberEntity.setPassword(passwordEncoder.encode(passwordResetCheckDto.getNewPassword()));
                    memberRepository.save(updatedMemberEntity);
                    redisUtils.deleteData(passwordResetCheckDto.getToken());

                    return true;
                })
                .orElse(false);
    }
}
