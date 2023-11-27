package com.erp.hrms.payroll.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class PayRoll {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long payRollID;
	private long employeeId;
	private String Name;
	private String jobDesignation;
	private String jobLevel;
	private String jobLocation;
	private String Department;
	private String Contact;
	private String Address;
	
	private int leavedays;
	private int  tardyDays;
	
	@Embedded
	private Allowances allowances;
	
	private String IncentivesName;
	private String IncentiveAmount;
	
	private String anySpecialReward;
	private String bonus;

	private Long OvertimePayAmount;
	private String OvertimePay;
	private String monthyPerformancePay;
	private String totalPay;
	
	

}
