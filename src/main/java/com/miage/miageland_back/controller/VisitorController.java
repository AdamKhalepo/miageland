package com.miage.miageland_back.controller;

import com.miage.miageland_back.entities.Ticket;
import com.miage.miageland_back.entities.Visitor;
import com.miage.miageland_back.service.VisitorService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("miageland/visitor")
@RequiredArgsConstructor
public class VisitorController {

    private final VisitorService visitorService;
    @GetMapping("/login")
    public void loginVisitor(@RequestBody Visitor visitor, HttpServletResponse response) {
        this.visitorService.loginVisitor(visitor.getEmail(),response);
    }

    //to refine
    @PostMapping("/{id}/ticket")
    public void postTicket(@PathVariable Long id,Ticket ticket){
        this.visitorService.addTicket(id,ticket);
    }

    @PatchMapping("/{id}/tickets/{idTicket}")
    public void patchTicket(@PathVariable Long idTicket){
        this.visitorService.buyTicket(idTicket);
    }

    //to refine
    @DeleteMapping("/{id}/tickets/{idTicket}")
    public void deleteTicket(@PathVariable Long idTicket){
        this.visitorService.deleteTicket(idTicket);
    }




}
