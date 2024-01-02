package com.erp.hrms.joblevelandDesignationCONTROLLER;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.joblevelandDesignationEntity.JobLevel;
import com.erp.hrms.joblevelandDesignationSERVICE.joblevelServiceImpl;

@RestController
public class JoblevelController {

	@Autowired
	private joblevelServiceImpl service;

	@PostMapping(value = "/saveJobLevel")
	public ResponseEntity<?> savePayRoll(/* @RequestBody JobLevel joblevel */
	 @RequestParam("levelName") String levelName) {
		try {
			JobLevel joblevel = new JobLevel();
			joblevel.setLevelName(levelName);
			return ResponseEntity.ok().body(service.savejoblevel(joblevel));
		} catch (Exception e) {
			return new ResponseEntity<>(new MessageResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/getAllJobLevel")
	public ResponseEntity<?> loadAllJobLevel() {
		try {
			return ResponseEntity.ok().body(service.loadAllJobLevel());

		} catch (Exception e) {
			return ResponseEntity.ok().body(e.getMessage());
		}
	}

}
