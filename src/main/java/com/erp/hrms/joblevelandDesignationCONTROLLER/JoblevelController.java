package com.erp.hrms.joblevelandDesignationCONTROLLER;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	public ResponseEntity<?> savePayRoll(@RequestParam("levelName") String levelName) {
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

			// sending only levelId and Name in response , to minimise data loading time on
			// server
			List<JobLevel> list = new ArrayList<JobLevel>();
			List<JobLevel> loadAllJobLevel = service.loadAllJobLevel();

			for (JobLevel jobLevel : loadAllJobLevel) {
				JobLevel obj = new JobLevel();
				obj.setLevelId(jobLevel.getLevelId());
				obj.setLevelName(jobLevel.getLevelName());

				list.add(obj);
			}

			return ResponseEntity.ok().body(list);

		} catch (Exception e) {
			return ResponseEntity.ok().body(e.getMessage());
		}
	}

	@GetMapping("/getJobLevel/{id}")
	public ResponseEntity<?> loadJobLevel(@PathVariable Integer id) {
		try {
			return ResponseEntity.ok().body(service.getJobLevelById(id));
		} catch (Exception e) {
			return ResponseEntity.ok().body(e.getMessage());
		}
	}

}
