package beans.services;

import beans.models.*;
import beans.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import util.CsvUtil;
import util.exceptions.NotEnoughMoneyException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/3/2016
 * Time: 11:33 AM
 */
@Service
@PropertySource({"classpath:strategies/booking.properties"})
@Transactional
public class BookingServiceImpl implements BookingService {


    private final EventService      eventService;
    private final AuditoriumService auditoriumService;
    private final UserService       userService;
    private final BookingRepository bookingRepository;
    private final DiscountService   discountService;
    @Autowired
    private ExportService<Ticket> exportService;
    private final UserAccountService userAccountService;
    final         int               minSeatNumber;
    final         double            vipSeatPriceMultiplier;
    final         double            highRatedPriceMultiplier;
    final         double            defaultRateMultiplier;

    @Autowired
    public BookingServiceImpl(@Qualifier("eventService") EventService eventService,
                              AuditoriumService auditoriumService,
                              @Qualifier("userService") UserService userService,
                              @Qualifier("discountService") DiscountService discountService,
                              BookingRepository bookingRepository,
                              UserAccountService userAccountService,
                              @Value("${min.seat.number}") int minSeatNumber,
                              @Value("${vip.seat.price.multiplier}") double vipSeatPriceMultiplier,
                              @Value("${high.rate.price.multiplier}") double highRatedPriceMultiplier,
                              @Value("${def.rate.price.multiplier}") double defaultRateMultiplier){
        this.eventService = eventService;
        this.auditoriumService = auditoriumService;
        this.userService = userService;
        this.bookingRepository = bookingRepository;
        this.userAccountService = userAccountService;
        this.discountService = discountService;
        this.minSeatNumber = minSeatNumber;
        this.vipSeatPriceMultiplier = vipSeatPriceMultiplier;
        this.highRatedPriceMultiplier = highRatedPriceMultiplier;
        this.defaultRateMultiplier = defaultRateMultiplier;
    }

    @Override
    public double getTicketPrice(String eventName, String auditoriumName, LocalDateTime dateTime, List<Integer> seats,
                                 User user) {
        if (Objects.isNull(eventName)) {
            throw new NullPointerException("Event name is [null]");
        }
        if (Objects.isNull(seats)) {
            throw new NullPointerException("Seats are [null]");
        }
        if (Objects.isNull(user)) {
            throw new NullPointerException("User is [null]");
        }
        if (seats.contains(null)) {
            throw new NullPointerException("Seats contain [null]");
        }

        final Auditorium auditorium = auditoriumService.getByName(auditoriumName);

        final Event event = eventService.getEvent(eventName, auditorium, dateTime);
        if (Objects.isNull(event)) {
            throw new IllegalStateException(
                    "There is no event with name: [" + eventName + "] in auditorium: [" + auditorium + "] on date: ["
                    + dateTime + "]");
        }

        final double baseSeatPrice = event.getBasePrice();
        final double rateMultiplier = event.getRate() == Rate.HIGH ? highRatedPriceMultiplier : defaultRateMultiplier;
        final double seatPrice = baseSeatPrice * rateMultiplier;
        final double vipSeatPrice = vipSeatPriceMultiplier * seatPrice;
        final double discount = discountService.getDiscount(user, event);


        validateSeats(seats, auditorium);

        final List<Integer> auditoriumVipSeats = auditorium.getVipSeatsList();
        final List<Integer> vipSeats = auditoriumVipSeats.stream().filter(seats:: contains).collect(
                Collectors.toList());
        final List<Integer> simpleSeats = seats.stream().filter(seat -> !vipSeats.contains(seat)).collect(
                Collectors.toList());

        final double simpleSeatsPrice = simpleSeats.size() * seatPrice;
        final double vipSeatsPrice = vipSeats.size() * vipSeatPrice;

        //        System.out.println("auditoriumVipSeats = " + auditoriumVipSeats);
        //        System.out.println("baseSeatPrice = " + baseSeatPrice);
        //        System.out.println("rateMultiplier = " + rateMultiplier);
        //        System.out.println("vipSeatPriceMultiplier = " + vipSeatPriceMultiplier);
        //        System.out.println("seatPrice = " + seatPrice);
        //        System.out.println("vipSeatPrice = " + vipSeatPrice);
        //        System.out.println("discount = " + discount);
        //        System.out.println("seats = " + seats);
        //        System.out.println("simpleSeats.size() = " + simpleSeats.size());
        //        System.out.println("vipSeats.size() = " + vipSeats.size());

        final double totalPrice = simpleSeatsPrice + vipSeatsPrice;

        return (1.0 - discount) * totalPrice;
    }

