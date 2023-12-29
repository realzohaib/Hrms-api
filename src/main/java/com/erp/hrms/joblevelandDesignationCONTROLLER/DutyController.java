package com.erp.hrms.joblevelandDesignationCONTROLLER;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.joblevelandDesignationEntity.Duties;
import com.erp.hrms.joblevelandDesignationREQ_RES.DutiesRequest;
import com.erp.hrms.joblevelandDesignationREQ_RES.DutiesResponse;
import com.erp.hrms.joblevelandDesignationSERVICE.DutiesIServicempl;

@RestController
public class DutyController {
	
	@Autowired
	private DutiesIServicempl service;
	
	@PostMapping("/saveduties")
	public ResponseEntity<?> saveDuties(@RequestBody DutiesRequest req) {
		try {
			return ResponseEntity.ok().body(service.saveDuties(req));
		} catch (Exception e) {	
			return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/loadAllDuties")
	public ResponseEntity<?> getAllDuties(){
		try {
			List<DutiesResponse> response = new ArrayList<DutiesResponse>();
			List<Duties> getallduties = service.getallduties();
			System.out.println(getallduties);
			for (Duties duties : getallduties) {
				Duties d1 = new Duties();
				DutiesResponse dutiesResponse = new DutiesResponse();
				
				d1.setDutiesId(duties.getDutiesId());
				d1.setDutyName(duties.getDutyName());
				d1.setDesignations(duties.getDesignations());
				
				dutiesResponse.setDuties(d1);
				response.add(dutiesResponse);
			}
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {	
			return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

}
