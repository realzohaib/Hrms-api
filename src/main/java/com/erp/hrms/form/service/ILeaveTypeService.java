package com.erp.hrms.form.service;

import java.io.IOException;
import java.util.List;

import com.erp.hrms.entity.form.LeaveType;

public interface ILeaveTypeService {

	public void predefinedLeaveType();

	public void createLeaveType(String leaveType) throws IOException;

	public List<LeaveType> findAllLeaveType();

	public LeaveType findByLeaveTypeId(Long leaveTypeId);

}
