package com.miage.miageland_back.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    public void deleteUserCookie(HttpServletResponse response) {
        Cookie userCookieToDelete = new Cookie("user", null);
        userCookieToDelete.setMaxAge(0);
        response.addCookie(userCookieToDelete);
    }

    public void addUserCookie(String email,HttpServletResponse response) {
        Cookie adminCookie = new Cookie("user", email);
        adminCookie.setPath("/");
        response.addCookie(adminCookie);
    }
}
