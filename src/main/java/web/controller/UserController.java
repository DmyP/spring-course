package web.controller;

import beans.models.Ticket;
import beans.models.User;
import beans.services.UserService;
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
@RequestMapping("/user")
public class UserController {

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/byId/{id}")
    public String getById(Model model, @PathVariable long id) {
        model.addAttribute("user", userService.getById(id));
        model.addAttribute("header", "User by id " + id);
        return "users";
    }

    @GetMapping("/byEmail/{email}")
    public String getByEmail(Model model, @PathVariable String email) {
        model.addAttribute("user", userService.getUserByEmail(email));
        model.addAttribute("header", "User by email " + email);
        return "users";
    }

    @GetMapping("/byName/{name}")
    public String getByName(Model model, @PathVariable String name) {
        List<User> users = userService.getUsersByName(name);
        model.addAttribute("users", users);
        model.addAttribute("header", "Users by name " + name);
        return "users";
    }

    @GetMapping("/tickets")
    public String getBookedTickets(Model model) {
        List<Ticket> tickets = userService.getBookedTickets();
        model.addAttribute("header", "Users tickets");
        model.addAttribute("tickets", tickets);
        return "tickets";
    }

    @PostMapping("/load")
    public String load(@RequestParam("file") MultipartFile file) throws IOException {
        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf('.'));
        File tmpFile = File.createTempFile("tmp", ext);
        List<User> users = objectMapper.readValue(tmpFile, new TypeReference<List<User>>(){});
        users.forEach(user -> userService.register(user));
        return "redirect:/users";
    }
}
