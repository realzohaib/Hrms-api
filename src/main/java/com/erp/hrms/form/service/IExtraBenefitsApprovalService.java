package com.erp.hrms.form.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface IExtraBenefitsApprovalService {

	public void createExtraBenefitsApproval(String extraBenefitsApproval, MultipartFile supportingDocumentsName)
			throws IOException;

}
