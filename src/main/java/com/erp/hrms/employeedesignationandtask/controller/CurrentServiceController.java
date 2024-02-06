package com.erp.hrms.employeedesignationandtask.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.hrms.employeedesignationandtask.entity.CurrentDesignationAndTask;
import com.erp.hrms.employeedesignationandtask.requestresponseentity.CurrentReq;
import com.erp.hrms.employeedesignationandtask.requestresponseentity.CurrentRes;
import com.erp.hrms.employeedesignationandtask.service.CurrentServiceImpl;

@RestController
@RequestMapping("api/v1")
public class CurrentServiceController {

	@Autowired
	private CurrentServiceImpl service;

	@PostMapping("/save_designationTask")
	public ResponseEntity<?> saveCurrentsave(@RequestBody CurrentReq obj) {
		try {
			service.saveCurrent(obj);
			return ResponseEntity.ok("Data Saved");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	// using DesignationResponse to minimize data in response ,if we directly use
	// designation it will give all data that is irrevalent
	@GetMapping("/getAll_designationTask/{empid}")
	public ResponseEntity<?> loadAllDesignationAndTaskByEmpid(@PathVariable long empid) {
		try {
			List<CurrentRes> list = service.loadAllDesignationAndTaskByEmpId(empid);
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	// this method only load current data or duties that employee has to do
	@GetMapping("/getActive_designationTask/{empid}")
	public ResponseEntity<?> loadAllActiveDesignationAndTaskByEmpid(@PathVariable long empid) {
		try {
			List<CurrentRes> list = service.loadAllActiveDesignationAndTaskByEmpId(empid);
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	@GetMapping("/get_designationTask_ById/{currentid}")
	public ResponseEntity<?> loadByCurrentId(@PathVariable Integer currentid) {
		try {
			CurrentRes object = service.loadByCurrentId(currentid);
			return ResponseEntity.ok(object);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	@PutMapping("/end_task")
	public ResponseEntity<?> endCurrentTask(@RequestBody CurrentReq obj) {
		try {
			CurrentDesignationAndTask endCurrentTask = service.endCurrentTask(obj);
			return ResponseEntity.ok(endCurrentTask);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	@GetMapping("/findAllEmpByLevelId/{levelId}")
	public ResponseEntity<?> findAllEmpByLevelId(@PathVariable Integer levelId) {
		try {
			List<CurrentDesignationAndTask> list = service.findAllEmpByLevelId(levelId);
			if (list.isEmpty() || list == null) {
				return ResponseEntity.badRequest().body("No employee found..!!");
			}
			return ResponseEntity.ok(list);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

}