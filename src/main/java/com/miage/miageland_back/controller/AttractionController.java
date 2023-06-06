package com.miage.miageland_back.controller;

import com.miage.miageland_back.entities.Attraction;
import com.miage.miageland_back.service.AttractionService;
import com.miage.miageland_back.service.EmployeeService;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("miageland/attraction")
@RequiredArgsConstructor
public class AttractionController {
    private final AttractionService attractionService;
    private final EmployeeService employeeService;

    @GetMapping
    public List<Attraction> getAttractions() {
        return this.attractionService.getAttractions();
    }

    @GetMapping("/{attractionId}")
    public Attraction getAttraction(@PathVariable Long attractionId) {
        return attractionService.getAttraction(attractionId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Attraction postAttraction(@RequestBody Attraction attraction,
                                     @CookieValue(value = "user") Cookie userCookie) throws IllegalAccessException {
        if (!employeeService.isManager(userCookie.getValue()) && !employeeService.isAdmin(userCookie.getValue()))
            throw new IllegalAccessException("You must be an admin or manager to call this endpoint.");
        return attractionService.createAttraction(attraction);
    }

    @DeleteMapping("/{attractionId}")
    public void deleteAttraction(@PathVariable Long attractionId,
                                 @CookieValue(value = "user") Cookie userCookie) throws IllegalAccessException {
        if (!employeeService.isManager(userCookie.getValue()) && !employeeService.isAdmin(userCookie.getValue()))
            throw new IllegalAccessException("You must be an admin or manager to call this endpoint.");
        attractionService.deleteAttraction(attractionId);
    }

    @PatchMapping("/{attractionId}")
    public boolean patchAttraction(@PathVariable Long attractionId,
                                   @CookieValue(value = "user") Cookie userCookie) throws IllegalAccessException {
        if (!employeeService.isManager(userCookie.getValue()) && !employeeService.isAdmin(userCookie.getValue()))
            throw new IllegalAccessException("You must be an admin or manager to call this endpoint.");
        return attractionService.changeAttractionStatus(attractionId);
    }
}
