package beans.services;

import beans.models.User;
import beans.models.UserAccount;

public interface UserAccountService {

    UserAccount findById(long id);

    UserAccount findByUser(User user);

    UserAccount addMoney(User user, Double money);

    UserAccount withdrawMoney(User user, Double money);

    UserAccount create(User user);
}
