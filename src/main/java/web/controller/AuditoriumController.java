package web.controller;

import beans.models.Auditorium;
import beans.services.AuditoriumService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/auditorium")
public class AuditoriumController {

    @Autowired
    private AuditoriumService auditoriumService;

    @GetMapping()
    public String getAuditoriums(Model model) {
        List<Auditorium> auditoriums = auditoriumService.getAuditoriums();
        model.addAttribute("auditoriums", auditoriums);
        model.addAttribute("header", "All Auditoriums");
        return "auditoriums";
    }

    @GetMapping("/{name}")
    public String getByName(Model model, @PathVariable String name) {
        Auditorium auditorium = auditoriumService.getByName(name);
        model.addAttribute("auditorium", auditorium);
        model.addAttribute("header", "Auditorium by name " + name);
        return "auditorium";
    }
}
