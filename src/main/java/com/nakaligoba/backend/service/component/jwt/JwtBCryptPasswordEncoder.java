package com.nakaligoba.backend.service.component.jwt;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class JwtBCryptPasswordEncoder extends BCryptPasswordEncoder {
}
