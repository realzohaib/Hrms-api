package com.erp.hrms.api.security.entity;

import lombok.Data;
// this class is being  used when user set his password for the first time
@Data
public class RegistrationRequest {
	
	private String username;
	private String otp;

}
