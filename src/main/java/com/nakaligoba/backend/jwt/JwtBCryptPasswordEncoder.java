package com.nakaligoba.backend.jwt;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class JwtBCryptPasswordEncoder extends BCryptPasswordEncoder {
}
