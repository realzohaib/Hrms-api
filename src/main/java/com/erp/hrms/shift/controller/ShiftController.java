package com.erp.hrms.shift.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.shift.dao.ShiftDaoImpl;
import com.erp.hrms.shift.entity.Shift;

@Controller
public class ShiftController {
	
	@Autowired
	private ShiftDaoImpl service;
	
	
	@PostMapping("/save-shift")
	public ResponseEntity<?>saveShift(@RequestBody Shift shift){
		try {
			service.saveShift(shift);
			return ResponseEntity.ok(new MessageResponse("Shift saved"));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("failed to save shift" + e.getMessage()));
		}
		
	}
	
	
	@GetMapping("/get-Allshift")
	public ResponseEntity<?>getAllShift(){
		try {
			return ResponseEntity.ok(service.getAllShifts());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("failed to fetched shift" + e.getMessage()));
		}
		
	}
	
	
	@PostMapping("/update-shift")
	public ResponseEntity<?>updateShift(@RequestBody Shift shift){
		try {
			service.updateShift(shift);
			return ResponseEntity.ok(new MessageResponse("Shift updated with shift name: "+shift.getShiftName() ));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("failed to updare shift" + e.getMessage()));
		}
	}

}
