package com.erp.hrms.form.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.erp.hrms.entity.form.LeaveApproval;
import com.erp.hrms.entity.form.LeaveCalendarData;
import com.erp.hrms.entity.form.LeaveCountDTO;
import com.erp.hrms.entity.form.LeaveDataDTO;
import com.erp.hrms.entity.form.MarkedDate;

public interface ILeaveService {

	public void createLeaveApproval(String leaveApproval, MultipartFile medicalDocumentsName) throws IOException;

	public LeaveApproval getleaveRequestById(Long leaveRequestID) throws IOException;

	public List<LeaveApproval> getLeaveRequestByEmployeeId(Long employeeId) throws IOException;

	public List<LeaveApproval> findAllLeaveApproval() throws IOException;

	public LeaveApproval approvedByManager(Long leaveRequestId, String leaveApproval,
			MultipartFile medicalDocumentsName) throws IOException;

	public LeaveApproval approvedOrDenyByHR(Long leaveRequestId, String leaveApproval,
			MultipartFile medicalDocumentsName) throws IOException;

	public List<LeaveApproval> findAllLeaveApprovalPending() throws IOException;

	public List<LeaveApproval> findAllLeaveApprovalAccepted() throws IOException;

	public List<LeaveApproval> findAllLeaveApprovalRejected() throws IOException;

	public BigDecimal calculateTotalNumberOfDaysRequestedByEmployee(Long employeeId);

	public BigDecimal calculateTotalSpecificNumberOfDaysRequestedByEmployee(Long employeeId, String leaveName);

	public List<LeaveCalendarData> generateLeaveCalendar(List<LeaveApproval> leaveApprovals);

	public List<LeaveApproval> getAllLeaveApprovalsAccepted();

	public List<MarkedDate> markCalendarDates();

	public BigDecimal calculateTotalNumberOfDaysRequestedByEmployeeInMonthAndStatus(Long employeeId, int year,
			int month);

	public List<LeaveCountDTO> getAllLeavesByEmployeeIdAndYear(Long employeeId, int year, String countryName);

	public List<LeaveCountDTO> getAllLeaveByMonthByEmployeeId(int year, int month, long employeeId, String countryName);

	public LeaveDataDTO getLeaveDataByDate(String date);

}
