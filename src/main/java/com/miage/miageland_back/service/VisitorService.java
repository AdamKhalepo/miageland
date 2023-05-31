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
        Visitor loggedVisitor = getVisitor(visitorEmail);

        this.cookieService.deleteUserCookie(response);
        //Adding cookie to response to keep track of the employee
        //This cookie needs to be sent back to the server to identify the employee
        this.cookieService.addUserCookie(loggedVisitor.getEmail(), response);
    }

    public Visitor getVisitor(String visitorEmail) {
        if (!this.visitorRepository.existsByEmail(visitorEmail))
            throw new EntityNotFoundException("Visitor does not exist");

        return this.visitorRepository.findByEmail(visitorEmail);
    }

    public boolean isVisitor(String userCookie) {
        return this.visitorRepository.existsByEmail(userCookie);
    }
}
