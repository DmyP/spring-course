package beans.services.discount;

import beans.models.User;
import beans.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/4/2016
 * Time: 11:25 AM
 */
@Component("ticketsStrategy")
@PropertySource({"classpath:strategies/strategies.properties"})
@Transactional
public class TicketsStrategy implements DiscountStrategy {

    public final BookingRepository bookingRepository;
    public final double     ticketsDiscountValue;
    public final int        discountThreshold;
    public final double     defaultDiscount;

    @Autowired
    public TicketsStrategy(BookingRepository bookingRepository,
                           @Value("${tickets.discount}") double ticketsDiscountValue,
                           @Value("${tickets.discount.threshold}") int discountThreshold,
                           @Value("${tickets.discount.default}") double defaultDiscount) {
        this.bookingRepository = bookingRepository;
        this.ticketsDiscountValue = ticketsDiscountValue;
        this.discountThreshold = discountThreshold;
        this.defaultDiscount = defaultDiscount;
    }

    @Override
    public double calculateDiscount(User user) {
        final long boughtTicketsCount = bookingRepository.countByUser(user);
        if ((boughtTicketsCount + 1) % discountThreshold == 0) {
            return ticketsDiscountValue;
        }
        return defaultDiscount;
    }
}
