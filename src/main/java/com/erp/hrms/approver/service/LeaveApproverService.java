package com.erp.hrms.approver.service;

import java.util.List;
import java.util.Optional;

import com.erp.hrms.approver.entity.LeaveApprover;

public interface LeaveApproverService {

	public void createLeaveApprover(LeaveApprover leaveApprover);
	
	public Optional<LeaveApprover> findLeaveApproverById(Long lAId);
	
	public List<LeaveApprover> findAllLeaveApprovers();
	
	public LeaveApprover updateLeaveApproverEndDate(Long lAId, LeaveApprover leaveApprover);
	
	

}
