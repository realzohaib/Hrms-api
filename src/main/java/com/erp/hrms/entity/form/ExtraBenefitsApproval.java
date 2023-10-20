package com.erp.hrms.entity.form;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class ExtraBenefitsApproval {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long extraBenefitsRequestId;
	private Long employeeId; 
	private String employeeName;
	private String post;
	private String department;
	private String contectNumber;
	private String email;
	private String dateOfRequest;

	private String typeOfBenefit;
	private String reasonForRequest;
	private String justification;
	private String startDate;
	private String endDate;
	private String budgetImpact;

	private String supportingDocumentsName; // this field is image name save to database and frontend dynamic field

	private String departmentManagerStatus;
	private String departmentManagerRemark; 
	private String hrDepartmentStatus;
	private String hrDepartmentRemark;
	private String adminStatus;
	private String adminRemark;

}
