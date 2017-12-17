package beans.repository;

import beans.models.Booking;
import beans.models.Event;
import beans.models.Ticket;
import beans.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

	Long countByUser(User user);

	List<Booking> getAllByTicketEvent(Event event);

	static void validateUser(User user) {
		if (Objects.isNull(user)) {
			throw new NullPointerException("User is [null]");
		}
		if (Objects.isNull(user.getEmail())) {
			throw new NullPointerException("User email is [null]");
		}
	}

	static void validateTicket(Ticket ticket) {
		if (Objects.isNull(ticket)) {
			throw new NullPointerException("Ticket is [null]");
		}
	}
}
