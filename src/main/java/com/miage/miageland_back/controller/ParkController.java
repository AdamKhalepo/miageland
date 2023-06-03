package com.miage.miageland_back.controller;

import com.miage.miageland_back.entities.Statistic;
import com.miage.miageland_back.service.EmployeeService;
import com.miage.miageland_back.service.ParkService;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("miageland/park")
@RequiredArgsConstructor
public class ParkController {

    private final ParkService parkService;
    private final EmployeeService employeeService;

    @GetMapping()
    public Statistic getStatistiques(@CookieValue(value = "user") Cookie userCookie, @RequestParam(name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) throws IllegalAccessException {
        if (!this.employeeService.isManager(userCookie.getValue()))
            throw new IllegalAccessException("You must be a manager to call this endpoint.");
        return parkService.getStatisticsOfaDate(date);
    }
}