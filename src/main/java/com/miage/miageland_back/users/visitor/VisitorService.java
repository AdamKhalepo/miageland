package com.miage.miageland_back.users.visitor;

import com.miage.miageland_back.ticket.Ticket;
import com.miage.miageland_back.ticket.TicketRepository;
import com.miage.miageland_back.ticket.TicketState;
import com.miage.miageland_back.security.CookieService;
import jakarta.persistence.EntityExistsException;
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

    /**
     * Log the user in by adding a cookie to the response
     * @param visitorEmail the email of the visitor to log in
     *
     */
    public void loginVisitor(String visitorEmail, HttpServletResponse response) {
        //Resetting the cookie if it already exists
        this.cookieService.deleteUserCookie(response);
        //Adding cookie to response to keep track of the employee
        //This cookie needs to be sent back to the server to identify the employee
        this.cookieService.addUserCookie(visitorEmail, response);
    }

    /**
     * Get the visitor by his email
     * @param visitorEmail the email of the visitor to get
     * @return the visitor
     * @throws EntityNotFoundException if the visitor does not exist
     */
    public Visitor getVisitorByEmail(String visitorEmail) {
        return this.visitorRepository.findByEmail(visitorEmail)
                .orElseThrow(() -> new EntityNotFoundException("Visitor does not exist"));
    }

    /**
     * Get the visitor by his id
     * @param visitorId the id of the visitor to get
     * @return the visitor
     * @throws EntityNotFoundException if the visitor does not exist
     */
    public Visitor getVisitorById(Long visitorId) {
        return this.visitorRepository.findById(visitorId)
                .orElseThrow(() -> new EntityNotFoundException("Visitor does not exist"));
    }

    /**
     * Check if the visitor exists
     * @param email the email of the visitor to check
     * @return true if the visitor exists, false otherwise
     */
    public boolean isVisitor(String email) {
        return this.visitorRepository.existsByEmail(email);
    }

    /**
     * Get the visitor stats
     * @param visitorId the id of the visitor to get the stats
     * @return the {@link VisitorDTO} stats
     */
    public VisitorDTO visitorsStats(Long visitorId) {
        Visitor visitor = getVisitorById(visitorId);
        return new VisitorDTO(
                visitor.getId(),
                visitor.getName(),
                visitor.getFirstName(),
                ticketRepository.countTicketsByVisitorAndState(visitor, TicketState.USED)
        );
    }

    /**
     * Get the stats of all the visitors
     * @return the list of {@link VisitorDTO} stats
     */
    public List<VisitorDTO> allVisitorsStats() {
        return visitorRepository.findAll().stream().map(visitor -> new VisitorDTO(
                visitor.getId(),
                visitor.getName(),
                visitor.getFirstName(),
                ticketRepository.countTicketsByVisitorAndState(visitor, TicketState.USED)
        )).toList();
    }

    /**
     * Create a new visitor
     * @param newVisitor the visitor to create
     * @return the created visitor
     * @throws IllegalArgumentException if the visitor is missing fields
     * @throws EntityNotFoundException if the visitor already exists
     */
    public Visitor createVisitor(Visitor newVisitor) {
        if (missingFields(newVisitor))
            throw new IllegalArgumentException("Missing parameters, please provide all parameters");

        if (this.isVisitor(newVisitor.getEmail()))
            throw new EntityExistsException("Visitor already exists");

        this.visitorRepository.save(newVisitor);

        return newVisitor;
    }

    /**
     * Check if the visitor is missing fields
     * @param newVisitor the visitor to check
     * @return true if the visitor is missing fields, false otherwise
     */
    private boolean missingFields(Visitor newVisitor) {
        return newVisitor.getEmail() == null || newVisitor.getName() == null || newVisitor.getFirstName() == null;
    }

    /**
     * Check if the visitor is the same as the one in the cookie
     * @param userCookie the cookie of the visitor
     * @param visitorId the id of the visitor to check
     * @return true if the visitor is the same as the one in the cookie, false otherwise
     */
    public boolean isSameVisitor(String userCookie, Long visitorId) {
        return this.visitorRepository.findById(visitorId).get().getEmail().equals(userCookie);
    }

    /**
     * Delete a visitor and also delete all his tickets
     * @param visitorId the id of the visitor to delete
     */
    public void deleteVisitor(Long visitorId) {
        List<Ticket> ticketsToDelete = this.ticketRepository.findByVisitor(this.visitorRepository.findById(visitorId).get());
        this.ticketRepository.deleteAll(ticketsToDelete);
        this.visitorRepository.deleteById(visitorId);
    }
}
