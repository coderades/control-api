package com.rades.erp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rades.erp.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

	Boolean existsByUserId(String userId);

	Boolean existsByUserName(String userName);

	Boolean existsByUserEmail(String userEmail);

	User findByUserName(String userName);

	Optional<User> findByUserEmail(String userEmail);

	List<?> findByUserNameIgnoreCaseContaining(String userName);

	List<?> findByUserNameIgnoreCaseContainingOrUserEmailIgnoreCaseContaining(String userName, String userEmail);

	@Query("SELECT u FROM User u WHERE (userName LIKE %?1% OR userEmail LIKE %?1%) AND (userIsEnabled = ?2 OR NULL = ?2) AND (userIsAccountNonExpired = ?3 OR NULL = ?3) AND (userIsAccountNonLocked = ?4 OR NULL = ?4) AND (userIsCredentialsNonDiscredited = ?5 OR NULL = ?5) ORDER BY userName ASC")
	List<?> findByUser(String userName, Boolean userIsEnabled, Boolean userIsAccountNonExpired,
			Boolean userIsAccountNonLocked, Boolean userIsCredentialsNonDiscredited);

	@Modifying
	@Transactional
	@Query("UPDATE User u SET u.userAccessed = CURRENT_TIMESTAMP WHERE u.userName = ?1")
	void updateUserAccessed(String userName);
}
