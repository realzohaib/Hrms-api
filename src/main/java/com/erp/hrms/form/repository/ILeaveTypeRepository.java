package com.erp.hrms.form.repository;

import java.util.List;

import com.erp.hrms.entity.form.LeaveType;

public interface ILeaveTypeRepository {
	
	public void predefinedLeaveType(List<LeaveType> initialLeaveType);

	public void createLeaveType(LeaveType leaveType);

	public List<LeaveType> findAllLeaveType();

	public LeaveType findByLeaveTypeId(Long leaveTypeId);

	public LeaveType findByLeaveName(String leaveName);

}
