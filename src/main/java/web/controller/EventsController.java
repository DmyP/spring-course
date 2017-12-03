package web.controller;

import beans.models.Event;
import beans.services.EventService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/events")
public class EventsController {

    @Autowired
    @Qualifier("eventServiceImpl")
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public String getEvents(Model model) {
        List<Event> events = eventService.getAll();
        model.addAttribute("events", events);
        return "events";
    }

    @GetMapping("/byName/{name}")
    public String getByName(Model model, @PathVariable String name) {
        List<Event> events = eventService.getByName(name);
        model.addAttribute("events", events);
        return "events";
    }

    @GetMapping("/nextEvents/{to}")
    public String getNextEvents(Model model, @PathVariable LocalDateTime to) {
        List<Event> events = eventService.getNextEvents(to);
        model.addAttribute("events", events);
        return "events";
    }

    @GetMapping("/forDateRange/{from}/{to}")
    public String getForDateRange(Model model, @PathVariable LocalDateTime from, @PathVariable LocalDateTime to) {
        List<Event> events = eventService.getForDateRange(from, to);
        model.addAttribute("events", events);
        return "events";
    }
/*
    @PostMapping("/download")
    public String loadEvents(@RequestParam("file") MultipartFile file) throws IOException {
        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf('.'));
        File tmpFile = File.createTempFile("tmp", ext);
        List<Event> events = objectMapper.readValue(tmpFile, new TypeReference<List<Event>>(){});
        events.forEach(event -> eventService.create(event));
        return "redirect:/events";
    }*/
}
