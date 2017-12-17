package beans.services;

import beans.daos.UserAccountDAO;
import beans.daos.UserDAO;
import beans.daos.db.UserAccountDAOImpl;
import beans.models.Ticket;
import beans.models.User;
import beans.models.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/1/2016
 * Time: 7:30 PM
 */
@Service("userServiceImpl")
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final UserAccountService userAccountService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDAO userDAO, UserAccountService userAccountService) {
        this.userDAO = userDAO;
        this.userAccountService = userAccountService;
    }

    @Override
    public User register(User user) {
        if (Objects.nonNull(userDAO.getByEmail(user.getEmail()))) {
            throw new IllegalStateException("User with same email exist in database");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userDAO.create(user);
        userAccountService.save(new UserAccount(user));
        return user;
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
