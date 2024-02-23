package com.erp.hrms.joblevelanddesignation.controller;

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
import com.erp.hrms.joblevelanddesignation.request_responseentity.TaskRequest;
import com.erp.hrms.joblevelanddesignation.service.TaskServiceImpl;

@RestController
@RequestMapping("/api/v1")
public class TaskController {

	@Autowired
	private TaskServiceImpl service;

//	@PostMapping("/save_task")
	@PostMapping("/task")
	public ResponseEntity<?> savetask(@RequestBody TaskRequest req) {
		try {
			service.saveTask(req);
			return ResponseEntity.ok().body(new MessageResponse("data saved"));
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

//	@GetMapping("/getAllTask")
	@GetMapping("/task")
	public ResponseEntity<?> loadAllTask() {
		try {
			return ResponseEntity.ok().body(service.loadAllTask());
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
//	@GetMapping("/getTask/{id}")
	@GetMapping("/task/{id}")
	public ResponseEntity<?> loadTaskById(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok().body(service.laodTaskById(id));
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
}
