package com.erp.hrms.approver.service;

import java.util.List;
import java.util.Optional;

import com.erp.hrms.approver.entity.LeaveApprover;
import com.erp.hrms.entity.form.LeaveApproval;
import com.erp.hrms.entity.response.LeaveApproverDTO;

public interface LeaveApproverService {

	public void createLeaveApprover(LeaveApprover leaveApprover);

	public Optional<LeaveApprover> findLeaveApproverById(Long lAId);

	public List<LeaveApprover> findAllLeaveApprovers();

	public LeaveApprover updateLeaveApproverEndDate(Long lAId, LeaveApprover leaveApprover);

	public List<LeaveApproverDTO> findByFirstApproverEmpId(Long firstApproverEmpId);

	public List<LeaveApproverDTO> findBySecondApproverEmpId(Long secondApproverEmpId);
	
	public List<LeaveApproverDTO> findByApproverEmpId(Long approverEmpId);

}