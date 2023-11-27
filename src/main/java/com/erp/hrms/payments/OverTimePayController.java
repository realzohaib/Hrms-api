package com.erp.hrms.payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.erp.hrms.api.security.response.MessageResponse;

@Controller
public class OverTimePayController {
	
	@Autowired
	private OvertimePayService service;
	
	@PostMapping("/save-overtimepay")
	public ResponseEntity<?>saveOverTimeAmount(@RequestBody OverTimePay overtimepay){
		try {
			service.saveOverTimeAmount(overtimepay);
			return ResponseEntity.ok(new MessageResponse("OverTime payment saved"));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("failed to save " + e.getMessage()));
		}
		
	}
	
	
	@GetMapping("/get-overtimepay")
	public ResponseEntity<?>getovertimeAmount(){
		try {
			return ResponseEntity.ok().body(service.getovertimeAmount());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("failed to get " + e.getMessage()));
		}
		
	}
	
	@PutMapping("/update-overtimepay")
	public ResponseEntity<?>updateovertimeAmount(@RequestBody OverTimePay pay){
		
		try {
			return ResponseEntity.ok().body(service.updateovertimepay(pay.getId(), pay));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("failed to update " + e.getMessage()));
		}
		
	}

}
