package com.erp.hrms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.api.service.IDepartmentService;
import com.erp.hrms.entity.Department;

@RestController
@RequestMapping("api/v1")
public class DepartmentController {

	@Autowired
	private IDepartmentService idepartmentService;

	@PostMapping("/save/department")
	public ResponseEntity<?> saveDepartment(@RequestBody Department department) {
		try {
			idepartmentService.saveDepartment(department);
			return new ResponseEntity<>(new MessageResponse("your department save."), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(
					new MessageResponse("Your department is not save because is somthing went wrong"),
					HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/departmentId/{departmentId}")
	public ResponseEntity<?> findDepartmentById(@PathVariable Long departmentId) {
		try {
			return new ResponseEntity<>(idepartmentService.getDepartmentById(departmentId),HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity<>(new MessageResponse("No record found"),HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/all/departments")
	public ResponseEntity<?> findAllDepartments() {
		try {
			return new ResponseEntity<>(idepartmentService.getAllDepartment(),HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity<>(new MessageResponse("No record found"),HttpStatus.NOT_FOUND);
		}
	}
	
}
