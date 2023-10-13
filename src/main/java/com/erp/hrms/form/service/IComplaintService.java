package com.erp.hrms.form.service;

import java.io.IOException;
import java.util.List;

import com.erp.hrms.entity.form.ComplaintForm;
import com.erp.hrms.entity.form.LeaveApproval;

public interface IComplaintService {

	public void sendComplaintRequest(String complaintForm) throws IOException;

	public ComplaintForm getComplaintRequestById(long complaintId);

	public List<ComplaintForm> findAllComplaints();

	public ComplaintForm complaintApprovedOrDeniedOrPending(long complaintId, String complaintForm) throws IOException;
	
	public List<ComplaintForm> getComplaintRequestByEmployeeId(long employeeId);
}
