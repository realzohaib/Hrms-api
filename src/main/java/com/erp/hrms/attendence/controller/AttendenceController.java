package com.erp.hrms.attendence.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.attendence.entity.Attendence;
import com.erp.hrms.attendence.repo.IAttendencerepo;
import com.erp.hrms.attendence.service.AttendenceRequest;
import com.erp.hrms.attendence.service.AttendenceServiceImpl;
import com.erp.hrms.attendence.service.AttendencenotRegistered;
import com.erp.hrms.attendence.service.DuplicateEntryException;

@RestController
//@RequestMapping("/api/auth")
public class AttendenceController {
	
	@Autowired
    private	AttendenceServiceImpl service;
	
	@Autowired
	private IAttendencerepo repo;
	
	
	@PostMapping("/punch-In-Time")
	public ResponseEntity<?> punchInTime(@RequestBody Attendence attendence) throws AttendencenotRegistered{
		
		if(repo.existsByEmployeeIdAndDate(attendence.getEmployeeId(), attendence.getDate())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Employee Already punched -in"));
		}
	try {
		Attendence punchIn = service.punchIn(attendence);
		return ResponseEntity.ok(punchIn);
	}catch (Exception e) {
		return ResponseEntity.badRequest().body(e);
	}
		
	}
	
	@GetMapping("/get-attendence")
	public ResponseEntity<?> getAttendenceById(@RequestBody AttendenceRequest req) throws AttendencenotRegistered{
	try {
		List<Attendence> list = service.getEmployeeAttendence(req.getId());
		return ResponseEntity.ok(list);
	}catch (Exception e) {
		return ResponseEntity.badRequest().body(e);
	}
		
	}

	
	@PostMapping("/punch-out")
	public ResponseEntity<?> punchOut(@RequestBody AttendenceRequest req) throws AttendencenotRegistered{
	try {
		Attendence punchout = service.punchout(req.getId());
		return ResponseEntity.ok(punchout);
	}catch (Exception e) {
		return ResponseEntity.badRequest().body(e);
	}
		
	}
	
	@PostMapping("/break-start")
	public ResponseEntity<?> breakStart(@RequestBody AttendenceRequest req) throws AttendencenotRegistered{
	try {
		Attendence breakStart = service.breakStart(req.getId());
		return ResponseEntity.ok(breakStart);
	}catch (Exception e) {
		return ResponseEntity.badRequest().body(e);
	}
		
	}
	
	@PostMapping("/break-end")
	public ResponseEntity<?> breakEnd(@RequestBody AttendenceRequest req) throws AttendencenotRegistered{
	try {
		Attendence breakStart = service.breakEnd(req.getId());
		return ResponseEntity.ok(breakStart);
	}catch (Exception e) {
		return ResponseEntity.badRequest().body(e);
	}
		
	}
	
	@GetMapping("/get-attendence-byDate")
	public ResponseEntity<?>AttendenceByDate(@RequestBody AttendenceRequest req){
		System.out.println("hello");
		List<Attendence> list = service.getAttendenceByDate(req.getId(), req.getStartDate(), req.getEndODate());
		return ResponseEntity.ok(list);
	}
}
