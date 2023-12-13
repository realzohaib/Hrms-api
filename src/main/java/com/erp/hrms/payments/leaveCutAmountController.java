package com.erp.hrms.payments;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.hrms.api.security.response.MessageResponse;

@RestController
@RequestMapping("/api/v1")
public class leaveCutAmountController {

	@Autowired
	private leaveCutAmountService service;

	@PostMapping("/manager/save-deduction")
	public ResponseEntity<?> savedeductionpercent(@RequestBody leaveCutAmount leavecut) {
		try {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.savedeductionAmount(leavecut));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(e.getMessage()));
		}

	}

	@GetMapping("/manager/get-deduction")
	public ResponseEntity<?> getalldeductionpercent() {
		try {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.getall());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(e.getMessage()));
		}
	}

	@PutMapping("/manager/update-deduction")
	public ResponseEntity<?> updatedeductionpercent(@RequestBody leaveCutAmount l1) {
		try {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.update(l1));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(e.getMessage()));
		}
	}

}
