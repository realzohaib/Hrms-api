package com.erp.hrms.form.repository;

import java.util.List;

import com.erp.hrms.entity.form.LeaveApproval;

public interface ILeaveRepository {

	public void createLeaveApproval(LeaveApproval leaveApproval);

	public LeaveApproval approvedByManager(long leaveRequestId, LeaveApproval leaveApproval);

	public LeaveApproval getleaveRequestById(long leaveRequestId);

	public List<LeaveApproval> findAllLeaveApproval();

	public List<LeaveApproval> getLeaveRequestByEmployeeId(long employeeId);


}