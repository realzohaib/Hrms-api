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
import com.erp.hrms.api.service.ICurrentDesignationService;
import com.erp.hrms.entity.CurrentDesignation;

@RestController
@RequestMapping("/api/v1")
public class CurrentDesignationController {

	@Autowired
	private ICurrentDesignationService iCurrentDesignationService;

	@PostMapping("/current-designation")
	ResponseEntity<?> saveCurrentDesignation(@RequestBody CurrentDesignation currentDesignation) {
		try {
			iCurrentDesignationService.saveCurrentDesignation(currentDesignation);
			return new ResponseEntity<>(new MessageResponse("Your current designation is saved."), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("An error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/current-designation/{cdid}")
	public ResponseEntity<?> findByLevelId(@PathVariable Long cdid) {
		try {
			CurrentDesignation findByCurrentDesignationId = iCurrentDesignationService.findByCurrentDesignationId(cdid);
			if (findByCurrentDesignationId == null) {
				return new ResponseEntity<>(new MessageResponse("No records found"), HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<>(findByCurrentDesignationId, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("An error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/all/current-designation")
	public ResponseEntity<?> findAllDepartments() {
		try {
			List<CurrentDesignation> findAllCurrentDesignation = iCurrentDesignationService.findAllCurrentDesignation();
			if (findAllCurrentDesignation.isEmpty()) {
				return new ResponseEntity<>(new MessageResponse("No records found"), HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<>(findAllCurrentDesignation, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("An error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
