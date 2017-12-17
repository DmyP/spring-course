package web.controller;

import beans.models.Ticket;
import beans.services.AuditoriumService;
import beans.services.BookingService;
import beans.services.EventService;
import beans.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import util.exceptions.ExportPdfException;

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
    @Qualifier("bookingServiceImpl")
    private BookingService bookingService;

    @Autowired
    @Qualifier("eventServiceImpl")
    private EventService eventService;

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    @Autowired
    private AuditoriumService auditoriumService;

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
    public ModelAndView bookTickets(@RequestParam("event") String event,
                                    @RequestParam("auditorium") String auditorium,
                                    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
                                    @RequestParam("dateTime") LocalDateTime dateTime,
                                    @RequestParam("seats") List<Integer> seats) {
        //Ticket ticket = bookingService.createTicket(userService.getById(1), eventService.getByName(event).get(0),
          //      dateTime, seats, bookingService.getTicketPrice(event, auditorium, dateTime, seats, userService.getById(1)));

        Ticket ticket = new Ticket(
                eventService.getEvent(event, auditoriumService.getByName(auditorium), dateTime),
                dateTime,
                seats,
                userService.getCurrentUser(),
                bookingService.getTicketPrice(event, auditorium, dateTime,seats, userService.getCurrentUser()));
        bookingService.bookTicket(ticket);
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
