package com.rades.erp.service;

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

import com.rades.erp.repository.UserRepository;

@Service
@Transactional
public class AuthService implements ApplicationListener<AuthenticationSuccessEvent>, UserDetailsService {

	private final UserRepository userRepository;

	@Autowired
	public AuthService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String userName) {
		var userAuth = userRepository.findByUserName(userName);
		return Optional.ofNullable(new User(userAuth.getUsername(), userAuth.getPassword(), userAuth.isEnabled(),
				userAuth.isAccountNonExpired(), userAuth.isAccountNonExpired(), userAuth.isAccountNonLocked(),
				userAuth.getAuthorities())).orElseThrow(() -> new UsernameNotFoundException("Not Found"));
	}

	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		userRepository.updateUserAccessed(event.getAuthentication().getName());
	}

}
