package com.erp.hrms.api.security.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.erp.hrms.entity.PersonalInfo;
import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

/**
 * @author TA Admin
 *
 * 
 */

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "username"),
		@UniqueConstraint(columnNames = "email") })
@Setter
@Getter
public class UserEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	//@NotBlank
	@Size(max = 20)
	private String username;

	//@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	//@NotBlank
	@Size(max = 120)
	private String password;
	
	private String activationToken;
	
	private boolean isEnabled;
	
	@OneToOne
	@JoinColumn(name = "PId")
	@JsonBackReference
	private PersonalInfo personalinfo;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<RoleEntity> roles = new HashSet<>();

	public UserEntity() {
	}

	public UserEntity(String username, String email, String password ,boolean isEnabled ) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.isEnabled = isEnabled;
	}

}
