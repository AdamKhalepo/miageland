package com.miage.miageland_back.park;

import com.miage.miageland_back.users.employee.EmployeeService;
import com.miage.miageland_back.users.visitor.VisitorDTO;
import com.miage.miageland_back.users.visitor.VisitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

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
     *
     * @param userEmail the email of the manager (set in user cookie)
     * @param gauge the new limit of visitors in the park
     */
    @PatchMapping("/park/gauge/{gauge}")
    public void patchAttraction(@CookieValue(value = "user") String userEmail, @PathVariable int gauge) {
        try {
            //checking if the user is a manager
            if (!this.employeeService.isManager(userEmail))
                throw new IllegalAccessException("You must be a manager to call this endpoint.");

            parkService.updateGauge(gauge);
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Get all the stats of the park of a specific date
     * @param userEmail the email of the manager (set in user cookie)
     * @param date the date of the stats
     * @return the {@link StatisticDaily} of the date given in parameter
     */
    @GetMapping(value = "/stats", params = "date")
    public StatisticDaily getDailyStatistics(@CookieValue(value = "user") String userEmail,
                                   @RequestParam(name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        try {
            if (!this.employeeService.isManager(userEmail))
                throw new IllegalAccessException("You must be a manager to call this endpoint.");
            return parkService.getStatisticsOfADate(date);
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    /**
     * Get all the global stats of the park
     * @param userEmail the email of the manager (set in user cookie)
     * @return the global {@link Statistic}
     */
    @GetMapping("/stats")
    public Statistic getGlobalStatistics(@CookieValue(value = "user") String userEmail) {
        try {
            if (!this.employeeService.isManager(userEmail))
                throw new IllegalAccessException("You must be a manager to call this endpoint.");
            return parkService.getGlobalStatistics();
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    /**
     * Get the stats of a specific visitor
     * @param userEmail the email of the manager (set in user cookie)
     * @param visitorId the id of the visitor
     * @return the {@link VisitorDTO} of the visitor
     */
    @GetMapping("stats/visitors/{visitorId}")
    public VisitorDTO getVisitorStats(@CookieValue(value = "user") String userEmail,
                                      @PathVariable Long visitorId) {
        try {
            if (!this.employeeService.isManager(userEmail))
                throw new IllegalAccessException("You must be the manager to call this endpoint.");
            return this.visitorService.visitorsStats(visitorId);
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }

    /**
     * Get all the stats of all the visitors
     * @param userEmail the email of the manager (set in user cookie)
     * @return a list of {@link VisitorDTO}
     */
    @GetMapping("stats/visitors")
    public List<VisitorDTO> getAllVisitorsStats(@CookieValue(value = "user") String userEmail) {
        try {
            if (!this.employeeService.isManager(userEmail))
                throw new IllegalAccessException("You must be the manager to call this endpoint.");
            return this.visitorService.allVisitorsStats();
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        }
    }
}