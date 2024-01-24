package com.erp.hrms.entity.response;

import com.erp.hrms.entity.PersonalInfo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeResponseDTO {

	private Long employeeId;
	private String namePrefix;
	private String firstName;
	private String middleName;
	private String lastName;
	private String phoneCode;
	private String personalContactNo;
	private String email;
	private String postedLocation;

	public EmployeeResponseDTO() {
	}

	public EmployeeResponseDTO(PersonalInfo personalInfo) {
		this.employeeId = personalInfo.getEmployeeId();
		this.namePrefix = personalInfo.getNamePrefix();
		this.firstName = personalInfo.getFirstName();
		this.middleName = personalInfo.getMiddleName();
		this.lastName = personalInfo.getLastName();
		this.phoneCode = personalInfo.getPhoneCode();
		this.personalContactNo = personalInfo.getPersonalContactNo();
		this.email = personalInfo.getEmail();

		if (personalInfo.getJobDetails() != null && !personalInfo.getJobDetails().isEmpty()) {
			this.postedLocation = personalInfo.getJobDetails().get(0).getPostedLocation();
		}
	}
}
