
package com.erp.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
@Entity
@Table(name = "job_detail")
public class JobDetails {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String companyEmailIdProvided;
	private String companyEmailId;
	private String postedLocation;

	private String basicPay;
	private String houseRentAllowance;
	private String houseRentAmount;
	private String foodAllowance;
	private String foodAllowanceAmount;

	private String vehicleAllowance;
	private String vehicleAllowanceAmount;
	private String uniformAllowance;
	private String uniformAllowanceAmount;
	private String travellingAllowances;
	private String travellingAllowancesAmount;
	private String educationalAllowance;
	private String educationalAllowanceAmount;
	private String otherAllowance;
	private String otherAllowanceAmount;

//	Perquisites
	private String vehicle;
	private String vehicleNumber;
	private String vehicleModelName;
	private String vehicleModelYear;
	private String isVehicleNewOrPreowned;

	private String cashOrChipFacility;
	private String cashAmount;
	private String chipNumber;
	private String accommodationYesOrNo;
	private String isShredOrSeparate;
	private String isExeutiveOrLabourFacility;

	private String electricityAllocationYesOrNo;
	private String electricityAllocationAmount;
	private String rentAllocationYesOrNo;
	private String rentAllocationAmount;
	private String mobileIssuedOrNot;

	private String simIssuedOrNot;
	private String flightFacilities;
	private String howMuchTime;
	private String familyTicketsAlsoProvidedOrNot;
	private String othersAccomandation;

	private String healthInsuranceCoverage;
	private String maximumAmountGiven;
	private String familyHealthInsuranceGivenOrNot;
	private String familyHealthInsuranceType;
	private String punchingMachineYesOrNo;

	private String joiningDate;
	private String referredBy;
	private String byWhom;

	@ManyToOne
	@JoinColumn(name = "employee_id")
	@JsonBackReference
	private PersonalInfo personalinfo;

	@PrePersist
	@PreUpdate
	private void validateFields() {
		validateAllowance("House Rent", houseRentAllowance, houseRentAmount);
		validateAllowance("Food", foodAllowance, foodAllowanceAmount);
		validateAllowance("Vehicle", vehicleAllowance, vehicleAllowanceAmount);
		validateAllowance("Uniform", uniformAllowance, uniformAllowanceAmount);
		validateAllowance("Travelling", travellingAllowances, travellingAllowancesAmount);
		validateAllowance("Educational", educationalAllowance, educationalAllowanceAmount);
		validateAllowance("Others", otherAllowance, otherAllowanceAmount);
	}

	private void validateAllowance(String allowanceName, String allowanceFlag, String allowanceAmount) {
		if ("true".equalsIgnoreCase(allowanceFlag) && (allowanceAmount == null || allowanceAmount.trim().isEmpty())) {
			throw new IllegalStateException(
					allowanceName + " Amount cannot be null or empty when " + allowanceName + " Allowance is true");
		}
	}
}
