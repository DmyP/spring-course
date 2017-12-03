package web.controller;

import beans.models.Ticket;
import beans.models.User;
import beans.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UsersController {

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    @GetMapping
    public String getBookedTickets(Model model) {
        List<Ticket> tickets = userService.getBookedTickets();
        model.addAttribute("tickets", tickets);
        return "tickets";
    }

    @GetMapping("/byId/{id}")
    public String getById(Model model, @PathVariable long id) {
        List<User> users = new ArrayList<>();
        users.add(userService.getById(id));
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/byEmail/{email}")
    public String getByEmail(Model model, @PathVariable String email) {
        List<User> users = new ArrayList<>();
        users.add(userService.getUserByEmail(email));
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/byName/{name}")
    public String getById(Model model, @PathVariable String name) {
        List<User> users = userService.getUsersByName(name);
        model.addAttribute("users", users);
        return "users";
    }
}
