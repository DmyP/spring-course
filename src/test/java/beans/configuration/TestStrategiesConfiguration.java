package beans.configuration;

import beans.models.User;
import beans.repository.BookingRepository;
import beans.repository.UserRepository;
import beans.services.DiscountService;
import beans.services.DiscountServiceImpl;
import beans.services.discount.BirthdayStrategy;
import beans.services.discount.TicketsStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 13/2/16
 * Time: 3:36 PM
 */
@Configuration
public class TestStrategiesConfiguration {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @PostConstruct
    public void init() {
        userRepository
                .save(new User("test", "test", "test", LocalDate.now()));
    }

    @Bean
    public BirthdayStrategy birthdayStrategy() {
        return new BirthdayStrategy(1.0, 0);
    }

    @Bean
    public TicketsStrategy ticketsStrategy() {
        return new TicketsStrategy(bookingRepository, 0.5, 2, 0);
    }

    @Bean
    public DiscountService discountServiceImpl() {
        return new DiscountServiceImpl(Arrays.asList(birthdayStrategy(), ticketsStrategy()));
    }
}
