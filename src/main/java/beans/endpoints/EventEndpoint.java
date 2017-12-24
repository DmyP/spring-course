package beans.endpoints;

import beans.models.Auditorium;
import beans.models.Event;
import beans.models.Rate;
import beans.services.AuditoriumService;
import beans.services.EventService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.time.LocalDateTime;
import java.util.Optional;

@Endpoint
public class EventEndpoint {

	private final EventService eventService;

	private final AuditoriumService auditoriumService;

	public EventEndpoint(EventService eventService, AuditoriumService auditoriumService) {
		this.eventService = eventService;
		this.auditoriumService = auditoriumService;
	}

	@PayloadRoot(namespace ="default", localPart = "createEvent")
	@ResponsePayload
	public Event create() {
		Optional<Auditorium> found = Optional.empty();
		for (Auditorium auditorium1 : auditoriumService.getAuditoriums()) {
			found = Optional.of(auditorium1);
			break;
		}
		Auditorium auditorium = found.get();
		return eventService.create(new Event("name", Rate.HIGH, 100d, LocalDateTime.now(), auditorium));
	}
}
