package com.innowise.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping()
    public String publicHello() {
        return "Hello from public endpoint!";
    }

    @GetMapping("/private/hello")
    public String privateHello() {
        return "Hello from private endpoint!";
    }
}
