package web.controller;

import beans.models.Event;
import beans.services.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public String getEvents(Model model) {
        List<Event> events = eventService.getAll();
        model.addAttribute("header", "All Events");
        model.addAttribute("events", events);
        return "events";
    }

    @GetMapping("/byName/{name}")
    public String getByName(Model model, @PathVariable String name) {
        List<Event> events = eventService.getByName(name);
        model.addAttribute("header", "Event be name " + name);
        model.addAttribute("events", events);
        return "events";
    }

    @GetMapping("/nextEvents/{from}")
    public String getNextEvents(Model model,  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") @PathVariable LocalDateTime from) {
        List<Event> events = eventService.getNextEvents(from);
        model.addAttribute("header", "Events from " + from);
        model.addAttribute("events", events);
        return "events";
    }

    @GetMapping("/forDateRange/{from}/{to}")
    public String getForDateRange(Model model, @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") @PathVariable LocalDateTime from, @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") @PathVariable LocalDateTime to) {
        List<Event> events = eventService.getForDateRange(from, to);
        model.addAttribute("header", "Events from " + from + " to " + to);
        model.addAttribute("events", events);
        return "events";
    }
}
