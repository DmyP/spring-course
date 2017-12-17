package beans.services;

import beans.models.Ticket;
import beans.models.User;
import beans.models.UserAccount;
import beans.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserAccountService userAccountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserAccountService userAccountService) {
        this.userRepository = userRepository;
        this.userAccountService = userAccountService;
    }

    @Override
    public User register(User user) {
        if (Objects.nonNull(userRepository.getByEmail(user.getEmail()))) {
            throw new IllegalStateException("User with same email exist in database");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        userAccountService.save(new UserAccount(user));
        return user;
    }

    @Override
    public void remove(User user) {
        if (Objects.nonNull(user)){
            userRepository.delete(user);
        }
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(long id) {
        return userRepository.getOne(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    @Override
    public List<User> getUsersByName(String name) {
        return userRepository.getAllByName(name);
    }

    @Override
    public List<Ticket> getBookedTickets() {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getByEmail(username);
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object user = authentication.getPrincipal();
        if (Objects.nonNull(user) && user instanceof User) {
            return (User) user;
        } else {
            throw new AccessDeniedException("User is not authenticated");
        }
    }
}