package beans.services;

import beans.models.Auditorium;
import beans.models.Event;
import beans.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/2/2016
 * Time: 12:29 PM
 */
@Service
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Event create(Event event) {
        Event newEvent = eventRepository.findByAuditoriumAndDateTime(event.getAuditorium(), event.getDateTime());
        if (Objects.nonNull(newEvent)) {
            throw new IllegalStateException("Unable to store event");
        }
        return eventRepository.save(event);
    }

    @Transactional
    public void remove(Event event) {
        if (Objects.nonNull(event)) {
            eventRepository.delete(event);
            eventRepository.flush();
        }
    }

    @Override
    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    @Override
    public Event getById(Long id) {
        if (Objects.isNull(id)) {
            return null;
        }
        return eventRepository.getOne(id);
    }
    @Override
    public List<Event> getByName(String name) {
        if (Objects.isNull(name)) {
            return null;
        }
        return eventRepository.getByName(name);
    }

    @Override
    public Event getEvent(String name, Auditorium auditorium, LocalDateTime dateTime) {
        if (Objects.isNull(name) || Objects.isNull(auditorium) || Objects.isNull(dateTime)) {
            return null;
        }
        return eventRepository.getByNameAndAuditoriumAndDateTime(name, auditorium, dateTime);
    }

    @Override
    public List<Event> getForDateRange(LocalDateTime from, LocalDateTime to) {
        if (Objects.isNull(from) || Objects.isNull(to)) {
            return null;
        }
        return eventRepository.getByDateTimeBetween(from, to);
    }

    @Override
    public List<Event> getNextEvents(LocalDateTime to) {
        if (Objects.isNull(to)) {
            return null;
        }
        return eventRepository.getByDateTimeTo(to);
    }

    @Override
    public Event assignAuditorium(Event event, Auditorium auditorium, LocalDateTime date) {
        final Event updatedEvent = new Event(event.getId(), event.getName(), event.getRate(), event.getBasePrice(), date, auditorium);
        return eventRepository.save(updatedEvent);
    }
}
