package web.controller;

import beans.models.Ticket;
import beans.models.User;
import beans.services.UserAccountService;
import beans.services.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping
    public String getAll(Model model) {
        List<User> users = userService.getAll();
        model.addAttribute("users", users);
        model.addAttribute("header", "All Users");
        return "users";
    }

    @GetMapping("/current")
    public String getCurrent(Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("users", Arrays.asList(currentUser));
        model.addAttribute("header", "Current User  " + currentUser.getName());
        return "user";
    }

    @PostMapping("/current")
    public String addMoney(Model model, @RequestParam("money") double money) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        currentUser.getUserAccount().setMoney(currentUser.getUserAccount().getMoney() + money);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("header", "Current User  " + currentUser.getName());
        return "redirect:/user";
    }

    @GetMapping("/byId/{id}")
    public String getById(Model model, @PathVariable long id) {
        model.addAttribute("user", userService.getById(id));
        model.addAttribute("header", "User by id " + id);
        return "user";
    }

    @GetMapping("/byEmail/{email}")
    public String getByEmail(Model model, @PathVariable String email) {
        model.addAttribute("user", userService.getUserByEmail(email));
        model.addAttribute("header", "User by email " + email);
        return "user";
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
}
