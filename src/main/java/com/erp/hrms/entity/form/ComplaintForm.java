package com.erp.hrms.entity.form;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class ComplaintForm {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long complaintId;
	private Long employeeId;
	private String nameOfEmployee;
	private String department;
	private String contactNumber;
	private String post;
	private String email;
	private String dateOfComplaint;
	
	private String complaintRegarding;
	private String accused; 	//	Name of the company/person against which/whom the complaint is filed
	private String dateOfIncident;
	private String timeOfIncident;
	private String locationOfIncident;
	private String describeTheIncident;
	private String witnessesOfIncident;
	private String numbersOfComplaints;
	
	private String approvedOrDeniedOrPending;
	private String remark;
	
}
