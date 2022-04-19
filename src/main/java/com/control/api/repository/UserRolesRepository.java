package com.control.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.control.api.model.UserRoles;

public interface UserRolesRepository  extends JpaRepository<UserRoles, String> {

}
