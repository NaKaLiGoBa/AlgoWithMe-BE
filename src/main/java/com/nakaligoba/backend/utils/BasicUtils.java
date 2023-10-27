package com.nakaligoba.backend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

@Component
public class BasicUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String convertObjectToString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 변환 중 오류가 발생하였습니다.", e);
        }
    }

    public static String getAuthNumber() {
        Random random = new Random();
        return String.valueOf(111111 + random.nextInt(888889));
    }

    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
