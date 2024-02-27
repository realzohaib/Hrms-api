package com.erp.hrms.weekOff.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.hrms.weekOff.service.IweekOffService;
import com.erp.hrms.weekOffEntity.WeekOff;

@RestController
@RequestMapping("api/v1")
public class WeekOffController {

	@Autowired
	private IweekOffService service;
	
	@PostMapping("/weekoff")
	public ResponseEntity<?> saveWeekOff(@RequestBody WeekOff weekoff){
		try {
			return  ResponseEntity.ok(service.changeweekOff(weekoff));
		} catch (Exception e) {
			return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	//current week-off
	@GetMapping("/weekoff/current/{empid}")
	public ResponseEntity<?> getWeekOff(@PathVariable long empid ){
		try {
			return  ResponseEntity.ok(service.getweekOff(empid));
		} catch (Exception e) {
			return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	//All week off of Employee
	@GetMapping("/Weekoff/{empid}")
	public ResponseEntity<?> AllWeekOffByEmpId(@PathVariable long empid ){
		try {
			return  ResponseEntity.ok(service.getAllWeekOff(empid));
		} catch (Exception e) {
			return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	
	
	

}
