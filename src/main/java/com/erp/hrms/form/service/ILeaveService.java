package com.erp.hrms.form.service;

import java.io.IOException;
import java.util.List;

import com.erp.hrms.entity.form.LeaveApproval;

public interface ILeaveService {

	public void createLeaveApproval(String leaveApproval) throws IOException;

	public LeaveApproval getleaveRequestById(long leaveRequestID);

	public List<LeaveApproval> getLeaveRequestByEmployeeId(long employeeId);

	public List<LeaveApproval> findAllLeaveApproval();

	public LeaveApproval approvedByManager(long leaveRequestId, String leaveApproval) throws IOException;

	public List<LeaveApproval> findAllLeaveApprovalPending();
}