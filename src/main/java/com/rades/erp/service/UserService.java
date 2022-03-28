package com.rades.erp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.rades.erp.model.User;
import com.rades.erp.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public Page<?> findAll(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

	public User findById(String userId) {
		final var user = userRepository.findById(userId);
		return user.isPresent() ? user.get() : null;
	}

	public User findByName(String userName) {
		final var user = userRepository.findByUserName(userName);
		return user != null ? user : null;
	}

	public List<?> findByNameContaining(String userName) {
		final var user = userRepository.findByUserNameIgnoreCaseContaining(userName);
		return user.size() > 0 ? user : null;
	}

	public User findByEmail(String userEmail) {
		final var user = userRepository.findByUserEmail(userEmail);
		return user.isPresent() ? user.get() : null;
	}

	public List<?> find(String find) {
		final var user = userRepository.findByUserNameIgnoreCaseContainingOrUserEmailIgnoreCaseContaining(find, find);
		return user.size() > 0 ? user : null;
	}

	public User save(User user) {
		final var userSave = new User();

		userSave.setUserIsEnabled(user.getUserIsEnabled());
		userSave.setUserIsAccountNonExpired(user.getUserIsAccountNonExpired());
		userSave.setUserIsAccountNonLocked(user.getUserIsAccountNonLocked());
		userSave.setUserIsCredentialsNonDiscredited(user.getUserIsCredentialsNonDiscredited());
		userSave.setUserName(user.getUserName());
		userSave.setUserEmail(user.getUserEmail());
		
		userSave.setUserPassword(new BCryptPasswordEncoder().encode(user.getUserPassword()));

		final var save = userRepository.save(userSave);
		return save;
	}

	public boolean savePassword(String userId, String userPassword) {
		final var userSave = new User();

		if (userRepository.existsByUserId(userId) && !userId.trim().isEmpty() && !userPassword.trim().isEmpty()) {
			userSave.setUserId(userId);
			userSave.setUserPassword(new BCryptPasswordEncoder().encode(userPassword));
			userRepository.save(userSave);
			return true;
		} else {
			return false;
		}
	}

	public Boolean delete(String userId) {
		if (userRepository.existsByUserId(userId)) {
			userRepository.deleteById(userId);
			return true;
		} else {
			return false;
		}
	}

}
