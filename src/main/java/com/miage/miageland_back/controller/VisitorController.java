package com.miage.miageland_back.controller;

import com.miage.miageland_back.entities.Visitor;
import com.miage.miageland_back.service.VisitorService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("miageland/visitor")
@RequiredArgsConstructor
public class VisitorController {

    private final VisitorService visitorService;
    @GetMapping("/login")
    public void loginVisitor(@RequestBody Visitor visitor, HttpServletResponse response) {
        this.visitorService.loginVisitor(visitor.getEmail(),response);
    }
}
