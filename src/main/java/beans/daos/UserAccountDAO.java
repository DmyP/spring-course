package beans.daos;

import beans.models.User;
import beans.models.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountDAO extends JpaRepository<UserAccount, Long> {

    void setMoney(User user, Double money);

    Double getMoney(User user);
}
