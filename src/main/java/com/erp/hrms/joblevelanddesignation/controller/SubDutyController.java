package com.erp.hrms.joblevelanddesignation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.joblevelanddesignation.request_responseentity.SubDutiesRequest;
import com.erp.hrms.joblevelanddesignation.service.ISubdutyServiceImpl;


@RestController
public class SubDutyController {
	
	@Autowired
	private ISubdutyServiceImpl service;
	
	@PostMapping("/save_subduties")
	public ResponseEntity<?> saveSubDuties(@RequestBody SubDutiesRequest req) {
		try {
			service.saveSubduty(req);
			return ResponseEntity.ok().body(new MessageResponse("data saved"));
		} catch (Exception e) {	
			return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	

	@GetMapping("/getAllSubDuties")
	public ResponseEntity<?> loadAllSubDuties() {
		try {
			return ResponseEntity.ok().body(service.loadAllSubduties());

		} catch (Exception e) {
			return ResponseEntity.ok().body(e.getMessage());
		}
	}

}