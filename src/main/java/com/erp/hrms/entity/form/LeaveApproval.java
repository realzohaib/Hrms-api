package com.erp.hrms.entity.form;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class LeaveApproval {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long leaveRequestId;
	private Long employeeId;
	private String nameOfEmployee;
	private String email;
	private String contactNumber;
	private String designation; 
	private String department;

	private String emergencyContactNumber;
	private String requestDate;
	private String leaveType;
	private String leaveReason;
	private String startDate;
	private String endDate;
	private double numberOfDaysRequested;
	private String approvalStatus;
	private String approvingManagerName;
	private String approvalRemarks;



}