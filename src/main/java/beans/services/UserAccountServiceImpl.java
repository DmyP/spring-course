package beans.services;

import beans.models.User;
import beans.models.UserAccount;
import beans.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.exceptions.NotEnoughMoneyException;

import java.util.Objects;

@Service
@Transactional
public class UserAccountServiceImpl implements UserAccountService {

    private UserAccountRepository userAccountRepository;

    @Autowired
    public UserAccountServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

//TODO del
    @Override
    public void save(UserAccount userAccount) {
        userAccountRepository.save(userAccount);
    }

    @Override
    public UserAccount findByUser(User user) {
        return userAccountRepository.findByUser(user);
    }

    @Override
    public UserAccount addMoney(User user, Double money) {
        if (Objects.isNull(user) || Objects.isNull(money) || Objects.isNull(findByUser(user))) {
            return null;
        }
        UserAccount userAccount = findByUser(user);
        userAccount.setMoney(userAccount.getMoney() + money);
        return userAccount;
    }

    @Override
    public UserAccount withdrawMoney(User user, Double money) {
        if (Objects.isNull(user) || Objects.isNull(money) || Objects.isNull(findByUser(user))) {
            return null;
        }
        UserAccount userAccount = findByUser(user);
        if (userAccount.getMoney() < money) {
            throw new NotEnoughMoneyException();
        }
        userAccount.setMoney(userAccount.getMoney() - money);
        return userAccount;
    }

}
