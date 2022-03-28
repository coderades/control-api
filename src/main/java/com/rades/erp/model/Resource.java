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
@Table(name = "[resource]")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "resourceId")
public class Resource {

	@Id
	@Column(name = "resource_id", columnDefinition = "uuid", nullable = false, unique = true)
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "org.hibernate.id.UUIDGenerator")
	private String resourceId;

	@Column(name = "application_id", columnDefinition = "uuid", nullable = false)
	private String applicationId;

	@Column(name = "resource_is_enabled", nullable = false)
	private Boolean resourceIsEnabled;

	@Column(name = "resource_is_public", nullable = false)
	private Boolean resourceIsPublic;

	@Column(name = "resource_title", length = 50, nullable = false, unique = true)
	@Size(min = 2, max = 50)
	@NotBlank(message = "It cannot be empty")
	private String resourceTitle;

	@Column(name = "resource_url", length = 200, nullable = false, unique = true)
	@Size(min = 2, max = 200)
	@NotBlank(message = "It cannot be empty")
	private String resourceUrl;

	@Column(name = "resource_created", nullable = false, insertable = true, updatable = false)
	@DateTimeFormat
	@CreationTimestamp
	private LocalDateTime resourceCreated;

	@Column(name = "resource_updated", nullable = true, insertable = false, updatable = true)
	@DateTimeFormat
	@UpdateTimestamp
	private LocalDateTime resourceUpdated;

}
