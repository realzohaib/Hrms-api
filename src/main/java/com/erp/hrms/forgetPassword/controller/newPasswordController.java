package com.erp.hrms.forgetPassword.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.hrms.api.security.entity.UserEntity;
import com.erp.hrms.api.security.response.StatusResponse;
import com.erp.hrms.forgetPassword.dao.userRepo;
import com.erp.hrms.forgetPassword.request.NewPassword;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class newPasswordController {

	@Autowired
	userRepo repo;

	@Autowired
	PasswordEncoder encoder;

	@PutMapping("/new-password")
	public ResponseEntity<?> saveNewPassword(@RequestBody NewPassword newpassword) {

		String email = newpassword.getEmail();
		String pwd = newpassword.getPassword();
		UserEntity user = repo.findByEmail(email);

		try {
			user.setPassword(encoder.encode(pwd));
			repo.save(user);
			return ResponseEntity.ok(new StatusResponse(true));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new StatusResponse(false));
		}
	}

}
