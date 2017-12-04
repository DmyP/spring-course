package web.controller;

import beans.models.Ticket;
import beans.models.Auditorium;
import beans.services.AuditoriumService;
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
import java.util.List;

@Controller
@RequestMapping("/auditorium")
public class AuditoriumController {

    @Autowired
    @Qualifier("auditoriumServiceImpl")
    private AuditoriumService auditoriumService;
    
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping()
    public String getAuditoriums(Model model) {
        List<Auditorium> auditoriums = auditoriumService.getAuditoriums();
        model.addAttribute("auditoriums", auditoriums);
        model.addAttribute("header", "All Auditoriums");
        return "auditoriums";
    }

    @GetMapping("/byName/{name}")
    public String getByName(Model model, @PathVariable String name) {
        Auditorium auditorium = auditoriumService.getByName(name);
        model.addAttribute("auditoriums", auditorium);
        model.addAttribute("header", "Auditorium by name " + name);
        return "auditoriums";
    }

    @PostMapping("/load")
    public String load(@RequestParam("file") MultipartFile file) throws IOException {
        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf('.'));
        File tmpFile = File.createTempFile("tmp", ext);
        List<Auditorium> auditoriums = objectMapper.readValue(tmpFile, new TypeReference<List<Auditorium>>(){});
        auditoriums.forEach(auditorium -> auditoriumService.add(auditorium));
        return "redirect:/auditoriums";
    }
}
