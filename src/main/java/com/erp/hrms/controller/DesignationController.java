package com.erp.hrms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.api.service.IDesignationService;
import com.erp.hrms.entity.Designation;

@RestController
@RequestMapping("/api/v1")
public class DesignationController {

	@Autowired
	private IDesignationService iDesignationService;

	@PostMapping("/designation")
	public ResponseEntity<?> saveDesignation(@RequestBody Designation designation) {

		try {
			iDesignationService.saveDesignation(designation);
			return new ResponseEntity<>(new MessageResponse("Your designation is saved."), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("An error occurred" + e.getMessage()),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/designation/{did}")
	public ResponseEntity<?> findByLevelId(@PathVariable Long did) {
		try {
			Designation findByDesignationId = iDesignationService.findByDesignationId(did);
			if (findByDesignationId == null) {
				return new ResponseEntity<>(new MessageResponse("No records found"), HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<>(findByDesignationId, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("An error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/all/designation")
	public ResponseEntity<?> findAllDepartments() {
		try {
			List<Designation> findAllDesignation = iDesignationService.findAllDesignation();
			if (findAllDesignation.isEmpty()) {
				return new ResponseEntity<>(new MessageResponse("No records found"), HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<>(findAllDesignation, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("An error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
