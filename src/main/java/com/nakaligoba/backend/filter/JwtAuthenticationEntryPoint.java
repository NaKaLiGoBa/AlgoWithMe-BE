package com.nakaligoba.backend.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nakaligoba.backend.controller.payload.response.SigninResponse;
import com.nakaligoba.backend.service.component.jwt.JwtProperties;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    public static final String EXPIRATION_JWT = "1";
    public static final String NOT_VALIDATE_JWT = "2";

    private final JwtProperties jwtProperties = new JwtProperties();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String exception = (String) request.getAttribute(jwtProperties.getHEADER_STRING());
        String message = "로그인이 필요한 서비스입니다.";

        if (EXPIRATION_JWT.equals(exception)) {
            message = "토큰이 만료되었습니다.";
        }

        if (NOT_VALIDATE_JWT.equals(exception)) {
            message = "유효하지 않은 토큰입니다.";
        }

        SigninResponse signinResponse = new SigninResponse("", message, "", "");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(convertObjectToString(signinResponse));
    }

    private String convertObjectToString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 변환 중 오류가 발생하였습니다.", e);
        }
    }
}
