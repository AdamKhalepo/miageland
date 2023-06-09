package com.miage.miageland_back.users.visitor;

import com.miage.miageland_back.ticket.TicketRepository;
import com.miage.miageland_back.ticket.TicketState;
import com.miage.miageland_back.security.CookieService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VisitorService {
    private final VisitorRepository visitorRepository;
    private final TicketRepository ticketRepository;
    private final CookieService cookieService;

    public void loginVisitor(String visitorEmail, HttpServletResponse response) {
        Visitor loggedVisitor = getVisitorByEmail(visitorEmail);

        this.cookieService.deleteUserCookie(response);
        //Adding cookie to response to keep track of the employee
        //This cookie needs to be sent back to the server to identify the employee
        this.cookieService.addUserCookie(loggedVisitor.getEmail(), response);
    }

    public Visitor getVisitorByEmail(String visitorEmail) {
        return this.visitorRepository.findByEmail(visitorEmail)
                .orElseThrow(() -> new EntityNotFoundException("Visitor does not exist"));
    }

    public Visitor getVisitorById(Long visitorId) {
        return this.visitorRepository.findById(visitorId)
                .orElseThrow(() -> new EntityNotFoundException("Visitor does not exist"));
    }

    public boolean isVisitor(String userCookie) {
        return this.visitorRepository.existsByEmail(userCookie);
    }

    public List<VisitorDTO> allVisitors(){
        return visitorRepository.findAll().stream().map(visitor ->
            new VisitorDTO(
                    visitor.getId(),
                    visitor.getName(),
                    visitor.getFirstName(),
                    ticketRepository.countTicketsByVisitorAndState(visitor, TicketState.USED)
            )
        ).toList();
    }

    public Visitor createVisitor(Visitor newVisitor) {
        if (missingFields(newVisitor))
            throw new IllegalArgumentException("Missing parameters, please provide all parameters");

        if(this.visitorRepository.existsByEmail(newVisitor.getEmail()))
            throw new EntityNotFoundException("Visitor already exists");

        this.visitorRepository.save(newVisitor);

        return newVisitor;
    }

    private boolean missingFields(Visitor newVisitor) {
        return newVisitor.getEmail() == null || newVisitor.getName() == null || newVisitor.getFirstName() == null;
    }

    public boolean isSameVisitor(String userCookie, Long visitorId) {
        return this.visitorRepository.findById(visitorId).get().getEmail().equals(userCookie);
    }
}