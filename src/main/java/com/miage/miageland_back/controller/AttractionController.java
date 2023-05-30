package com.miage.miageland_back.controller;

import com.miage.miageland_back.entities.Attraction;
import com.miage.miageland_back.service.AttractionService;
import com.miage.miageland_back.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public Attraction getAttraction(@PathVariable String attractionId) {
        return attractionService.getAttraction(Long.valueOf(attractionId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Attraction postAttraction(@RequestBody Attraction attraction) {
        //todo : only admin can create attraction, so add annotation to check the user is an admin
        return attractionService.createAttraction(attraction);
    }

    @DeleteMapping("/{attractionId}")
    public void deleteAttraction(@PathVariable String attractionId) {
        //todo : only admin can delete attraction, so add annotation to check the user is an admin
        attractionService.deleteAttraction(Long.valueOf(attractionId));
    }

    @PatchMapping("/{attractionId}")
    public boolean patchAttraction(@PathVariable String attractionId) {
        //todo : only admin can update attraction, so add annotation to check the user is an admin
        return attractionService.changeAttractionStatus(Long.valueOf(attractionId));
    }




}
