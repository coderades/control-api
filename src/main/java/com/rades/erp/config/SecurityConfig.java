package com.rades.erp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.rades.erp.repository.RoleRepository;
import com.rades.erp.service.AuthService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final AuthService authService;
	private final RoleRepository roleRepository;

	@Autowired
	public SecurityConfig(RoleRepository roleRepository, AuthService authService) {
		this.authService = authService;
		this.roleRepository = roleRepository;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().and().httpBasic().and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/api/logout")).invalidateHttpSession(true);

		roleRepository.findByHasAnyRole("f9a1da70-68a4-eb11-a3d3-6245b4ea43a3").forEach(method -> {
			try {
				if (Boolean.parseBoolean(method[0].toString())) {
					switch (method[1].toString().toLowerCase()) {
					case "r":
						http.authorizeRequests().antMatchers(HttpMethod.GET, method[2].toString()).permitAll();
						break;
					case "w":
						http.authorizeRequests().antMatchers(HttpMethod.POST, method[2].toString()).permitAll()
								.antMatchers(HttpMethod.PUT, method[2].toString()).permitAll()
								.antMatchers(HttpMethod.PATCH, method[2].toString()).permitAll()
								.antMatchers(HttpMethod.DELETE, method[2].toString()).permitAll();
						break;
					}
				} else {
					switch (method[1].toString().toLowerCase()) {
					case "r":
						http.authorizeRequests().antMatchers(HttpMethod.GET, method[2].toString())
								.hasRole(method[3].toString());
						break;
					case "w":
						http.authorizeRequests().antMatchers(HttpMethod.POST, method[2].toString())
								.hasRole(method[3].toString()).antMatchers(HttpMethod.PUT, method[2].toString())
								.hasRole(method[3].toString()).antMatchers(HttpMethod.PATCH, method[2].toString())
								.hasRole(method[3].toString()).antMatchers(HttpMethod.DELETE, method[2].toString())
								.hasRole(method[3].toString());
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// In memory
		auth.inMemoryAuthentication().withUser("admin")
				.password(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode("admin")).roles("Manager");

		// In database
		auth.userDetailsService(authService).passwordEncoder(new BCryptPasswordEncoder());
	}
}
