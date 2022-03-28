package com.rades.erp.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rades.erp.model.User;
import com.rades.erp.repository.UserRepository;
import com.rades.erp.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private final UserRepository userRepository;
	private final UserService userService;

	@Autowired
	public UserController(UserRepository userRepository, UserService userService) {
		this.userRepository = userRepository;
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity<?> findAll(Pageable pageable) {
		final var user = userService.findAll(pageable);
		return user.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
				: ResponseEntity.status(HttpStatus.FOUND).body(user);
	}

	@GetMapping("/{user_id}")
	public ResponseEntity<?> findById(@PathVariable("user_id") String user_id) {
		var user = userService.findById(user_id);
		return user == null ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
				: ResponseEntity.status(HttpStatus.FOUND).body(user);
	}

	@GetMapping("/findByName/{user_name}")
	public ResponseEntity<?> findByName(@PathVariable("user_name") String user_name) {
		final var user = userService.findByName(user_name);
		return user == null ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
				: ResponseEntity.status(HttpStatus.FOUND).body(user);
	}

	@GetMapping("/findByEmail/{user_email}")
	public ResponseEntity<?> findByEmail(@PathVariable("user_email") String user_email) {
		final var user = userService.findByEmail(user_email);
		return user == null ? ResponseEntity.status(HttpStatus.NOT_FOUND).build()
				: ResponseEntity.status(HttpStatus.FOUND).body(user);
	}

	@GetMapping("/find/{find}")
	public ResponseEntity<?> findByUserNameIgnoreCaseContainingOrUserEmailIgnoreCaseContaining(
			@PathVariable("find") String find) {
		final var user = userRepository.findByUserNameIgnoreCaseContainingOrUserEmailIgnoreCaseContaining(find, find);
		return user.size() > 0 ? ResponseEntity.status(HttpStatus.FOUND).body(user)
				: ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@GetMapping("/findByNameContaining/{user_name}")
	public ResponseEntity<?> findByNameIgnoreCaseContaining(@PathVariable("user_name") String user_name) {
		final var user = userRepository.findByUserNameIgnoreCaseContaining(user_name);
		return user.size() > 0 ? ResponseEntity.status(HttpStatus.FOUND).body(user)
				: ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}

	@PostMapping
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> insert(@Valid @RequestBody User user) {
		final var save = userRepository.save(user);	
		return save == null ? ResponseEntity.status(HttpStatus.CONFLICT).build()
				: ResponseEntity.status(HttpStatus.CREATED).body(user);
	}

	@PutMapping("/{user_id}")
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> update(@Valid @PathVariable("user_id") String user_id, @Valid @RequestBody User user) {
		return userRepository.findById(user_id).map(record -> {
			record.setUserIsEnabled(user.getUserIsEnabled());
			record.setUserIsAccountNonExpired(user.getUserIsAccountNonExpired());
			record.setUserIsAccountNonLocked(user.getUserIsAccountNonLocked());
			record.setUserIsCredentialsNonDiscredited(user.getUserIsCredentialsNonDiscredited());
			record.setUserName(user.getUserName());
			record.setUserEmail(user.getUserEmail());
			User updated = userRepository.save(record);
			return ResponseEntity.status(HttpStatus.OK).body(updated);
		}).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@PutMapping("/updatePassword")
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> savePassword(@Valid @RequestBody User user) {
		return userRepository.findById(user.getUserId()).map(record -> {
			record.setUserPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
			userRepository.save(record);
			return ResponseEntity.ok().build();
		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{user_id}")
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> delete(@Valid @PathVariable("user_id") String user_id) {
		return userService.delete(user_id) ? ResponseEntity.status(HttpStatus.NO_CONTENT).build()
				: ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
}
