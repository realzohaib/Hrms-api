package com.erp.hrms.form.service;

import java.util.List;

import com.erp.hrms.entity.form.LeaveRecords;

public interface ILeaveRecordsService {

	public LeaveRecords saveLeaveRecords(LeaveRecords leaveRecords);

	public LeaveRecords findByIdLeaveRecords(Long id);

	public List<LeaveRecords> findAllLeaveRecords();

//	public LeaveRecords updateLeaveRecords(long id, )
}
