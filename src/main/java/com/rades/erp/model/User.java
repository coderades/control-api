package com.rades.erp.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@Entity
@Table(name = "[user]")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "userId")
public class User implements UserDetails {

	private static final long serialVersionUID = -1678201250382609358L;

	@Id
	@Column(name = "user_id", columnDefinition = "uuid", nullable = false, unique = true)
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "org.hibernate.id.UUIDGenerator")
	private String userId;

	@Column(name = "user_is_enabled", nullable = false)
	private Boolean userIsEnabled;

	@Column(name = "user_is_account_non_expired", nullable = false)
	private Boolean userIsAccountNonExpired;

	@Column(name = "user_is_account_non_locked", nullable = false)
	private Boolean userIsAccountNonLocked;

	@Column(name = "user_is_credentials_non_expired", nullable = false)
	private Boolean userIsCredentialsNonDiscredited;

	@Column(name = "user_name", length = 50, nullable = false, unique = true)
	@Size(min = 2, max = 50)
	@NotBlank(message = "It cannot be empty")
	private String userName;

	@Column(name = "user_email", length = 50, nullable = true, unique = true)
	@Size(min = 8, max = 50)
	@Email(message = "Incorrect format")
	private String userEmail;

	@Column(name = "user_password", length = 70, nullable = false)
	@Size(min = 1, max = 70)
	private String userPassword;

	@Column(name = "user_password_token", columnDefinition = "uuid", nullable = true)
	private String userPasswordToken;

	@Column(name = "user_remember_token", columnDefinition = "uuid", nullable = true)
	private String userRememberToken;

	@Column(name = "user_pin_code", nullable = true)
	private Long userPinCode;

	@Column(name = "user_created", nullable = false, insertable = true, updatable = false)
	@DateTimeFormat
	@CreationTimestamp
	private LocalDateTime userCreated;

	@Column(name = "user_updated", nullable = true, insertable = false, updatable = true)
	@DateTimeFormat
	@UpdateTimestamp
	private LocalDateTime userUpdated;

	@Column(name = "user_accessed", nullable = true, insertable = false, updatable = false)
	@DateTimeFormat
	private LocalDateTime userAccessed;

	@ManyToMany()
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private List<Role> roles;

	public String getUserName() {
		return userName;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		final Collection<GrantedAuthority> grantedAuthority = new ArrayList<>();
		roles.forEach(role -> grantedAuthority.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleTitle())));	
		return grantedAuthority;
	}

	@Override
	public String getPassword() {
		return this.userPassword;
	}

	@Override
	public String getUsername() {
		return this.userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.userIsAccountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.userIsAccountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.userIsCredentialsNonDiscredited;
	}

	@Override
	public boolean isEnabled() {
		return this.userIsEnabled;
	}
}
