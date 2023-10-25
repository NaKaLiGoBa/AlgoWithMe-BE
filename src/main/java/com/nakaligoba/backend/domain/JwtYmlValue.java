package com.nakaligoba.backend.domain;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class JwtYmlValue {

    @Value("${jwt.secret}")
    private String secretKey;
}
