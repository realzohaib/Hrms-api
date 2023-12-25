package com.erp.hrms.entity.helper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonalInfoDTO {

	private Long departmentId;
	private String departmentName;
	private Long employeeId;
	private String namePrefi;
	private String firstName;
	private String middleName;
	private String lastName;
	private String dateOfBirth;
	private String phoneCode;
	private String personalContactNo;
	private String email;
	private String jobPostDesignation;
	private String jobLevel;
	private String postedLocation;

}
