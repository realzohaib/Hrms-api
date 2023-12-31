package com.erp.hrms.api.security.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.hrms.api.dao.IPersonalInfoDAO;
import com.erp.hrms.api.repo.IRoleRepository;
import com.erp.hrms.api.repo.UserRepository;
import com.erp.hrms.api.request.LoginRequest;
import com.erp.hrms.api.request.SignupRequest;
import com.erp.hrms.api.security.entity.RoleEntity;
import com.erp.hrms.api.security.entity.UserEntity;
import com.erp.hrms.api.security.response.JwtResponse;
import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.api.security.response.StatusResponse;
import com.erp.hrms.api.security.utll.JwtTokenUtill;
import com.erp.hrms.api.service.impl.UserDetailsImpl;
import com.erp.hrms.api.utill.ERole;
import com.erp.hrms.entity.PersonalInfo;

/**
 * @author TA Admin
 *
 * 
 */

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	IRoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtTokenUtill jwtTokenUtill;

	@Autowired
	IPersonalInfoDAO dao;

	@PostMapping("/v1/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			JwtResponse jwt = jwtTokenUtill.generateJwtToken(authentication);

			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			Set<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
					.collect(Collectors.toSet());

			// Verify if the user has the required role
			if (!roles.contains(loginRequest.getRole())) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MessageResponse("You don't have access"));
			}

			String username = loginRequest.getUsername();
			long parseLong = Long.parseLong(username);

			if (roles.contains("ROLE_EMPLOYEE")) {
				PersonalInfo personalInfo = dao.getPersonalInfoByEmployeeId(parseLong);
				jwt.setInfo(personalInfo);
				return ResponseEntity.ok(jwt);
			}

			return ResponseEntity.ok(jwt);

		} catch (BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new MessageResponse("Invalid username or password"));
		}
	}

	@PostMapping("/v1/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		String username = signUpRequest.getUsername();

		long parseLong = Long.parseLong(username);
		System.out.println(parseLong);
		System.out.println(dao.existByID(parseLong));

		if (dao.existByID(parseLong)) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid Employee Id"));

		}

		// Create new user's account
		UserEntity user = new UserEntity(signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()), false);

		Set<String> strRoles = signUpRequest.getRole();
		Set<RoleEntity> roles = new HashSet<>();

		System.out.println(strRoles);

		if (strRoles == null) {
			RoleEntity userRole = roleRepository.findByName(ERole.ROLE_EMPLOYEE)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					RoleEntity adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);
					break;
				case "employee":
					RoleEntity empRole = roleRepository.findByName(ERole.ROLE_EMPLOYEE)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(empRole);
					break;
				case "manager":
					RoleEntity modRole = roleRepository.findByName(ERole.ROLE_MANAGER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);
					break;
				case "hr":
					RoleEntity hrRole = roleRepository.findByName(ERole.ROLE_HR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(hrRole);
					break;
				case "subadmin":
					RoleEntity subAdminRole = roleRepository.findByName(ERole.ROLE_SUBADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
					roles.add(subAdminRole);
				}
			});
		}

		user.setRoles(roles);
		UserEntity save = userRepository.save(user);
		if (save != null) {
			return ResponseEntity.ok(new StatusResponse(true));
		}
		return ResponseEntity.ok(new StatusResponse(false));

	}

	@PostConstruct
	public ResponseEntity<String> initializeDefaultRoles() {
		if (roleRepository.findAll().isEmpty()) {
			try {
				List<RoleEntity> roleList = Arrays.asList(new RoleEntity(ERole.ROLE_ADMIN),
						new RoleEntity(ERole.ROLE_EMPLOYEE), new RoleEntity(ERole.ROLE_MANAGER),
						new RoleEntity(ERole.ROLE_HR), new RoleEntity(ERole.ROLE_SUBADMIN));

				List<RoleEntity> savedRoles = roleRepository.saveAll(roleList);

				if (!savedRoles.isEmpty()) {
					return ResponseEntity.ok("Default roles have been initialized successfully.");
				} else {
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
							.body("Failed to initialize default roles.");
				}
			} catch (DataAccessException e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("An error occurred while initializing default roles: " + e.getMessage());
			}
		} else {
			return ResponseEntity.ok("Default roles already exist. No further action required.");
		}
	}

}
