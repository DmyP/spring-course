package beans.aspects;

import beans.aspects.mocks.DiscountAspectMock;
import beans.configuration.TestAspectsConfiguration;
import beans.models.Event;
import beans.models.Ticket;
import beans.models.User;
import beans.repository.AuditoriumRepository;
import beans.repository.BookingRepository;
import beans.repository.EventRepository;
import beans.repository.UserRepository;
import beans.services.BookingService;
import beans.services.EventService;
import beans.services.discount.BirthdayStrategy;
import beans.services.discount.TicketsStrategy;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 13/2/16
 * Time: 7:20 PM
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestAspectsConfiguration.class})
@Transactional
public class TestDiscountAspect {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private EventService eventService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditoriumRepository auditoriumRepository;

    @Before
    public void init() {
        DiscountAspectMock.cleanup();
    }

    @After
    public void cleanup() {
        DiscountAspectMock.cleanup();
        auditoriumRepository.deleteAll();
        userRepository.deleteAll();
        eventRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    public void testCalculateDiscount() {
        Event event = (Event) applicationContext.getBean("testEvent1");
        User user = (User) applicationContext.getBean("testUser1");
        User discountUser = new User(user.getId(), user.getEmail(), user.getName(), user.getPassword(),
                LocalDate.now());
        Ticket ticket1 = (Ticket) applicationContext.getBean("testTicket1");
        bookingService.bookTicket(new Ticket(ticket1.getEvent(), ticket1.getDateTime(), Arrays.asList(5, 6), user, ticket1.getPrice()));
        bookingService.bookTicket(new Ticket(ticket1.getEvent(), ticket1.getDateTime(), Arrays.asList(7, 8), user, ticket1.getPrice()));
        bookingService.bookTicket(new Ticket(ticket1.getEvent(), ticket1.getDateTime(), Arrays.asList(9, 10), user, ticket1.getPrice()));
        List<Integer> seats = Arrays.asList(1, 2, 3, 4);
        bookingService
                .getTicketPrice(event.getName(), event.getAuditorium().getName(), event.getDateTime(), seats, discountUser);
        bookingService
                .getTicketPrice(event.getName(), event.getAuditorium().getName(), event.getDateTime(), seats, discountUser);
        bookingService
                .getTicketPrice(event.getName(), event.getAuditorium().getName(), event.getDateTime(), seats, discountUser);
        bookingService
                .getTicketPrice(event.getName(), event.getAuditorium().getName(), event.getDateTime(), seats, discountUser);
        HashMap<String, Map<String, Integer>> expected = new HashMap<String, Map<String, Integer>>() {{
            put(TicketsStrategy.class.getSimpleName(), new HashMap<String, Integer>() {{
                put(user.getEmail(), 4);
            }});
            put(BirthdayStrategy.class.getSimpleName(), new HashMap<String, Integer>() {{
                put(user.getEmail(), 4);
            }});
        }};
        Assert.assertEquals(expected, DiscountAspect.getDiscountStatistics());
    }
}
