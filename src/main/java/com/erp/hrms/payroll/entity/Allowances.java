package com.erp.hrms.payroll.entity;

import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class Allowances {

	private String houseRentAmount;
	private String foodAllowanceAmount;
	private String vehicleAllowanceAmount;
	private String uniformAllowanceAmount;
	private String travellingAllowancesAmount;
	private String educationalAllowanceAmount;
	private String otherAllowanceAmount;

}
