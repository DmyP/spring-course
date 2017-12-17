package beans.repository;

import beans.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User getByEmail(String email);

	List<User> getAllByName(String name);

	static void validateUser(User user) {
		if (Objects.isNull(user)) {
			throw new NullPointerException("User is [null]");
		}
		if (Objects.isNull(user.getEmail())) {
			throw new NullPointerException("User's email is [null]. User: [" + user + "]");
		}
		if (Objects.isNull(user.getName())) {
			throw new NullPointerException("User's name is [null]. User: [" + user + "]");
		}
	}

}
