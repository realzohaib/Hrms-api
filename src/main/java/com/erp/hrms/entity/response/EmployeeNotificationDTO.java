package com.erp.hrms.entity.response;

import lombok.Data;

@Data
public class EmployeeNotificationDTO {

	private Long employeeId;
	private String nameOfEmployee;
	private String designation;
	private String department;
	private String jobLevel;
	private String location;

	private double numberOfDaysRequested;
	private String leaveReason;
	private String requestDate;
	private String startDate;
	private String endDate;

	private String approvingManagerName;
	private String approvalStatus;
	private String approvalRemarks;
	private String managerEmail;

	private String hrName;
	private String hrApprovalStatus;
	private String hrEmail;

	private String email;
	private String contactNumber;
	private String emergencyContactNumber;
	private double noOfLeavesApproved;

}
