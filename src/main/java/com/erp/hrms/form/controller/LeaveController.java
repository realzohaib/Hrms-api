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
import org.springframework.web.multipart.MultipartFile;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.form.service.ILeaveService;

@RestController
@RequestMapping("/api/v1")
public class LeaveController {

	@Autowired
	private ILeaveService iLeaveService;

//	This method for send the leave request to manager
	@PostMapping("/leave/request")
	public ResponseEntity<?> createLeaveApproval(@RequestParam("leaveApproval") String leaveApproval,
			@RequestParam("medicalDocumentsName") MultipartFile medicalDocumentsName) throws IOException {
		try {
			iLeaveService.createLeaveApproval(leaveApproval, medicalDocumentsName);
			return new ResponseEntity<>(new MessageResponse("Your leave request send to your manager."), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>((new MessageResponse("Error while creating leave approval. " + e)),
					HttpStatus.BAD_REQUEST);
		}
	}

//	This method for get the leave request by LeaveRequestId
	@GetMapping("/leave/request/{leaveRequestId}")
	public ResponseEntity<?> getleaveRequestById(@PathVariable Long leaveRequestId) throws Exception {
		try {
			return new ResponseEntity<>(iLeaveService.getleaveRequestById(leaveRequestId), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("Leave Request with ID: " + leaveRequestId + " not found "),
					HttpStatus.NOT_FOUND);
		}
	}

//	This method for find all the Leave Request by employeeId
	@GetMapping("/findall/leave/request/with/employeeId/{employeeId}")
	public ResponseEntity<?> getLeaveRequestByEmployeeId(@PathVariable Long employeeId) {
		try {
			return new ResponseEntity<>(iLeaveService.getLeaveRequestByEmployeeId(employeeId), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(
					new MessageResponse("leave request with employee ID: " + employeeId + " not foundm"),
					HttpStatus.NOT_FOUND);
		}
	}

//	This method for find all leave request
	@GetMapping("/findAll/leaverequest")
	public ResponseEntity<?> findAllLeaveApproval() {
		try {
			return new ResponseEntity<>(iLeaveService.findAllLeaveApproval(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("No Leave request "), HttpStatus.NOT_FOUND);
		}
	}

//	This method for update the leave request by the manager Accepted or Rejected with the help of leaveRequestId
	@PutMapping("/leave/request/approvedByManager/{leaveRequestId}")
	public ResponseEntity<?> approvedByManager(@PathVariable Long leaveRequestId,
			@RequestParam("leaveApproval") String leaveApproval) throws IOException {
		try {
			iLeaveService.approvedByManager(leaveRequestId, leaveApproval);
			return new ResponseEntity<>(new MessageResponse("Your request is approved or denied By manager"),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("Error while approving leave request. " + e),
					HttpStatus.BAD_REQUEST);
		}
	}

//	This method for find all pending request
	@GetMapping("/leave/request/findall/pending")
	public ResponseEntity<?> findAllLeaveApprovalPending() {
		try {
			return new ResponseEntity<>(iLeaveService.findAllLeaveApprovalPending(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("No leave request pending"), HttpStatus.NOT_FOUND);
		}
//		return ResponseEntity.ok();
	}

//	This method for find all accepted request
	@GetMapping("/leave/request/findall/accepted")
	public ResponseEntity<?> findAllLeaveApprovalAccepted() {
		try {
			return new ResponseEntity<>(iLeaveService.findAllLeaveApprovalAccepted(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("No leave request accepted "), HttpStatus.NOT_FOUND);
		}
	}

//	This method for find all rejected request
	@GetMapping("/leave/request/findall/rejected")
	public ResponseEntity<?> findAllLeaveApprovalRejected() {
		try {
			return new ResponseEntity<>(iLeaveService.findAllLeaveApprovalRejected(), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("No leave request rejected "), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/calculateTotalLeaveDays/{employeeId}")
	public ResponseEntity<?> calculateTotalLeaveDays(@PathVariable Long employeeId) {
		try {
			return new ResponseEntity<>(iLeaveService.calculateTotalNumberOfDaysRequestedByEmployee(employeeId),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("No data available now"), HttpStatus.NOT_FOUND);
		}
	}

//	This method for calculate total number of leave days with employee id and leave name
	@GetMapping("/calculateTotalLeaveDays/{employeeId}/leaveName/{leaveName}")
	public ResponseEntity<?> calculateTotalSpecificLeaveDays(@PathVariable Long employeeId,
			@PathVariable String leaveName) {
		try {
			return new ResponseEntity<>(
					iLeaveService.calculateTotalSpecificNumberOfDaysRequestedByEmployee(employeeId, leaveName),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("No data available now"), HttpStatus.NOT_FOUND);
		}
	}

}