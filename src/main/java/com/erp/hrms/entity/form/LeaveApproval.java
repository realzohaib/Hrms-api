package com.erp.hrms.entity.form;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import lombok.Data;

@Entity
@Data
public class LeaveApproval {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long leaveRequestId;
	private Long employeeId;
	private String nameOfEmployee;
	private String designation;
	private String department;
	private double numberOfDaysRequested;
	private String leaveReason;
	private String requestDate;
	private String startDate;
	private String endDate;

	private String approvingManagerName;
	private String approvalStatus;
	private String approvalRemarks;

	private String email;
	private String contactNumber;
	private String emergencyContactNumber;

	private String medicalDocumentsName;
	private String jobLevel;
	private String location;

	private String managerEmail;

	@Transient
	private byte[] medicalDocumentData;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "leaveTypeLeaveApproval", referencedColumnName = "leaveTypeId")
	private LeaveType leaveType;

}