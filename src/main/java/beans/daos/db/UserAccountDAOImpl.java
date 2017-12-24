package beans.daos.db;

import beans.daos.AbstractDAO;
import beans.daos.UserAccountDAO;
import beans.models.User;
import beans.models.UserAccount;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class UserAccountDAOImpl extends AbstractDAO implements UserAccountDAO {

   @Override
    public UserAccount create(User user) {
        if (Objects.isNull(user)) {
            throw new IllegalStateException("Unable to store");
        } else {
            UserAccount account = new UserAccount(user);

            getCurrentSession().save(account);
            return account;
        }
    }

    @Override
    public UserAccount findById(long id) {
        return getCurrentSession().get(UserAccount.class, id);
    }

    @Override
    public UserAccount findByUser(User user) {
        return findById(user.getId());
    }

    @Override
    public void setMoney(User user, Double money) {
        getCurrentSession().get(UserAccount.class, user).setMoney(money);

    }

    @Override
    public Double getMoney(User user) {
        return getCurrentSession().get(UserAccount.class, user).getMoney();
    }
}
