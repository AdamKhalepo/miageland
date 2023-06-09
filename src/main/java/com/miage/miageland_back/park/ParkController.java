package com.miage.miageland_back.park;

import com.miage.miageland_back.users.employee.EmployeeService;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@RestController
@RequestMapping("miageland/park")
@RequiredArgsConstructor
public class ParkController {

    private final ParkService parkService;
    private final EmployeeService employeeService;

    @GetMapping()
    public Statistic getStatistics(@CookieValue(value = "user") Cookie userCookie,
                                   @RequestParam(name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) throws IllegalAccessException {
        if (!this.employeeService.isManager(userCookie.getValue()))
            throw new IllegalAccessException("You must be a manager to call this endpoint.");
        return parkService.getStatisticsOfaDate(date);
    }

    /**
     *  Allows the manager to change the limit of visitors in the park
     *  The limit must be superior to the number of visitors already in the park
     */
    @PatchMapping("/gauge/{gauge}")
    public void patchAttraction(@CookieValue(value = "user") String userEmail,
                                @PathVariable int gauge) throws IllegalAccessException {
        if (!this.employeeService.isManager(userEmail))
            throw new IllegalAccessException("You must be a manager to call this endpoint.");

        parkService.updateGauge(gauge);
    }
}