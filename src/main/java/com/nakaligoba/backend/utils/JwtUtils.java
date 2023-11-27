package com.nakaligoba.backend.utils;

import com.nakaligoba.backend.domain.JwtDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class JwtUtils {

    public static String getEmailFromSpringSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = "";

        if (authentication == null || "anonymousUser".equals(authentication.getName())) {
            return email;
        }

        email = ((JwtDetails) authentication.getPrincipal()).getUsername();
        return email;
    }
}
