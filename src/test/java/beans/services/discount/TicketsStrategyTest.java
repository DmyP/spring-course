package beans.services.discount;

import beans.models.User;
import beans.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static beans.services.BookingServiceImplTest.RANDOM_STRING;
import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 06/2/16
 * Time: 2:16 AM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = beans.configuration.TestStrategiesConfiguration.class)
public class TicketsStrategyTest {

    @Autowired
    @Qualifier("ticketsStrategy")
    private TicketsStrategy ticketsStrategy;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCalculateDiscount_UserHasDiscount() throws Exception {
        System.out.println(ticketsStrategy.getClass());
        User userWithDiscount = new User("test", "test", "test", LocalDate.now());
        userWithDiscount = userRepository.save(userWithDiscount);
        double discount = ticketsStrategy.calculateDiscount(userWithDiscount);
        assertEquals("User: [" + userWithDiscount + "] has tickets discount", ticketsStrategy.ticketsDiscountValue, discount,
                0.00001);
    }

    @Test
    public void testCalculateDiscount_UserHasNoDiscount() throws Exception {
        User userWithoutDiscount = new User("test@ema.il", "Test Name 2", RANDOM_STRING, LocalDate.now().minus(1, ChronoUnit.DAYS));
        double discount = ticketsStrategy.calculateDiscount(userWithoutDiscount);
        assertEquals("User: [" + userWithoutDiscount + "] doesn't have tickets discount", ticketsStrategy.defaultDiscount, discount, 0.00001);
    }
}
