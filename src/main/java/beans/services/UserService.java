package beans.services;

import beans.models.Ticket;
import beans.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/1/2016
 * Time: 7:32 PM
 */
public interface UserService extends UserDetailsService {

    User register(User user);

    void remove(User user);

    List<User> getAll();

    User getById(long id);

    User getUserByEmail(String email);

    List<User> getUsersByName(String name);

    List<Ticket> getBookedTickets();

    User getCurrentUser();
}
