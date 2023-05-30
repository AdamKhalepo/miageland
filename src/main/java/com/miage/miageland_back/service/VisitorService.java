package com.miage.miageland_back.service;

import com.miage.miageland_back.dao.repository.VisitorRepository;
import com.miage.miageland_back.entities.Visitor;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VisitorService {
    private final VisitorRepository visitorRepository;
    private final CookieService cookieService;

    public void loginVisitor(String visitorEmail, HttpServletResponse response) {
        if (!visitorRepository.existsByEmail(visitorEmail))
            throw new EntityNotFoundException("Employee does not exist");

        Visitor loggedVisitor = visitorRepository.findByEmail(visitorEmail);

        cookieService.deleteUserCookie(response);
        //Adding cookie to response to keep track of the employee
        //This cookie needs to be sent back to the server to identify the employee
        cookieService.addUserCookie(loggedVisitor.getEmail(), response);
    }
}
