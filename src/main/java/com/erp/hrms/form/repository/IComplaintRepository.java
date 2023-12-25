package com.erp.hrms.form.repository;

import java.util.List;

import com.erp.hrms.entity.form.ComplaintForm;

public interface IComplaintRepository {

	public void sendComplaintRequest(ComplaintForm complaintForm);

	public ComplaintForm getComplaintRequestById(long complaintId);

	public List<ComplaintForm> findAllComplaints();

	public ComplaintForm complaintApprovedOrDeniedOrPending(long complaineId, ComplaintForm complaintForm);

	public List<ComplaintForm> getComplaintRequestByEmployeeId(long employeeId);
}
