package com.rades.erp.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;

@Entity
@Table(name = "[permission]")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "permissionId")
public class Permission {

	@Id
	@Column(name = "permission_id", columnDefinition = "uuid", nullable = false, unique = true)
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "org.hibernate.id.UUIDGenerator")
	private String permissionId;

	@Column(name = "resource_id", columnDefinition = "uuid", nullable = false)
	private String resourceId;

	@Column(name = "permission_mod", length = 1, nullable = false)
	@Size(min = 1, max = 1)
	@NotBlank(message = "It cannot be empty")
	private String permissionMod;

	@Column(name = "permission_mode_public", nullable = false)
	private Integer permissionModePublic;

	@Column(name = "permission_created", nullable = false, insertable = true, updatable = false)
	@DateTimeFormat
	@CreationTimestamp
	private LocalDateTime permissionCreated;

	@Column(name = "permission_updated", nullable = true, insertable = false, updatable = true)
	@DateTimeFormat
	@UpdateTimestamp
	private LocalDateTime permissionUpdated;

}
