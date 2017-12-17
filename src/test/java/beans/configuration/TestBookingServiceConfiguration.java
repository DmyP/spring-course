package beans.configuration;

import beans.models.*;
import beans.repository.AuditoriumRepository;
import beans.repository.BookingRepository;
import beans.repository.EventRepository;
import beans.repository.UserRepository;
import beans.services.*;
import beans.services.discount.BirthdayStrategy;
import beans.services.discount.DiscountStrategy;
import beans.services.discount.TicketsStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.*;

import static beans.services.BookingServiceImplTest.RANDOM_STRING;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 13/2/16
 * Time: 3:36 PM
 */
@Configuration
public class TestBookingServiceConfiguration {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuditoriumRepository auditoriumRepository;
    @Autowired
    private EventService eventService;
    @Autowired
    private AuditoriumService auditoriumService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserAccountService userAccountService;

    @PostConstruct
    public void init() {
        auditoriumRepository.save(Arrays.asList(testHall1(), testHall2()));

        eventRepository.save(Arrays.asList(testEvent1(), testEvent2()));

        tickets().forEach(ticket ->
                bookingRepository.save(new Booking(ticket.getId(), testUser1(), ticket)));
    }

    @Bean
    public BookingService bookingServiceImpl() {
        return new BookingServiceImpl(eventService, auditoriumService, userService,
                discountService(), bookingRepository, userAccountService, 1,2, 1.2, 1);
    }

    @Bean
    public DiscountService discountService() {
        return new DiscountServiceImpl(Arrays.asList(birthdayBookingStrategy(), ticketsBookingStrategy()));
    }

    @Bean
    public DiscountStrategy birthdayBookingStrategy() {
        return new BirthdayStrategy(0.15, 0);
    }

    @Bean
    public DiscountStrategy ticketsBookingStrategy() {
        return new TicketsStrategy(bookingRepository, 0.5, 3, 0);
    }

    @Bean
    public Event testEvent1() {
        return new Event(1, "Test event", beans.models.Rate.HIGH, 124.0, java.time.LocalDateTime.of(2016, 2, 6, 14, 45, 0),
                         testHall1());
    }

    @Bean
    public Event testEvent2() {
        return new Event(2, "Test event2", Rate.MID, 500.0, java.time.LocalDateTime.of(2016, 12, 6, 9, 35, 0),
                         testHall2());
    }

    @Bean
    public User testUser1() {
        return new User(0, "dmitriy.vbabichev@gmail.com", "Dmytro Babichev", RANDOM_STRING, java.time.LocalDate.of(1992, 4, 29));
    }

    @Bean
    public User testUser2() {
        return new User(1, "laory@yandex.ru", "Dmytro Babichev", RANDOM_STRING, java.time.LocalDate.of(1992, 4, 29));
    }

    @Bean
    public UserAccount testUserAccount1() {
        return new UserAccount(testUser1());
    }

    @Bean
    public UserAccount testUserAccount2() {
        return new UserAccount(testUser2());
    }

    @Bean
    public Ticket testTicket1() {
        return new Ticket(1, testEvent1(), java.time.LocalDateTime.of(2016, 2, 6, 14, 45, 0), Arrays.asList(3, 4),
                          testUser1(), 32D);
    }

    @Bean
    public Ticket testTicket2() {
        return new Ticket(2, testEvent2(), java.time.LocalDateTime.of(2016, 2, 7, 14, 45, 0), Arrays.asList(1, 2),
                          testUser1(), 123D);
    }

    @Bean
    public List<Ticket> tickets() {
        return Arrays.asList(testTicket1(), testTicket2());
    }

    @Bean
    public Auditorium testHall1() {
        return new Auditorium(1, "Test auditorium", 15, Arrays.asList(1, 2, 3, 4, 5));
    }

    @Bean
    public Auditorium testHall2() {
        return new Auditorium(2, "Test auditorium 2", 8, Collections.singletonList(1));
    }
}
