package web.config;

import beans.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static beans.models.Authorities.BOOKING_MANAGER;
import static beans.models.Authorities.REGISTERED_USER;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.httpBasic()
			.and()
			.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/upload/**").permitAll()
				.antMatchers("/ws/**").permitAll()
				.antMatchers("/book/tickets").hasAuthority(BOOKING_MANAGER.toString())
				.anyRequest().hasAuthority(REGISTERED_USER.toString())
			.and()
			.formLogin()
				.loginPage("/login")
				.permitAll()
			.and()
				.logout()
				.addLogoutHandler(customLogoutHandler())
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/")
			.and()
			.rememberMe()
				.tokenRepository(persistentTokenRepository())
				.tokenValiditySeconds(5184000)
			.and()
			.csrf()
				.disable();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider
				= new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new CustomPasswordEncoder();
	}

	@Bean
	public CustomLogoutHandler customLogoutHandler() {
		return new CustomLogoutHandler();
	}

	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		return new InMemoryTokenRepositoryImpl();
	}
}
