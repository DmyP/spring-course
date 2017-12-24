package beans.repository;

import beans.models.Auditorium;
import beans.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

	Event getByNameAndAuditoriumAndDateTime(String eventName, Auditorium auditorium, LocalDateTime dateTime);

	Event findByAuditoriumAndDateTime(Auditorium auditorium, LocalDateTime dateTime);

	List<Event> getByName(String name);

	List<Event> getByDateTimeBetween(LocalDateTime start, LocalDateTime end);

	List<Event> getByDateTimeTo(LocalDateTime to);

	static void validateEvent(Event event) {
		if (Objects.isNull(event)) {
			throw new NullPointerException("Event is [null]");
		}
		if (Objects.isNull(event.getName())) {
			throw new NullPointerException("Event's name is [null]. Event: [" + event + "]");
		}
		if (Objects.isNull(event.getRate())) {
			throw new NullPointerException("Events's rate is [null]. Event: [" + event + "]");
		}
	}

}
