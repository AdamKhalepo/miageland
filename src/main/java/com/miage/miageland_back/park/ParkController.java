package com.miage.miageland_back.park;

import com.miage.miageland_back.users.employee.EmployeeService;
import com.miage.miageland_back.users.visitor.VisitorDTO;
import com.miage.miageland_back.users.visitor.VisitorService;
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
import java.util.List;

@RestController
@RequestMapping("miageland")
@RequiredArgsConstructor
public class ParkController {

    private final ParkService parkService;
    private final EmployeeService employeeService;
    private final VisitorService visitorService;

    /**
     *  Allows the manager to change the limit of visitors in the park
     *  The limit must be superior to the number of visitors already in the park
     */
    @PatchMapping("/park/gauge/{gauge}")
    public void patchAttraction(@CookieValue(value = "user") String userEmail,
                                @PathVariable int gauge) throws IllegalAccessException {
        if (!this.employeeService.isManager(userEmail))
            throw new IllegalAccessException("You must be a manager to call this endpoint.");

        parkService.updateGauge(gauge);
    }

    @GetMapping("/stats")
    public StatisticDaily getDailyStatistics(@CookieValue(value = "user") Cookie userCookie,
                                   @RequestParam(name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) throws IllegalAccessException {
        if (!this.employeeService.isManager(userCookie.getValue()))
            throw new IllegalAccessException("You must be a manager to call this endpoint.");
        return parkService.getStatisticsOfADate(date);
    }

    @GetMapping("/stats")
    public Statistic getStatistics(@CookieValue(value = "user") Cookie userCookie) throws IllegalAccessException {
        if (!this.employeeService.isManager(userCookie.getValue()))
            throw new IllegalAccessException("You must be a manager to call this endpoint.");
        return parkService.getGlobalStatistics();
    }

    @GetMapping("stats/visitors/{visitorId}")
    public VisitorDTO getVisitorStats(@CookieValue(value = "user") Cookie userCookie,
                                      @PathVariable Long visitorId) throws IllegalAccessException {
        if (!this.employeeService.isManager(userCookie.getValue()))
            throw new IllegalAccessException("You must be the manager to call this endpoint.");
        return this.visitorService.visitorsStats(visitorId);
    }

    @GetMapping("stats/visitors")
    public List<VisitorDTO> getAllVisitorsStats(@CookieValue(value = "user") Cookie userCookie) throws IllegalAccessException {
        if (!this.employeeService.isManager(userCookie.getValue()))
            throw new IllegalAccessException("You must be the manager to call this endpoint.");
        return this.visitorService.allVisitorsStats();
    }
}