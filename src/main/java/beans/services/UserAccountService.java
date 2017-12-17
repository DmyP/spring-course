package beans.services;

import beans.models.User;
import beans.models.UserAccount;

public interface UserAccountService {

    void save(UserAccount userAccount);

    UserAccount addMoney(User user, Double money);

    UserAccount withdrawMoney(User user, Double money);

    UserAccount findByUser(User user);

}
