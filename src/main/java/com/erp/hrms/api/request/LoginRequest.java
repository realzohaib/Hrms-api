package com.erp.hrms.api.request;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank
	private String Username;

	@NotBlank
	private String password;

	private String role;
}
