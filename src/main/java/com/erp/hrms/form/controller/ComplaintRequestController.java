package com.erp.hrms.form.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.entity.form.ComplaintForm;
import com.erp.hrms.form.service.IComplaintService;

@RestController
@RequestMapping("/api/v1")
public class ComplaintRequestController {

	@Autowired
	IComplaintService iComplaintService;

//	This methos for send the complaint to the manager 
	@PostMapping("/complaintrequest")
	public ResponseEntity<?> sendComplaintRequest(@RequestParam("complaintRequest") String complaintRequest)
			throws IOException {
		iComplaintService.sendComplaintRequest(complaintRequest);
		return ResponseEntity.ok(new MessageResponse("Your complaint request is send to manager."));
	}

//	This method for get the complaint request by the complaint Id
	@GetMapping("/complaintrequest/{complaintId}")
	public ResponseEntity<?> getComplaintRequestById(@PathVariable long complaintId) {
		return ResponseEntity.ok(iComplaintService.getComplaintRequestById(complaintId));
	}

//	This method for get the find all complaints
	@GetMapping("/findall/complaints")
	public ResponseEntity<?> findAllComplaints() {
		return ResponseEntity.ok(iComplaintService.findAllComplaints());
	}

//	This method for approved or denied or pending the employee's complaint
	@PutMapping("/complaint/approvedByManager/{complaintId}")
	public ResponseEntity<?> complaintApprovedOrDeniedOrPending(@PathVariable long complaintId,
			@RequestParam("complaintRequest") String complaintRequest) throws IOException {
		iComplaintService.complaintApprovedOrDeniedOrPending(complaintId, complaintRequest);
		return ResponseEntity.ok(new MessageResponse("Your complaint is approved or denied by the manager"));
	}

//	This method for find all the complaint Request by employeeId
	@GetMapping("/findall/complaint/request/with/employeeId/{employeeId}")
	public ResponseEntity<List<ComplaintForm>> getComplaintRequestByEmployeeId(@PathVariable long employeeId) {
		return ResponseEntity.ok(iComplaintService.getComplaintRequestByEmployeeId(employeeId));
	}

}
