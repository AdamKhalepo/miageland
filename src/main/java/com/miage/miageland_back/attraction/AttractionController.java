package com.miage.miageland_back.attraction;

import com.miage.miageland_back.users.employee.EmployeeService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("miageland/attractions")
@RequiredArgsConstructor
public class AttractionController {
    private final AttractionService attractionService;
    private final EmployeeService employeeService;

    @GetMapping
    public List<Attraction> getAttractions(@CookieValue(value = "user") Cookie userCookie) {
        try {
            if (!employeeService.isEmployee(userCookie.getValue()))
                throw new IllegalAccessException("You must be an employee to call this endpoint.");
            return this.attractionService.getAttractions();
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    @GetMapping("/opened")
    public List<Attraction> getOpenedAttractions() {
        return this.attractionService.getOpenedAttractions();
    }

    @GetMapping("/{attractionId}")
    public Attraction getAttraction(@PathVariable Long attractionId,
                                    @CookieValue(value = "user") Cookie userCookie) {
        try {
            if (!employeeService.isEmployee(userCookie.getValue()))
                throw new IllegalAccessException("You must be an employee to call this endpoint.");
            return attractionService.getAttraction(attractionId);
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Attraction postAttraction(@RequestBody Attraction attraction,
                                     @CookieValue(value = "user") Cookie userCookie) {
        try {
            if (!employeeService.isManager(userCookie.getValue()) && !employeeService.isAdmin(userCookie.getValue()))
                throw new IllegalAccessException("You must be an admin or manager to call this endpoint.");
            return attractionService.createAttraction(attraction);
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (EntityExistsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{attractionId}")
    public void deleteAttraction(@PathVariable Long attractionId,
                                 @CookieValue(value = "user") Cookie userCookie) {
        try {
            if (!employeeService.isManager(userCookie.getValue()) && !employeeService.isAdmin(userCookie.getValue()))
                throw new IllegalAccessException("You must be an admin or manager to call this endpoint.");
            attractionService.deleteAttraction(attractionId);
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PatchMapping("/{attractionId}")
    public AttractionDTO patchAttraction(@PathVariable Long attractionId,
                                         @CookieValue(value = "user") Cookie userCookie) {
        try {
            if (!employeeService.isManager(userCookie.getValue()) && !employeeService.isAdmin(userCookie.getValue()))
                throw new IllegalAccessException("You must be an admin or manager to call this endpoint.");
            return attractionService.changeAttractionStatus(attractionId);
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
