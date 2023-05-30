package com.miage.miageland_back.service;

import com.miage.miageland_back.entities.Employee;
import com.miage.miageland_back.entities.Visitor;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    public void deleteCookies(HttpServletResponse response) {
        Cookie deleteEmployeeCookie = new Cookie("employee", null);
        deleteEmployeeCookie.setMaxAge(0);
        Cookie deleteAdminCookie = new Cookie("admin", null);
        deleteAdminCookie.setMaxAge(0);
        Cookie deleteManagerCookie = new Cookie("manager", null);
        deleteManagerCookie.setMaxAge(0);
        Cookie deleteVisitorCookie = new Cookie("visitor", null);
        deleteVisitorCookie.setMaxAge(0);
        response.addCookie(deleteEmployeeCookie);
        response.addCookie(deleteAdminCookie);
        response.addCookie(deleteManagerCookie);
        response.addCookie(deleteVisitorCookie);
    }

    public void addEmployeeCookie(Employee employee,HttpServletResponse response) {
        switch (employee.getRole()) {
            case ADMIN -> {
                Cookie adminCookie = new Cookie("admin", employee.getEmail());
                response.addCookie(adminCookie);
            }
            case MANAGER -> {
                Cookie managerCookie = new Cookie("manager", employee.getEmail());
                response.addCookie(managerCookie);
            }
            default -> {
                Cookie employeeCookie = new Cookie("employee", employee.getEmail());
                response.addCookie(employeeCookie);
            }
        }
    }

    public void addVisitorCookie(Visitor loggedVisitor, HttpServletResponse response) {
        Cookie visitorCookie = new Cookie("visitor", loggedVisitor.getEmail());
        response.addCookie(visitorCookie);
    }
}
