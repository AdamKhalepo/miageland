package com.miage.miageland_back.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("miageland/visitor")
public class VisitorController {
    @PostMapping("/login")
    public void login() {

    }
}
