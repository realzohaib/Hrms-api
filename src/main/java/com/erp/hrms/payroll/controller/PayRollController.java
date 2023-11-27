package com.erp.hrms.payroll.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.payroll.entity.PayRoll;
import com.erp.hrms.payroll.service.PayRollServiceImpl;

@RestController
public class PayRollController {
	
	@Autowired
	private PayRollServiceImpl service;
	
	@GetMapping("/payRoll/{empId}")
	public ResponseEntity<?>getPayRollByEmpId(@PathVariable long empId){
		try {
			return ResponseEntity.ok(service.getPayRollByEmpId(empId));
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/payRoll")
	public ResponseEntity<?>savePayRoll(@RequestBody PayRoll payroll){
		try {
			service.savePayRoll(payroll);
			return ResponseEntity.ok().body(new MessageResponse("PayRoll Saaved"));
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/payRoll-employee-Page/{empId}")
	public ResponseEntity<?>getPayRollByEmpIdForEmployee(@PathVariable long empId){
		try {
			return ResponseEntity.ok(service.findPayrollForEmployeePage(empId));
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	

}
