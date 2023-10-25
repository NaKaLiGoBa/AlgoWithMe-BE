package com.nakaligoba.backend.utils;

import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

@Component
public class BasicUtils {

    public String getAuthNumber() {
        Random random = new Random();
        return String.valueOf(111111 + random.nextInt(888889));
    }

    public String getUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
