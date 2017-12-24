package beans.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class UserAccount {

	@Id
	@GeneratedValue
	private Long id;
	@JsonProperty("money")
	private Double money;
	@OneToOne(fetch = FetchType.EAGER)
	private User user;

	public UserAccount() {
		this.money = 0D;
	}

	public UserAccount(User user) {
		this.id = user.getId();
		this.money = 0D;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return money.toString();
	}
}
