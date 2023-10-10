package com.erp.hrms.forgetPassword.controller;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.hrms.api.security.response.StatusResponse;
import com.erp.hrms.forgetPassword.dao.IPasswordReset;
import com.erp.hrms.forgetPassword.entity.PasswordReset;
import com.erp.hrms.forgetPassword.request.Otprequest;
import com.erp.hrms.forgetPassword.service.PassworfResetService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class PasswordResetController {

	@Autowired
	private PassworfResetService service;

	@Autowired
	IPasswordReset dao;

	@PostMapping("/send-otp")
	public ResponseEntity<?> getotp(@RequestBody PasswordReset passwordreset) {

		Random random = new Random();
		int otpNumber = random.nextInt(900000) + 100000;

		PasswordReset reset = new PasswordReset();
		reset.setEmail(passwordreset.getEmail());
		reset.setOtp(otpNumber);
		reset.setExpirationDateTime(LocalDateTime.now().plusMinutes(10));
		reset.setUsed(false);

		service.generateOTP(reset);
		return ResponseEntity.ok(new StatusResponse(true));
	}

	@PostMapping("/verifyotp")
	public ResponseEntity<?> verifyotp(@RequestBody Otprequest otp) {
		String email = otp.getEmail();
		Integer otp2 = otp.getOtp();
		boolean verifyOTP = service.verifyOTP(email, otp2);
		if (verifyOTP) {
			PasswordReset findByEmailAndOtp = dao.findByEmailAndOtp(email, otp2);
			findByEmailAndOtp.setUsed(true);
			dao.save(findByEmailAndOtp);
			return ResponseEntity.ok(new StatusResponse(true));
		}
		return ResponseEntity.ok(new StatusResponse(false));
	}

}
