package com.erp.hrms.form.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erp.hrms.api.security.response.MessageResponse;
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
		try {
			iComplaintService.sendComplaintRequest(complaintRequest);
			return new ResponseEntity<>(new MessageResponse("Your complaint request is send to manager."),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("Error while creating complant. " + e),
					HttpStatus.BAD_REQUEST);
		}
	}

//	This method for get the complaint request by the complaint Id
	@GetMapping("/complaint/request/{complaintId}")
	public ResponseEntity<?> getComplaintRequestById(@PathVariable long complaintId) {
		try {
			return new ResponseEntity<>(iComplaintService.getComplaintRequestById(complaintId), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("Complaint request with ID " + complaintId + " not found"),
					HttpStatus.NOT_FOUND);
		}
	}

//	This method for get the find all complaints
	@GetMapping("/findall/complaints")
	public ResponseEntity<?> findAllComplaints() {
		try {
			return new ResponseEntity<>(iComplaintService.findAllComplaints(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("No Complaints "), HttpStatus.NOT_FOUND);
		}
	}

//	This method for approved or denied or pending the employee's complaint
	@PutMapping("/complaint/approvedByManager/{complaintId}")
	public ResponseEntity<?> complaintApprovedOrDeniedOrPending(@PathVariable long complaintId,
			@RequestParam("complaintRequest") String complaintRequest) throws IOException {
		try {
			iComplaintService.complaintApprovedOrDeniedOrPending(complaintId, complaintRequest);
			return new ResponseEntity<>(new MessageResponse("Your complaint is approved or denied by the manager"),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("Error while approving leave request. " + e),
					HttpStatus.BAD_REQUEST);
		}
	}

//	This method for find all the complaint Request by employeeId
	@GetMapping("/findall/complaint/request/with/employeeId/{employeeId}")
	public ResponseEntity<?> getComplaintRequestByEmployeeId(@PathVariable long employeeId) {
		try {
			return new ResponseEntity<>(iComplaintService.getComplaintRequestByEmployeeId(employeeId), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("This employee ID " + employeeId + " not found." + e),
					HttpStatus.NOT_FOUND);
		}
	}

}
