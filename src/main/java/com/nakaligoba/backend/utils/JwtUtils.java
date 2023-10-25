package com.nakaligoba.backend.utils;

import com.nakaligoba.backend.domain.JwtDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    public String getEmailFromSpringSession() {
        JwtDetails principalDetails = (JwtDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principalDetails.getUsername();
    }
}