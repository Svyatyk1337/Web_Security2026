package chnu.edu.ua.web_application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	@Bean
	UserDetailsService userDetailsService() {
		return new InMemoryUserDetailsManager(
				User.withUsername("admin")
						.password("{noop}admin")
						.roles("USER")
						.build()
		);
	}

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	http
		.csrf(csrf -> csrf.ignoringRequestMatchers("/login"))
		.authorizeHttpRequests(auth -> auth
				.requestMatchers("/login.html", "/error").permitAll()
			.anyRequest().authenticated())
		.formLogin(form -> form
				.loginPage("/login.html")
			.loginProcessingUrl("/login")
			.defaultSuccessUrl("/api/v1/transfers", true)
			.permitAll())
		.httpBasic(Customizer.withDefaults())
		.logout(logout -> logout
			.logoutSuccessUrl("/login.html?logout")
			.permitAll());

	return http.build();
    }
}
