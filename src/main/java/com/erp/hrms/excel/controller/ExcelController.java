package com.erp.hrms.excel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.erp.hrms.api.security.response.StatusResponse;
import com.erp.hrms.api.security.utll.AuthFilter;
import com.erp.hrms.api.security.utll.JwtTokenUtill;
import com.erp.hrms.excel.helper.Helper;
import com.erp.hrms.excel.service.ExcelService;

@Controller
@RequestMapping("/api/v1")
public class ExcelController {
	@Autowired
	private ExcelService service;

	@Autowired
	private JwtTokenUtill filter;

	@GetMapping("test")
	private ResponseEntity<?> privateapi(@RequestHeader("Authorization") String auth) {
		System.out.println(auth);
		filter.validateJwtToken(auth);
		return ResponseEntity.ok("this is privates");
	}

	@PostMapping("/uploadexcel")
	public ResponseEntity<?> uploadfile(@RequestParam("file") MultipartFile file) {
		if (Helper.hasExcelFormat(file)) {
			try {
				System.out.println("it reached");
				service.savefile(file);
				System.out.println("finish");
				return ResponseEntity.ok(new StatusResponse(true));
			} catch (Exception e) {
				return ResponseEntity.ok(new StatusResponse(false));
			}
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StatusResponse(false));
	}
}
