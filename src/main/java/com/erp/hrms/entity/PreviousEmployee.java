package com.erp.hrms.entity;

//import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Data
@Table(name = "previous_employee")
public class PreviousEmployee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long previousId;
	private String companyName;
	private String companyAddress;
	private String designation;
	private String description;
	private String dateFrom;
	private String dateTo;
	private String previousManagerName;
	private String previousManagerPhoneCode;
	private String previousManagerContact;
	private String previousHRName;
	private String previousHRPhoneCode;
	private String previousHRContact;
	private double lastWithdrawnSalary;

	private String payslipScan;

	@Transient
	private byte[] payslipScanData;

	@ManyToOne
	@JoinColumn(name = "employee_id")
	@JsonBackReference
	private PersonalInfo personalinfo;

}
