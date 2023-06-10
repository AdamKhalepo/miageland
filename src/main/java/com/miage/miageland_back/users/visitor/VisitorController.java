package com.miage.miageland_back.users.visitor;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("miageland")
@RequiredArgsConstructor
public class VisitorController {

    private final VisitorService visitorService;

    /**
     * Login as a visitor
     * @param visitor the visitor to login (email must be set)
     * @param response the response to set the cookie in the header
     */
    @GetMapping("/visitors/login")
    public void loginVisitor(@RequestBody Visitor visitor, HttpServletResponse response) {
        if (!visitorService.isVisitor(visitor.getEmail()))
            throw new IllegalArgumentException("Incorrect user, try with another one.");

        this.visitorService.loginVisitor(visitor.getEmail(), response);
    }

    /**
     * Create a new visitor
     * @param visitor the visitor to create
     * @return the created visitor
     */
    @PostMapping("/visitors")
    @ResponseStatus(HttpStatus.CREATED)
    public Visitor postVisitor(@RequestBody Visitor visitor) {
        return this.visitorService.createVisitor(visitor);
    }

    //TODO ADD NEW ENDPOINT TO DELETE THE VISITOR
}
