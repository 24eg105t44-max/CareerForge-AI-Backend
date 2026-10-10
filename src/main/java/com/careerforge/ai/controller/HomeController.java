package com.careerforge.ai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        Map<String, Object> response = new HashMap<>();
        response.put("application", "CareerForge AI Backend REST Service");
        response.put("status", "UP");
        response.put("version", "1.0.0");
        response.put("healthCheck", "/api/health");
        response.put("frontendUrl", "https://24eg105t44-max.github.io/CareerForge-AI-Frontend/");
        response.put("message", "Welcome to CareerForge AI API. Access the live web application at frontendUrl.");
        return ResponseEntity.ok(response);
    }
}
