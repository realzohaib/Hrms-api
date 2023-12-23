package com.erp.hrms.payroll.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.erp.hrms.payments.Tax;

import lombok.Data;

@Entity
@Data
public class PayRoll {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payroll_id")
	private long payrollId;
	private long employeeId;
	private String name;
	private String jobDesignation;
	private String jobLevel;
	private String jobLocation;
	private String department;
	private String contact;
	private String address;

	private int month;
	private int year;

	private double basicPay;

	@Embedded
	private Allowances allowances;

	private double vehicleCashAmount;
	private double electricityAllocationAmount;
	private double rentAllocationAmount;

	private String incentivesName;
	private double incentiveAmount;

	private String anySpecialReward;
	private double anySpecialRewardAmount;
	private double bonus;

	private double overtimePayAmount;
	private double overtimePay;
	private double monthlyPerformancePay;
	private double totalPay;

	private String methodUsedForPayment;
	private LocalDate dateOfPayment;

	@Embedded
	private Deductions deductions;
	


	@OneToMany(targetEntity = Tax.class , mappedBy = "payroll")
	@Cascade(CascadeType.ALL)
	@ElementCollection
	private List<Tax> tax;

}
