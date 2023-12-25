package com.erp.hrms.helper.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.helper.entity.monthCycle;
import com.erp.hrms.helper.service.IMonthCycleService;

@RestController
@RequestMapping("/api/v1")
public class MonthCycleController {

	@Autowired
	private IMonthCycleService iMonthCycleService;

	@PostMapping("/save/month")
	public ResponseEntity<?> saveMonthCycle(@RequestBody monthCycle monthCycle) {
		iMonthCycleService.saveMonthCycle(monthCycle);
		return ResponseEntity.ok(new MessageResponse("Your month cycle save."));
	}
}
