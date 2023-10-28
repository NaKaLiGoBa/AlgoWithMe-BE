package com.nakaligoba.backend.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisUtils {

    private final StringRedisTemplate redisTemplate;

    public String getData(String key) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    public void setData(String key, String value, long duration) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        if (duration == 0) {
            valueOperations.set(key, value);
        } else {
            Duration expireTimeSeconds = Duration.ofSeconds(duration);
            valueOperations.set(key, value, expireTimeSeconds);
        }
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }
}