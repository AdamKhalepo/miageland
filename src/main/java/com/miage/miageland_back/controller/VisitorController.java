package com.miage.miageland_back.controller;

import com.miage.miageland_back.dto.VisitorDto;
import com.miage.miageland_back.entities.Visitor;
import com.miage.miageland_back.service.EmployeeService;
import com.miage.miageland_back.service.VisitorService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("miageland")
@RequiredArgsConstructor
public class VisitorController {

    private final VisitorService visitorService;
    private final EmployeeService employeeService;

    @GetMapping("/visitor/login")
    public void loginVisitor(@RequestBody Visitor visitor, HttpServletResponse response) {
        this.visitorService.loginVisitor(visitor.getEmail(), response);
    }

    @GetMapping("/visitors")
    public List<VisitorDto> getAllVisitor(@CookieValue(value = "user") Cookie userCookie) throws IllegalAccessException {
        if (!this.employeeService.isManager(userCookie.getValue()))
            throw new IllegalAccessException("You must be the manager to call this endpoint.");
        return this.visitorService.allVisitors();
    }
}
