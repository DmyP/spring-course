package beans.services;

import beans.daos.EventDAO;
import beans.models.Auditorium;
import beans.models.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/2/2016
 * Time: 12:29 PM
 */
@Service("eventServiceImpl")
@Transactional
public class EventServiceImpl implements EventService {

    private final EventDAO eventDAO;

    @Autowired
    public EventServiceImpl(@Qualifier("eventDAO") EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

    @Override
    public Event create(Event event) {
        return eventDAO.create(event);
    }

    @Override
    public void remove(Event event) {
        eventDAO.delete(event);
    }

    @Override
    public List<Event> getByName(String name) {
        return eventDAO.getByName(name);
    }

    @Override
    public Event getEvent(String name, Auditorium auditorium, LocalDateTime dateTime) {
        return eventDAO.get(name, auditorium, dateTime);
    }

    @Override
    public List<Event> getAll() {
        return eventDAO.getAll();
    }

    @Override
    public List<Event> getForDateRange(LocalDateTime from, LocalDateTime to) {
        return eventDAO.getForDateRange(from, to);
    }

    @Override
    public List<Event> getNextEvents(LocalDateTime to) {
        return eventDAO.getNext(to);
    }

    @Override
    public Event assignAuditorium(Event event, Auditorium auditorium, LocalDateTime date) {
        final Event updatedEvent = new Event(event.getId(), event.getName(), event.getRate(), event.getBasePrice(), date, auditorium);
        return eventDAO.update(updatedEvent);
    }
}
