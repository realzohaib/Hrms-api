package com.erp.hrms.form.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.form.service.IExtraBenefitsApprovalService;

@RestController
@RequestMapping("/api/v1")
public class ExtraBenefitsApprovalController {

	@Autowired
	IExtraBenefitsApprovalService iExtraBenefitsApprovalService;

	@PostMapping("/extraBenefits")
	public ResponseEntity<?> createExtraBenefitsApproval(@RequestParam("extraBenefits") String extraBenefits,
			@RequestParam("supportingDocumentsName") MultipartFile supportingDocumentsName) throws IOException {
		iExtraBenefitsApprovalService.createExtraBenefitsApproval(extraBenefits, supportingDocumentsName);
		return ResponseEntity.ok(new MessageResponse("Your Extra benefits request send."));
	}

}
