package com.erp.hrms.joblevelanddesignation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.joblevelanddesignation.dao.DesignationRepo;
import com.erp.hrms.joblevelanddesignation.request_responseentity.DesignationRequest;
import com.erp.hrms.joblevelanddesignation.request_responseentity.DesignationResponse;
import com.erp.hrms.joblevelanddesignation.service.DesignationServiceImpl;

@RestController
public class DesignationController {

	@Autowired
	private DesignationServiceImpl service;
	
	@Autowired
	private DesignationRepo repo;

	@PostMapping("/savedesignation")
	public ResponseEntity<?> saveDesignation(@RequestBody DesignationRequest designation) {
		try {
			return ResponseEntity.ok().body(service.savedesigDesignations(designation));
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/getAllDesignations")
	public ResponseEntity<?> getAllDesignations() {
		try {
			return ResponseEntity.ok().body(service.loadAllDesignations());
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("Failed to retrieve designations: " + e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/getDesignations/{id}")
	public ResponseEntity<?> loadDesignationById(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok().body(service.loadDesignationById(id));
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("Failed to retrieve designations: " + e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/getDesignationsByLevelId/{id}")
	public ResponseEntity<?> loadDesignationByLevelId(@PathVariable Integer id) {
		try {
			List<DesignationResponse> list = service.loadAllDesignationBYlevelId(id);

			if (list.isEmpty()) {
				ResponseEntity<Object> build = ResponseEntity.noContent().build();
				return ResponseEntity.ok("list is empty");
			}
			return ResponseEntity.ok().body(list);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("Failed to retrieve designations: " + e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}

	@PutMapping("/updateDesignation")
	public ResponseEntity<?> updateDesignation(@RequestBody DesignationResponse designation) {
		try {
			return ResponseEntity.ok().body(service.updateDesignation(designation));
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("Failed to update designations: " + e.getMessage()),
					HttpStatus.BAD_REQUEST);
		}
	}

}
