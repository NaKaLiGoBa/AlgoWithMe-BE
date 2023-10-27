package com.nakaligoba.backend.utils;

import com.nakaligoba.backend.domain.JwtDetails;
import org.springframework.security.core.context.SecurityContextHolder;

public class JwtUtils {

    public static String getEmailFromSpringSession() {
        JwtDetails principalDetails = (JwtDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principalDetails.getUsername();
    }
}