package beans.configuration;

import beans.models.User;
import beans.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Arrays;

import static beans.services.BookingServiceImplTest.RANDOM_STRING;

/**
 * Created with IntelliJ IDEA.
 * User: Dmytro_Babichev
 * Date: 2/12/2016
 * Time: 1:36 PM
 */
@Configuration
public class TestUserServiceConfiguration {
    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        userRepository.save(Arrays.asList(testUser1(), testUser2()));
    }

    @Bean
    public User testUser1() {
        return new User(0, "dmitriy.vbabichev@gmail.com", "Dmytro Babichev", RANDOM_STRING,java.time.LocalDate.of(1992, 4, 29));
    }

    @Bean
    public User testUser2() {
        return new User(1, "laory@yandex.ru", "Dmytro Babichev", RANDOM_STRING, java.time.LocalDate.of(1992, 4, 29));
    }
}
