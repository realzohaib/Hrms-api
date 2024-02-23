package com.erp.hrms.joblevelanddesignation.controller;

import java.util.ArrayList;
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
import com.erp.hrms.joblevelanddesignation.entity.Designations;
import com.erp.hrms.joblevelanddesignation.entity.Duties;
import com.erp.hrms.joblevelanddesignation.entity.SubDuties;
import com.erp.hrms.joblevelanddesignation.request_responseentity.DutiesRequest;
import com.erp.hrms.joblevelanddesignation.request_responseentity.DutiesResponse;
import com.erp.hrms.joblevelanddesignation.service.DutiesIServicempl;

@RestController
@RequestMapping("/api/v1")
public class DutyController {

	@Autowired
	private DutiesIServicempl service;

	@PostMapping("/duties")
	public ResponseEntity<?> saveDuties(@RequestBody DutiesRequest req) {
		try {
			service.saveDuties(req);
			return ResponseEntity.ok().body(new MessageResponse("data saved"));
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/duties")
	public ResponseEntity<?> getAllDuties() {
		try {
			List<DutiesResponse> response = new ArrayList<DutiesResponse>();
			List<Duties> getallduties = service.getallduties();

			for (Duties duties : getallduties) {
				DutiesResponse dutiesResponse = new DutiesResponse();

				Integer id = duties.getDutiesId();
				String dutyName = duties.getDutyName();
				List<SubDuties> subduties = duties.getSubduties();

				dutiesResponse.setDutiesId(id);
				dutiesResponse.setDutyName(dutyName);
				dutiesResponse.setSubduties(subduties);

				List<Designations> designationlist = duties.getDesignation();
				for (Designations designation : designationlist) {
					Integer designationId = designation.getDesignationId();
					String designationName = designation.getDesignationName();
					dutiesResponse.setDesignationName(designationName);
					dutiesResponse.setDesignationId(designationId);

				}
				response.add(dutiesResponse);

			}

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}

	}
	
	@GetMapping("/designation/{id}/duties")
	public ResponseEntity<?> getDutiesByDesignationId(@PathVariable int id) {
		try {
			List<DutiesResponse> list = service.loadDutiesByDesignationId(id);
			if (list.isEmpty()) {
				return ResponseEntity.ok("No Records.");
			}
			return ResponseEntity.ok().body(list);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	

}
