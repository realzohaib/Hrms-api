package com.erp.hrms.form.controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.form.service.ILeaveTypeService;

@RestController
@RequestMapping("/api/v1")
public class LeaveTypeController {

	@Autowired
	public ILeaveTypeService iLeaveTypeService;

	@PostConstruct
	public ResponseEntity<?> PredefinedLeaveType() {
		try {
			iLeaveTypeService.predefinedLeaveType();
			return new ResponseEntity<>(new MessageResponse("Your predefined leave type save to database. "),
					HttpStatus.OK);
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<>(new MessageResponse("Duplicate Leave type found"), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/leave/type")
	public ResponseEntity<?> createLeaveType(@RequestParam("leaveType") String leaveType) {
		try {
			iLeaveTypeService.createLeaveType(leaveType);
			return new ResponseEntity<>(new MessageResponse("your leave type is created "), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("Error while creating leave type. " + e),
					HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/find/all/leave/type")
	public ResponseEntity<?> findAllLeaveType() {
		try {
			return new ResponseEntity<>(iLeaveTypeService.findAllLeaveType(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("No Leave type "), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/find/byLeaveId/{leaveTypeId}")
	public ResponseEntity<?> findByleaveTypeId(@PathVariable Long leaveTypeId) {
		try {
			return new ResponseEntity<>(iLeaveTypeService.findByLeaveTypeId(leaveTypeId), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("Leave Request with ID: " + leaveTypeId + " not found "),
					HttpStatus.NOT_FOUND);
		}
	}

}
