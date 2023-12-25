package com.erp.hrms.controller;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.api.service.ILevelService;
import com.erp.hrms.entity.Level;

@RestController
@RequestMapping("/api/v1")
public class LevelController {

	@Autowired
	private ILevelService iLevelService;

	@PostConstruct
	public ResponseEntity<?> savePredefinedLevels() {
		try {
			iLevelService.saveInitialLevels(); 
			return new ResponseEntity<>(new MessageResponse("Your existing data save to database"), HttpStatus.OK);
		} catch (DataIntegrityViolationException e) {
			return new ResponseEntity<>(new MessageResponse("Duplicate Level entry found"), HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/level")
	public ResponseEntity<?> saveLevel(@RequestBody Level level) {

		try {
			iLevelService.saveLevel(level);
			return new ResponseEntity<>(new MessageResponse("Your level is saved."), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("An error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/level/{lid}")
	public ResponseEntity<?> findByLevelId(@PathVariable Long lid) {
		try {
			Level findByLevelId = iLevelService.findByLevelId(lid);
			if (findByLevelId == null) {
				return new ResponseEntity<>(new MessageResponse("No records found"), HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<>(findByLevelId, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("An error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/all/levels")
	public ResponseEntity<?> findAllDepartments() {
		try {
			List<Level> findAllLevel = iLevelService.findAllLevel();
			if (findAllLevel.isEmpty()) {
				return new ResponseEntity<>(new MessageResponse("No records found"), HttpStatus.NOT_FOUND);
			} else {
				return new ResponseEntity<>(findAllLevel, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse("An error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
