package com.erp.hrms.payroll.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class SalaryCerti {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long salaryCertiId;
	private long employeeId;
	private int month;
	private int year;
	private String hrApprovalStatusForCerti;
	private String managerApprovalStatusForCerti;

}
