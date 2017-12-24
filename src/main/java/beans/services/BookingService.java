package beans.services;

import beans.models.Event;
import beans.models.Ticket;
import beans.models.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/3/2016
 * Time: 11:22 AM
 */
public interface BookingService {

    double getTicketPrice(String event, String auditorium, LocalDateTime dateTime, List<Integer> seats, User user);

    Ticket createTicket(User user, Event event, LocalDateTime dateTime, List<Integer> seats, double price);

    Ticket bookTicket(User user, Ticket ticket);

    List<Ticket> getTicketsForEvent(String event, String auditorium, LocalDateTime date);

    List<Integer> getAvailableSeats(String event, String auditorium, LocalDateTime date);

    String exportTicketsToPdf(List<Ticket> tickets);
}
