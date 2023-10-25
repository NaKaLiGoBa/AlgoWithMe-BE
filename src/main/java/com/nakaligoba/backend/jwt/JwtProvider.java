package com.nakaligoba.backend.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.nakaligoba.backend.domain.JwtDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtProvider {

    @Autowired
    private JwtProperties jwtProperties;

    public String createJwt(Authentication authentication) {
        JwtDetails principalDetails = (JwtDetails) authentication.getPrincipal();
        String jwtToken = JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getEXPIRATION_TIME() * 1000L))
                .withClaim(jwtProperties.getCLAIM(), principalDetails.getUsername())
                .sign(Algorithm.HMAC512(jwtProperties.getSECRET_KEY()));

        return jwtProperties.getTOKEN_PREFIX() + jwtToken;
    }
}