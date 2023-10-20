package com.erp.hrms.form.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.erp.hrms.entity.form.ExtraBenefitsApproval;

public interface IExtraBenefitsApprovalService {

	public void createExtraBenefitsApproval(String extraBenefitsApproval, MultipartFile supportingDocumentsName)
			throws IOException;

	public List<ExtraBenefitsApproval> findAllExtraBenefitsApproval();

	public ExtraBenefitsApproval getBenefitApprovalByExtraBenefitsRequestId(long extraBenefitsRequestId);

	public List<ExtraBenefitsApproval> getBenefitApprovalByEmployeeId(long employeeId);

}
