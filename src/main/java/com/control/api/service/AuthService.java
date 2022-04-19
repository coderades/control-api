package com.control.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.control.api.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional @Slf4j
public class AuthService implements ApplicationListener<AuthenticationSuccessEvent>, UserDetailsService {

	private final UserRepository userRepository;

	@Autowired
	public AuthService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String userName) {
		var userAuth = userRepository.findByUserName(userName);
		
		if (userAuth == null) {
			log.error("User {} not found in the database", userName);
			throw new UsernameNotFoundException("User " + userName + " not found in the database");
		} else {
			log.info("User {} found in the database", userName);
		}

		return Optional.ofNullable(new User(userAuth.getUsername(), userAuth.getPassword(), userAuth.isEnabled(),
				userAuth.isAccountNonExpired(), userAuth.isAccountNonExpired(), userAuth.isAccountNonLocked(),
				userAuth.getAuthorities())).orElseThrow(() -> new UsernameNotFoundException("Not Found"));
	}

	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		userRepository.updateUserAccessed(event.getAuthentication().getName());
	}

}
