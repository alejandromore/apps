package com.example.demo.controller;

import com.example.demo.model.Cliente;
import com.example.demo.repository.ClienteRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/healthCheck")
public class HealthCheckController {
    @GetMapping
    public String healthCheck() {
        return "up";
    }
}
