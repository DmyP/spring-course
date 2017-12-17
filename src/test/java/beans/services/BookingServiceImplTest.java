package beans.services;

import beans.configuration.AppConfiguration;
import beans.configuration.TestBookingServiceConfiguration;
import beans.configuration.db.DataSourceConfiguration;
import beans.configuration.db.DbSessionFactory;
import beans.models.Event;
import beans.models.Ticket;
import beans.models.User;
import beans.repository.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 06/2/16
 * Time: 8:28 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfiguration.class, DataSourceConfiguration.class, DbSessionFactory.class,
                                 TestBookingServiceConfiguration.class})
@Transactional
public class BookingServiceImplTest {

    public static final String RANDOM_STRING = UUID.randomUUID().toString();

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private AuditoriumRepository auditoriumRepository;

    @After
    public void cleanup() {
        auditoriumRepository.deleteAll();
        userRepository.deleteAll();
        eventRepository.deleteAll();
        bookingRepository.deleteAll();
    }

    @Test
    public void testGetTicketsForEvent() throws Exception {
        Event testEvent1 = (Event) applicationContext.getBean("testEvent1");
        Ticket ticket = (Ticket) applicationContext.getBean("testTicket1");
        List<Ticket> ticketsForEvent = bookingService.getTicketsForEvent(testEvent1.getName(),
                                                                         testEvent1.getAuditorium().getName(),
                                                                         testEvent1.getDateTime());
        assertEquals("Tickets should match", Arrays.asList(ticket), ticketsForEvent);
    }

    @Test(expected = RuntimeException.class)
    public void testBookTicket_NotRegistered() throws Exception {
        Event testEvent1 = (Event) applicationContext.getBean("testEvent1");
        List<Ticket> before = bookingService.getTicketsForEvent(testEvent1.getName(),
                                                                testEvent1.getAuditorium().getName(),
                                                                testEvent1.getDateTime());
        User newUser = new User(RANDOM_STRING, RANDOM_STRING, RANDOM_STRING, LocalDate.now());
        Ticket newTicket = new Ticket(testEvent1, LocalDateTime.now(), Arrays.asList(3, 4), newUser, 0.0);
        bookingService.bookTicket(newTicket);
    }

    @Test(expected = RuntimeException.class)
    public void testBookTicket_AlreadyBooked() throws Exception {
        Event testEvent1 = (Event) applicationContext.getBean("testEvent1");
        List<Ticket> before = bookingService.getTicketsForEvent(testEvent1.getName(),
                                                                testEvent1.getAuditorium().getName(),
                                                                testEvent1.getDateTime());
        User testUser2 = (User) applicationContext.getBean("testUser2");
        Ticket newTicket = new Ticket(testEvent1, LocalDateTime.now(), Arrays.asList(3, 4), testUser2, 0.0);
        bookingService.bookTicket(newTicket);
    }

    @Test
    public void testBookTicket() throws Exception {
        Event testEvent1 = (Event) applicationContext.getBean("testEvent1");
        List<Ticket> before = bookingService.getTicketsForEvent(testEvent1.getName(),
                                                                testEvent1.getAuditorium().getName(),
                                                                testEvent1.getDateTime());
        User testUser1 = (User) applicationContext.getBean("testUser1");
        Ticket newTicket = new Ticket(testEvent1, LocalDateTime.now(), Arrays.asList(5, 6), testUser1, 0.0);
        Ticket bookedTicket = bookingService.bookTicket(newTicket);
        List<Ticket> after = bookingService.getTicketsForEvent(testEvent1.getName(),
                                                               testEvent1.getAuditorium().getName(),
                                                               testEvent1.getDateTime());
        before.add(bookedTicket);
        assertTrue("Events should change", after.containsAll(before));
        assertTrue("Events should change", before.containsAll(after));
    }

    @Test
    public void testGetTicketPrice() throws Exception {
        Ticket ticket = (Ticket) applicationContext.getBean("testTicket1");
        Event event = ticket.getEvent();
        double ticketPrice = bookingService.getTicketPrice(event.getName(), event.getAuditorium().getName(),
                                                           event.getDateTime(), ticket.getSeatsList(),
                                                           ticket.getUser());
        Assert.assertEquals("Price is wrong", 297.6, ticketPrice, 0.00001);
    }

    @Test
    public void testGetTicketPrice_WithoutDiscount() throws Exception {
        Ticket ticket = (Ticket) applicationContext.getBean("testTicket1");
        User user = userRepository.save(new User("dadsada", "asdasda", RANDOM_STRING, LocalDate.now().minus(1, ChronoUnit.DAYS)));
        Event event = ticket.getEvent();
        double ticketPrice = bookingService.getTicketPrice(event.getName(), event.getAuditorium().getName(),
                                                           event.getDateTime(), ticket.getSeatsList(), user);
        Assert.assertEquals("Price is wrong", 595.2, ticketPrice, 0.00001);
    }

    @Test
    public void testGetTicketPrice_DiscountsForTicketsAndForBirthday() throws Exception {
        Ticket ticket = (Ticket) applicationContext.getBean("testTicket1");
        User testUser = new User(RANDOM_STRING, RANDOM_STRING, RANDOM_STRING, LocalDate.now());
        User registeredUser = userRepository.save(testUser);
        bookingService.bookTicket(new Ticket(ticket.getEvent(), LocalDateTime.now(), Collections.singletonList(1),
                registeredUser, 0.0));
        bookingService.bookTicket(new Ticket(ticket.getEvent(), LocalDateTime.now(),
                Collections.singletonList(2), registeredUser, 0.0));
        Event event = ticket.getEvent();
        double ticketPrice = bookingService.getTicketPrice(event.getName(), event.getAuditorium().getName(),
                                                           event.getDateTime(), Arrays.asList(5, 6, 7, 8),
                                                           registeredUser);
        Assert.assertEquals("Price is wrong", 260.4, ticketPrice, 0.00001);
    }

    @Test
    public void testGetTicketPrice_DiscountsForTicketsAndForBirthday_MidRate() throws Exception {
        Ticket ticket = (Ticket) applicationContext.getBean("testTicket2");
        User testUser = new User(RANDOM_STRING, RANDOM_STRING, RANDOM_STRING, LocalDate.now());
        User registeredUser = userRepository.save(testUser);
        bookingService.bookTicket(new Ticket(ticket.getEvent(), LocalDateTime.now(),
                Collections.singletonList(3), registeredUser, 0.0));
        bookingService.bookTicket(new Ticket(ticket.getEvent(), LocalDateTime.now(),
                Collections.singletonList(4), registeredUser, 0.0));
        Event event = ticket.getEvent();
        double ticketPrice = bookingService.getTicketPrice(event.getName(), event.getAuditorium().getName(),
                                                           event.getDateTime(), Arrays.asList(5, 6, 7), registeredUser);
        Assert.assertEquals("Price is wrong", 525, ticketPrice, 0.00001);
    }
}
