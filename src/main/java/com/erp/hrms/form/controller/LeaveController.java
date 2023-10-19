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
import com.erp.hrms.entity.form.LeaveApproval;
import com.erp.hrms.form.service.ILeaveService;

@RestController
@RequestMapping("/api/v1")
public class LeaveController {

	@Autowired
	private ILeaveService iLeaveService;

//	This method for send the leave request to manager
	@PostMapping("/leave/request")
	public ResponseEntity<?> createLeaveApproval(@RequestParam("leaveApproval") String leaveApproval)
			throws IOException {
		iLeaveService.createLeaveApproval(leaveApproval);
		return ResponseEntity.ok(new MessageResponse("Your leave request send to your manager."));
	}

//	This method for get the leave request by LeaveRequestId
	@GetMapping("/leave/request/{leaveRequestId}")
	public ResponseEntity<?> getleaveRequestById(@PathVariable long leaveRequestId) {
		return ResponseEntity.ok(iLeaveService.getleaveRequestById(leaveRequestId));
	}

//	This method for find all the Leave Request by employeeId
	@GetMapping("/findall/leave/request/with/employeeId/{employeeId}")
	public ResponseEntity<List<LeaveApproval>> getLeaveRequestByEmployeeId(@PathVariable long employeeId) {
		return ResponseEntity.ok(iLeaveService.getLeaveRequestByEmployeeId(employeeId));
	}

//	This method for find all leave request
	@GetMapping("/findAll/leaverequest")
	public ResponseEntity<?> findAllLeaveApproval() {
		return ResponseEntity.ok(iLeaveService.findAllLeaveApproval());
	}

//	This method for update the leave request by the manager Accepted or Rejected with the help of leaveRequestId
	@PutMapping("/leave/request/approvedByManager/{leaveRequestId}")
	public ResponseEntity<?> approvedByManager(@PathVariable long leaveRequestId,
			@RequestParam("leaveApproval") String leaveApproval) throws IOException {
		iLeaveService.approvedByManager(leaveRequestId, leaveApproval);
		return ResponseEntity.ok(new MessageResponse("Your request is approved or denied By manager"));
	}


}