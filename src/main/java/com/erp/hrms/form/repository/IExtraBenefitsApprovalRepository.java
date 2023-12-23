package com.erp.hrms.form.repository;

import java.util.List;

import com.erp.hrms.entity.form.ExtraBenefitsApproval;

public interface IExtraBenefitsApprovalRepository {

	public void createExtraBenefitsApproval(ExtraBenefitsApproval extraBenefitesApproval);
	
	public List<ExtraBenefitsApproval> findAllBenefitsApproval();
	
	public ExtraBenefitsApproval getBenefitApprovalByExtraBenefitsRequestId(long extraBenefitsRequestId);
	
	public List<ExtraBenefitsApproval> getBenefitApprovalByEmployeeId(long employeeId);
}
