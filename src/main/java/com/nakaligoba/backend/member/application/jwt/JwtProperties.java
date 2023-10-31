package com.nakaligoba.backend.member.application.jwt;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class JwtProperties {
    @Value("${jwt.claim}")
    private String CLAIM;

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.token-validity-in-seconds}")
    private int EXPIRATION_TIME;

    private final String TOKEN_PREFIX = "Bearer ";
    private final String HEADER_STRING = "Authorization";
}
