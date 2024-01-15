package com.erp.hrms.joblevelandDesignationCONTROLLER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.joblevelandDesignationREQ_RES.SubDutiesRequest;
import com.erp.hrms.joblevelandDesignationSERVICE.ISubdutyServiceImpl;


@RestController
public class SubDutyController {
	
	@Autowired
	private ISubdutyServiceImpl service;
	
	@PostMapping("/save _subduties")
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
