package beans.services;

import beans.daos.UserDAO;
import beans.models.Ticket;
import beans.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/1/2016
 * Time: 7:30 PM
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(@Qualifier("userDAO") UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userDAO.create(user);
    }

    @Override
    public void remove(User user) {
        userDAO.delete(user);
    }

    @Override
    public User getById(long id) {
        return userDAO.get(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userDAO.getByEmail(email);
    }

    @Override
    public List<User> getUsersByName(String name) {
        return userDAO.getAllByName(name);
    }

    @Override
    public List<Ticket> getBookedTickets() {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public List<User> getAll() {
        return userDAO.getAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDAO.getByEmail(username);
    }
}
