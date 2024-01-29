package com.erp.hrms.approver.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.hrms.approver.entity.LeaveApprover;
import com.erp.hrms.approver.service.LeaveApproverService;
import com.erp.hrms.entity.response.LeaveApproverDTO;

@RestController
@RequestMapping("/api/v1")
public class LeaveApproverController {

	@Autowired
	private LeaveApproverService leaveApproverService;

	@PostMapping("/leaveApprovers")
	public ResponseEntity<String> createLeaveApprover(@RequestBody LeaveApprover leaveApprover) {
		try {
			leaveApproverService.createLeaveApprover(leaveApprover);
			return new ResponseEntity<>("Leave Approver created successfully", HttpStatus.CREATED);
		} catch (Exception e) {
			System.out.println(e);
			return new ResponseEntity<>("Failed to create Leave Approver", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/leaveApprover/id/{lAId}")
	public ResponseEntity<LeaveApprover> getLeaveApproverById(@PathVariable Long lAId) {
		Optional<LeaveApprover> leaveApprover = leaveApproverService.findLeaveApproverById(lAId);
		return leaveApprover.map(response -> ResponseEntity.ok().body(response))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@GetMapping("/findall/leaveapprovers")
	public ResponseEntity<List<LeaveApprover>> getAllLeaveApprovers() {
		List<LeaveApprover> leaveApprovers = leaveApproverService.findAllLeaveApprovers();
		return ResponseEntity.ok().body(leaveApprovers);
	}

	@PutMapping("/leave/approver/{lAId}")
	public ResponseEntity<LeaveApprover> updateLeaveApproverEndDate(@PathVariable Long lAId,
			@RequestBody LeaveApprover updatedLeaveApprover) {
		LeaveApprover updatedApprover = leaveApproverService.updateLeaveApproverEndDate(lAId, updatedLeaveApprover);
		return new ResponseEntity<>(updatedApprover, HttpStatus.OK);
	}

	@GetMapping("/findByFirstApproverEmpId/{firstApproverEmpId}")
	public ResponseEntity<List<LeaveApproverDTO>> findByFirstApproverEmpId(@PathVariable Long firstApproverEmpId) {
		List<LeaveApproverDTO> leaveApproverDTOs = leaveApproverService.findByFirstApproverEmpId(firstApproverEmpId);

		if (leaveApproverDTOs.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(leaveApproverDTOs, HttpStatus.OK);
		}
	}

	@GetMapping("/findBySecondApproverEmpId/{secondApproverEmpId}")
	public ResponseEntity<List<LeaveApproverDTO>> findBySecondApproverEmpId(@PathVariable Long secondApproverEmpId) {
		List<LeaveApproverDTO> leaveApproverDTOs = leaveApproverService.findBySecondApproverEmpId(secondApproverEmpId);

		if (leaveApproverDTOs.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<>(leaveApproverDTOs, HttpStatus.OK);
		}
	}

	
}
