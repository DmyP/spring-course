package web.controller;

import beans.models.Ticket;
import beans.models.User;
import beans.services.BookingService;
import beans.services.EventService;
import beans.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import util.exceptions.ExportPdfException;
import util.exceptions.NotEnoughMoneyException;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/book")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private EventService eventService;

    @GetMapping
    public String getTickets(@RequestParam("event") String event,
                             @RequestParam("auditorium") String auditorium,
                             @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
                             @RequestParam("dateTime") LocalDateTime dateTime,
                             Model model) {
        model.addAttribute("header", "Tickets");
        model.addAttribute("event", event);
        model.addAttribute("auditorium", auditorium);
        model.addAttribute("dateTime", dateTime);
        List<Integer> seats = bookingService.getAvailableSeats(event, auditorium, dateTime);
        model.addAttribute("seats", seats);
        List<Ticket> tickets = bookingService.getTicketsForEvent(event, auditorium, dateTime);
        model.addAttribute("tickets", tickets);
        return "tickets";
    }

    @PostMapping("/tickets")
    @Transactional(rollbackFor = {NotEnoughMoneyException.class, IllegalStateException.class,
            NullPointerException.class}, isolation = Isolation.SERIALIZABLE)
    public ModelAndView bookTickets(@RequestParam("event") String event,
                                    @RequestParam("auditorium") String auditorium,
                                    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
                                    @RequestParam("dateTime") LocalDateTime dateTime,
                                    @RequestParam("seats") List<Integer> seats) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        double ticketPrice = bookingService.getTicketPrice(event, auditorium, dateTime, seats, currentUser);
        if (currentUser.getUserAccount().getMoney() < ticketPrice) {
            throw new NotEnoughMoneyException();
        }
        Ticket ticket = bookingService.createTicket(currentUser, eventService.getByName(event).get(0),
                dateTime, seats, ticketPrice);
        bookingService.bookTicket(currentUser, ticket);
        Map<String, String> attributes = new HashMap<>();
        attributes.put("event", event);
        attributes.put("dateTime", dateTime.toString());
        attributes.put("auditorium", auditorium);
        return new ModelAndView("redirect:/book", attributes);
    }

    @PostMapping("/tickets/toPdf")
    @ResponseBody
    public void generatePdf(@RequestParam("event") String event,
                            @RequestParam("auditorium") String auditorium,
                            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
                            @RequestParam("dateTime") LocalDateTime dateTime,
                            HttpServletResponse response) {
        String filePath = bookingService.exportTicketsToPdf(bookingService.getTicketsForEvent(event, auditorium, dateTime));

        File file = new File(filePath);
        try (InputStream is = new FileInputStream(file)) {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
            response.setHeader("Content-Length", String.valueOf(file.length()));
            FileCopyUtils.copy(is, response.getOutputStream());
        } catch (Exception e) {
            throw new ExportPdfException(e);
        }
    }
}
