package web.controller;

import beans.models.Event;
import beans.models.User;
import beans.services.EventService;
import beans.services.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/upload")
public class UploadController {

	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;

	@Autowired
	@Qualifier("eventServiceImpl")
	private EventService eventService;

	@Autowired
	private ObjectMapper objectMapper;

	@RequestMapping
	public String upload(Model model) {
		model.addAttribute("message", "");
		return "upload";
	}

	@PostMapping("/events")
	public String eventsUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {
		Event[] events = objectMapper.readValue(file.getInputStream(), Event[].class);
		List<Event> list = Arrays.asList(events);
		list.forEach(event -> eventService.create(event));
		return "redirect:/events";
	}

	@PostMapping("/users")
	public String usersUpload(@RequestParam("file") MultipartFile file) throws IOException {
		User[] users = objectMapper.readValue(file.getInputStream(), User[].class);
		List<User> list = Arrays.asList(users);
		for (User user : list) {
			userService.register(user);
		}
		return "redirect:/users";
	}
}
