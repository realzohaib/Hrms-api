package com.erp.hrms.api.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.hrms.api.repo.UserRepository;
import com.erp.hrms.api.request.SignupRequest;
import com.erp.hrms.api.security.entity.RegistrationRequest;
import com.erp.hrms.api.security.entity.UserEntity;
import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.api.security.response.StatusResponse;

@RestController
@RequestMapping("/api/auth")
public class AccountActivationController {

	@Autowired
	private UserRepository repo;

	@Autowired
	PasswordEncoder encoder;

	@PostMapping("/activate")
	public ResponseEntity<?> activateAccount(@RequestBody RegistrationRequest req) {
		
		try {
			UserEntity user = repo.findByOtpAndUsername(req.getOtp() ,req.getUsername() );
			if (user != null) {
				user.setOtp(null);
				user.setEnabled(true);
				repo.save(user);
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(new StatusResponse(true));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new MessageResponse("Invalid otp or Username. Account activation failed."));
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(e.getMessage()));
		}
	}

	@PostMapping("/save-password")
	public ResponseEntity<?> savePassword(@RequestBody SignupRequest request) {
		try {
			String username = request.getUsername();
			UserEntity user = repo.getByUsername(username);

			if (user != null) {
				user.setPassword(encoder.encode(request.getPassword()));
				repo.save(user);
				return ResponseEntity.ok(new StatusResponse(true));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body(new MessageResponse("User not found. Password update failed."));
			}
		} catch (NumberFormatException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new MessageResponse("Invalid user identifier. Password update failed."));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse(e.getMessage()));
		}
	}

}
