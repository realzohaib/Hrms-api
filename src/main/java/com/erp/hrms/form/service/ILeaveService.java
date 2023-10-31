package com.erp.hrms.form.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.erp.hrms.entity.form.LeaveApproval;

public interface ILeaveService {

	public void createLeaveApproval(String leaveApproval, MultipartFile medicalDocumentsName) throws IOException;

	public LeaveApproval getleaveRequestById(Long leaveRequestID);

	public List<LeaveApproval> getLeaveRequestByEmployeeId(Long employeeId);

	public List<LeaveApproval> findAllLeaveApproval();

	public LeaveApproval approvedByManager(Long leaveRequestId, String leaveApproval) throws IOException;

	public List<LeaveApproval> findAllLeaveApprovalPending();

	public List<LeaveApproval> findAllLeaveApprovalAccepted();

	public List<LeaveApproval> findAllLeaveApprovalRejected();

	public BigDecimal calculateTotalNumberOfDaysRequestedByEmployee(Long employeeId);

	public BigDecimal calculateTotalSpecificNumberOfDaysRequestedByEmployee(Long employeeId, String leaveName);

}
