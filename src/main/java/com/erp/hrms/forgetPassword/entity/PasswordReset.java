package com.erp.hrms.forgetPassword.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "password_reset")
public class PasswordReset {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long resetid;

	private String email;

	private Integer otp;

	private LocalDateTime expirationDateTime;

	private boolean used;
}
