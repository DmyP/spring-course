package beans.services;

import beans.daos.UserAccountDAO;
import beans.daos.UserDAO;
import beans.models.User;
import beans.models.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userAccountService")
@Transactional
public class UserAccountServiceImpl implements UserAccountService {

    private UserAccountDAO userAccountDAO;

    public UserAccountServiceImpl(UserAccountDAO userAccountDAO) {
        this.userAccountDAO = userAccountDAO;
    }

    @Override
    public UserAccount findById(long id) {
        return userAccountDAO.findById(id);
    }

    @Override
    public UserAccount findByUser(User user) {
        return userAccountDAO.findByUser(user);
    }

    @Override
    public UserAccount addMoney(User user, Double money) {
        UserAccount userAccount = findByUser(user);
        if (userAccount == null) {
            return null;
        }
        userAccount.setMoney(userAccount.getMoney() + money);
        return userAccount;
    }

    @Override
    public UserAccount withdrawMoney(User user, Double money) {
        UserAccount userAccount = findByUser(user);
        if (userAccount == null || userAccount.getMoney() < money) {
            return null;
        }
        userAccount.setMoney(userAccount.getMoney() - money);
        return userAccount;
    }

    @Override
    public void save(UserAccount userAccount) {
        userAccountDAO.save(userAccount);
    }
}
