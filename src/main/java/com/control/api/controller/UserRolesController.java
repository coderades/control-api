package com.control.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.control.api.repository.UserRolesRepository;

@RestController
@RequestMapping("/api/userRoles")
public class UserRolesController {

	private UserRolesRepository userRolesRepository;

	@Autowired
	public UserRolesController(UserRolesRepository userRoleRepository) {
		this.userRolesRepository = userRoleRepository;
	}

	@GetMapping
	public ResponseEntity<?> findAll(Pageable pageable) {
		final List<?> user = userRolesRepository.findAll();
		return !user.isEmpty() ? new ResponseEntity<>(user, HttpStatus.FOUND)
				: new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/{userRoles_id}")
	public ResponseEntity<?> findById(@PathVariable("userRoles_id") String userRoles_id) {
		return userRolesRepository.findById(userRoles_id).map(record -> new ResponseEntity<>(record, HttpStatus.FOUND))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

}
