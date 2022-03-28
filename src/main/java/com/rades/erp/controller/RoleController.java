package com.rades.erp.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rades.erp.model.Role;
import com.rades.erp.repository.RoleRepository;

@RestController
@RequestMapping("/api/role")
public class RoleController {

	private RoleRepository roleRepository;

	@Autowired
	public RoleController(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	@GetMapping
	public ResponseEntity<?> findAll(Pageable pageable) {
		final List<?> role = roleRepository.findAll();
		return !role.isEmpty() ? new ResponseEntity<>(role, HttpStatus.FOUND)
				: new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/{role_id}")
	public ResponseEntity<?> findById(@PathVariable("role_id") String role_id) {
		return roleRepository.findById(role_id).map(record -> new ResponseEntity<>(record, HttpStatus.FOUND))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@GetMapping("/find/{find}")
	public ResponseEntity<?> findByRoleTitleIgnoreCaseContaining(@PathVariable("find") String find) {
		final List<?> role = roleRepository.findByRoleTitleIgnoreCaseContaining(find);
		return !role.isEmpty() ? new ResponseEntity<>(role, HttpStatus.FOUND)
				: new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping("/findByTitle/{role_title}")
	public ResponseEntity<?> findByTitle(@PathVariable("role_title") String role_title) {
		return Optional.ofNullable(roleRepository.findByRoleTitle(role_title))
				.map(record -> new ResponseEntity<>(record, HttpStatus.FOUND))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@GetMapping("/findByTitleContaining/{user_name}")
	public ResponseEntity<?> findByTitleIgnoreCaseContaining(@PathVariable("role_title") String role_title) {
		final List<?> role = roleRepository.findByRoleTitleIgnoreCaseContaining(role_title);
		return !role.isEmpty() ? new ResponseEntity<>(role, HttpStatus.FOUND)
				: new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> insert(@Valid @RequestBody Role role) {
		return !roleRepository.findById(role.getRoleId()).isPresent()
				? new ResponseEntity<>(roleRepository.save(role), HttpStatus.CREATED)
				: new ResponseEntity<>(HttpStatus.FOUND);
	}

	@PutMapping
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> update(@Valid @RequestBody Role role) {
		return roleRepository.findById(role.getRoleId()).isPresent()
				? new ResponseEntity<>(roleRepository.save(role), HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/{role_id}")
	@Transactional(rollbackFor = Exception.class)
	public ResponseEntity<?> delete(@Valid @PathVariable("role_id") String role_id) {
		if (roleRepository.findById(role_id).isPresent()) {
			roleRepository.deleteById(role_id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}
