package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/healthCheck")
public class HealthCheckController {
    @GetMapping
    public String healthCheck() {
        return "up";
    }
}
