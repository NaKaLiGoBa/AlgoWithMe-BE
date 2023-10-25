package com.nakaligoba.backend.utils;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AuthNumberManager {

    private Map<String, String> authNumberStore = new HashMap<>();

    public void setData(String key, String value) {
        authNumberStore.put(key, value);
    }

    public String getData(String key) {
        return authNumberStore.get(key);
    }

    public void removeCode(String key) {
        authNumberStore.remove(key);
    }
}