package com.miage.miageland_back.controller;

import com.miage.miageland_back.dto.VisitorDTO;
import com.miage.miageland_back.entities.Visitor;
import com.miage.miageland_back.service.EmployeeService;
import com.miage.miageland_back.service.VisitorService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("miageland")
@RequiredArgsConstructor
public class VisitorController {

    private final VisitorService visitorService;
    private final EmployeeService employeeService;

    @GetMapping("/visitors/login")
    public void loginVisitor(@RequestBody Visitor visitor, HttpServletResponse response) {
        this.visitorService.loginVisitor(visitor.getEmail(), response);
    }

    @PostMapping("/visitors")
    @ResponseStatus(HttpStatus.CREATED)
    public Visitor postVisitor(@RequestBody Visitor visitor) {
        return this.visitorService.createVisitor(visitor);
    }

    @GetMapping("/visitors")
    public List<VisitorDTO> getAllVisitor(@CookieValue(value = "user") Cookie userCookie) throws IllegalAccessException {
        if (!this.employeeService.isManager(userCookie.getValue()))
            throw new IllegalAccessException("You must be the manager to call this endpoint.");
        return this.visitorService.allVisitors();
    }
}
