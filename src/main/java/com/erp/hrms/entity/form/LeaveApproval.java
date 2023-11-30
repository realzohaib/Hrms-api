package com.erp.hrms.entity.form;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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

	private String hrName;
	private String hrApprovalStatus;
	private String hrApprovalRemarks;
	private String hrEmail;

	private String email;
	private String contactNumber;
	private String emergencyContactNumber;

	private String medicalDocumentsName;
	private String jobLevel;
	private String location;

	private String managerEmail;

	@Transient
	private byte[] medicalDocumentData;

	private double totalRequestedDays;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "leaveTypeLeaveApproval", referencedColumnName = "leaveTypeId")
	private LeaveType leaveType;

//	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
//	@JoinColumn(name = "leave_approval_id") // Use a different name for the foreign key column
	private List<RemaingLeaves> remaingLeavesList;

}