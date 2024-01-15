package com.erp.hrms.joblevelandDesignationCONTROLLER;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.joblevelandDesignationEntity.Designations;
import com.erp.hrms.joblevelandDesignationEntity.Duties;
import com.erp.hrms.joblevelandDesignationEntity.SubDuties;
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
			service.saveDuties(req);
			return ResponseEntity.ok().body(new MessageResponse("data saved"));
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/loadAllDuties")
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
	
	@GetMapping("/getDutiesByDesignationId/{id}")
	public ResponseEntity<?> getDutiesByDesignationId(@PathVariable int id) {
		try {
			List<DutiesResponse> list = service.loadDutiesByDesignationId(id);
			return ResponseEntity.ok().body(list);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	

}
