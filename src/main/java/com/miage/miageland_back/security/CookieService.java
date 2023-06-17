package com.miage.miageland_back.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
/*
 * Service to manage cookies
 * It is used to simulate a user connection
 * The user cookie is store in the browser to keep the user "connected"
 * It allows the back-end project to see if the user is connected as a visitor or as an employee
 */
public class CookieService {

    /**
     * Delete the user cookie by setting its max age to 0
     * @param response the response to the client to send in the header
     */
    public void deleteUserCookie(HttpServletResponse response) {
        Cookie userCookieToDelete = new Cookie("user", null);
        userCookieToDelete.setMaxAge(0);
        response.addCookie(userCookieToDelete);
    }

    /**
     * Add the user cookie to the response
     * @param email the email of the user to store in the cookie
     * @param response the response to the client to send in the header
     */
    public void addUserCookie(String email,HttpServletResponse response) {
        Cookie userCookie = new Cookie("user", email);
        userCookie.setPath("/");
        userCookie.setMaxAge(60 * 60);
        response.addCookie(userCookie);
    }
}