    private void validateSeats(List<Integer> seats, Auditorium auditorium) {
        final int seatsNumber = auditorium.getSeatsNumber();
        final Optional<Integer> incorrectSeat = seats.stream().filter(
                seat -> seat < minSeatNumber || seat > seatsNumber).findFirst();
        incorrectSeat.ifPresent(seat -> {
            throw new IllegalArgumentException(
                    String.format("Seat: [%s] is incorrect. Auditorium: [%s] has [%s] seats", seat, auditorium.getName(),
                                  seatsNumber));
        });
    }

    @Override
    @Transactional(rollbackFor = {NotEnoughMoneyException.class, IllegalStateException.class,
            NullPointerException.class}, isolation = Isolation.SERIALIZABLE)
    public Ticket bookTicket(Ticket ticket) {
        if (Objects.isNull(ticket)) {
            return null;
        }
        User user = userService.getCurrentUser();
        UserAccount userAccount = userAccountService.findByUser(user);
        Stream<Ticket> bookedTickets = bookingRepository.getAllByTicketEvent(ticket.getEvent()).stream().map(Booking::getTicket);
        Ticket finalTicket = ticket;
        boolean seatsAreAlreadyBooked = bookedTickets
                .anyMatch(bookedTicket -> finalTicket.getSeatsList().stream().anyMatch(
                        bookedTicket.getSeatsList()::contains));
        if (!seatsAreAlreadyBooked) {
            Double money = userAccount.getMoney();
            Double rest = money - ticket.getPrice();
            if (rest < 0) {
                String message = String.format("User %s have't enough money %s for ticket price %s",
                        user.getEmail(), userAccount.getMoney(), ticket.getPrice());
                throw new IllegalStateException(message);
            }
            userAccount.setMoney(rest);
            userAccountService.save(userAccount);
            ticket.setUser(user);
            ticket = bookingRepository.save(new Booking(user, ticket)).getTicket();
        } else {
            throw new IllegalStateException("Unable to book ticket: [" + ticket + "]. Seats are already booked.");
        }

        return ticket;
    }

    @Override
    public List<Ticket> getTicketsForEvent(String event, String auditoriumName, LocalDateTime date) {
        final Auditorium auditorium = auditoriumService.getByName(auditoriumName);
        final Event foundEvent = eventService.getEvent(event, auditorium, date);
        return bookingRepository.getAllByTicketEvent(foundEvent).stream().map(Booking::getTicket).collect(Collectors.toList());
    }

    @Override
    public List<Integer> getAvailableSeats(String event, String auditoriumName, LocalDateTime date) {
        final int seatsNumber = auditoriumService.getByName(auditoriumName).getSeatsNumber();
        List<Integer> seatsList = IntStream.rangeClosed(1, seatsNumber).boxed().collect(Collectors.toList());
        List<Ticket> bookedTickets = getTicketsForEvent(event, auditoriumName, date);
        if (!bookedTickets.isEmpty()) {
            bookedTickets.forEach(seat -> seatsList.removeAll(CsvUtil.fromCsvToList(seat.getSeats(), Integer::new)));
        }
        return seatsList;
    }

    @Override
    public String exportTicketsToPdf(List<Ticket> tickets) {
        return exportService.exportEntities(tickets);
    }
}
