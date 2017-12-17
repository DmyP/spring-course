package beans.daos.db;

import beans.daos.AbstractDAO;
import beans.daos.UserAccountDAO;
import beans.daos.UserDAO;
import beans.models.User;
import beans.models.UserAccount;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Repository(value = "userAccountDAO")
@Transactional()
public class UserAccountDAOImpl extends AbstractDAO implements UserAccountDAO {

   @Override
    public UserAccount create(UserAccount userAccount) {
        if (Objects.nonNull(userAccount)) {
            throw new IllegalStateException("Unable to store");
        } else {
            getCurrentSession().save(userAccount);
            return userAccount;
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

    @Override
    public void save(UserAccount userAccount) {
        openSession().saveOrUpdate(userAccount);
    }
}
