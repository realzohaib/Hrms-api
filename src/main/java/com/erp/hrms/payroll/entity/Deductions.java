package com.erp.hrms.payroll.entity;

import javax.persistence.Embeddable;	

import lombok.Data;

@Data
@Embeddable
public class Deductions {
	private double leaveDayCut;
	private double tardyDayCut;
	private double halfDayCut;
	private String otherDeductionsName;
	private double otherDeductions;
	private double anyOtherSpecificDeduction;
	private double totalDeductions;

}
