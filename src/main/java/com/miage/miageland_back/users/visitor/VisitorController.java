package com.miage.miageland_back.users.visitor;

import com.miage.miageland_back.users.employee.EmployeeService;
import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("miageland/visitors")
@RequiredArgsConstructor
public class VisitorController {

    private final VisitorService visitorService;
    private final EmployeeService employeeService;

    /**
     * Login as a visitor
     * @param visitor the visitor to login (email must be set)
     * @param response the response to set the cookie in the header
     */
    @GetMapping("/login")
    public void loginVisitor(@RequestBody Visitor visitor, HttpServletResponse response) {
        try {
            if (!visitorService.isVisitor(visitor.getEmail()))
                throw new IllegalArgumentException("Incorrect user, try with another one.");
            this.visitorService.loginVisitor(visitor.getEmail(), response);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    /**
     * Create a new visitor
     * @param visitor the visitor to create
     * @return the created visitor
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Visitor postVisitor(@RequestBody Visitor visitor) {
        try {
            return this.visitorService.createVisitor(visitor);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (EntityExistsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    /**
     * Delete a visitor
     * @param userEmail the email of the visitor to delete
     * @param visitorId the id of the connected user (through cookie)
     */
    @DeleteMapping("/{visitorId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteVisitor(@CookieValue(value = "user") String userEmail,
                              @PathVariable Long visitorId) {

        try {
            //If the user is a manager OR if the user is a visitor and the visitorId is the same as the user's id
            if (!employeeService.isManager(userEmail) &&
                    (!this.visitorService.isVisitor(userEmail) || !this.visitorService.isSameVisitor(userEmail, visitorId))) {
                throw new IllegalAccessException("An error occured, try again later.");
            }
            this.visitorService.deleteVisitor(visitorId);
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
        } catch (EntityExistsException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
