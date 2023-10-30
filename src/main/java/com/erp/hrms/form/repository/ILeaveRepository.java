package com.erp.hrms.form.repository;

import java.util.List;

import com.erp.hrms.entity.form.LeaveApproval;

public interface ILeaveRepository {

	public void createLeaveApproval(LeaveApproval leaveApproval);

	public LeaveApproval approvedByManager(Long leaveRequestId, LeaveApproval leaveApproval);

	public LeaveApproval getleaveRequestById(Long leaveRequestId);

	public List<LeaveApproval> findAllLeaveApproval();

	public List<LeaveApproval> getLeaveRequestByEmployeeId(Long employeeId);

	public List<LeaveApproval> findAllLeaveApprovalPending();

	public List<LeaveApproval> findAllLeaveApprovalAccepted();

	public List<LeaveApproval> findAllLeaveApprovalRejected();

	List<LeaveApproval> getLeaveApprovalByEmployeeId(Long employeeId);

	void updateLeaveApproval(LeaveApproval leaveApproval);

}