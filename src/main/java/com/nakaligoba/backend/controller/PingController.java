package com.nakaligoba.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @GetMapping("/api/test")
    private ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }
}
