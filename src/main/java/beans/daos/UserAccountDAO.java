package beans.daos;

import beans.models.User;
import beans.models.UserAccount;

public interface UserAccountDAO {

    UserAccount create(User user);

    UserAccount findById(long id);

    UserAccount findByUser(User user);

    void setMoney(User user, Double money);

    Double getMoney(User user);
}
