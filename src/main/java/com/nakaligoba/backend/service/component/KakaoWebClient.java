package com.nakaligoba.backend.service.component;


import com.nakaligoba.backend.service.dto.KakaoSigninTokenResponse;
import com.nakaligoba.backend.service.dto.KakaoSigninUserInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.ProxyProvider;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoWebClient {

    @Value("${kakao.login.grant-type}")
    private String GRANT_TYPE;

    @Value("${kakao.client-id}")
    private String CLIENT_ID;

    @Value("${kakao.login.redirect-url}")
    private String REDIRECT_URL;

    private final WebClient webClient = WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(build()))
            .build();

    private final RestTemplate restTemplate;

    private HttpClient build() {
        return HttpClient.create()
                .tcpConfiguration(tcpClient -> tcpClient.proxy(proxy ->
                    proxy.type(ProxyProvider.Proxy.HTTP).host("krmp-proxy.9rum.cc").port(3128)
                ));
    }

    public KakaoSigninTokenResponse getKakaoSigninToken(String kakaoAuthCode) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", GRANT_TYPE);
        formData.add("client_id", CLIENT_ID);
        formData.add("redirect_url", REDIRECT_URL);
        formData.add("code", kakaoAuthCode);

        log.info("Check Auth Code: {}", kakaoAuthCode);
        return webClient
                .post()
                .uri("https://kauth.kakao.com/oauth/token")
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8")
                .bodyValue(formData)
                .retrieve()
                .bodyToMono(KakaoSigninTokenResponse.class)
                .block();
    }

    public KakaoSigninTokenResponse getKakaoSigninTokenV2(String kakaoAuthCode) {
        log.info("kakao signin v2");
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", GRANT_TYPE);
        formData.add("client_id", CLIENT_ID);
        formData.add("redirect_url", REDIRECT_URL);
        formData.add("code", kakaoAuthCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType(MediaType.APPLICATION_FORM_URLENCODED, StandardCharsets.UTF_8));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);
        return restTemplate.postForObject("https://kauth.kakao.com/oauth/token", request, KakaoSigninTokenResponse.class);
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
