package com.miage.miageland_back.security;

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
        Cookie userCookie = new Cookie("user", email);
        userCookie.setPath("/");
        userCookie.setMaxAge(60 * 60);
        response.addCookie(userCookie);
    }
}