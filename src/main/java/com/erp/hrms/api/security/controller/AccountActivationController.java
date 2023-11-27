package com.erp.hrms.api.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erp.hrms.api.repo.UserRepository;
import com.erp.hrms.api.request.SignupRequest;
import com.erp.hrms.api.security.entity.UserEntity;
import com.erp.hrms.api.security.response.MessageResponse;

@RestController
public class AccountActivationController {

	@Autowired
	private UserRepository repo;

	@Autowired
	PasswordEncoder encoder;

	@GetMapping("/activate")
	public ResponseEntity<?> activateAccount(@RequestParam("token") String activationToken) {
		try {
			UserEntity user = repo.findByActivationToken(activationToken);
			if (user != null) {
				user.setActivationToken(null);
				user.setEnabled(true);
				repo.save(user);
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(new MessageResponse("Account Activated"));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new MessageResponse("Invalid activation token. Account activation failed."));
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
				return ResponseEntity.ok(new MessageResponse("Password updated successfully"));
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
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
