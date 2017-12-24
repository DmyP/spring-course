package beans.endpoints;

import beans.models.Auditorium;
import beans.models.User;
import beans.services.UserService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.Optional;

@Endpoint
public class UserEndpoint {

	private final UserService userService;

	public UserEndpoint(UserService userService) {
		this.userService = userService;
	}

	@PayloadRoot(namespace ="default", localPart = "createUser")
	@ResponsePayload
	public User create() {
		return userService.register(new User());
	}
}
