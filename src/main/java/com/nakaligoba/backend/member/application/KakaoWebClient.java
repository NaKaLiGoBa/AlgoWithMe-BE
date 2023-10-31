package com.nakaligoba.backend.member.application;

import com.nakaligoba.backend.member.controller.MemberController.KakaoSigninTokenResponse;
import com.nakaligoba.backend.member.controller.MemberController.KakaoSigninUserInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class KakaoWebClient {

    @Value("${kakao.login.grant-type}")
    private String GRANT_TYPE;

    @Value("${kakao.client-id}")
    private String CLIENT_ID;

    @Value("${kakao.login.redirect-url}")
    private String REDIRECT_URL;

    private final WebClient webClient = WebClient.builder().build();

    public KakaoSigninTokenResponse getKakaoSigninToken(String kakaoAuthCode) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", GRANT_TYPE);
        formData.add("client_id", CLIENT_ID);
        formData.add("redirect_url", REDIRECT_URL);
        formData.add("code", kakaoAuthCode);

        return webClient
                .post()
                .uri("https://kauth.kakao.com/oauth/token")
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(KakaoSigninTokenResponse.class)
                .block();
    }

    public KakaoSigninUserInfoResponse getKakaoSigninUserInfo(String accessToken) {
        return webClient
                .post()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(KakaoSigninUserInfoResponse.class)
                .block();
    }
}
