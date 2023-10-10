package com.erp.hrms.api.dao;

import java.util.List;

import com.erp.hrms.entity.PersonalInfo;

public interface IPersonalInfoDAO {

	public boolean existsByEmail(String email);

	public boolean existByID(long Id);

	public void savePersonalInfo(PersonalInfo personalInfo);

	public List<PersonalInfo> findAllPersonalInfo();

	public PersonalInfo getPersonalInfoByEmail(String email);

	public PersonalInfo getPersonalInfoByEmailForUpdate(String email);

	public PersonalInfo getPersonalInfoByEmployeeId(Long employeeId);

	public PersonalInfo deletePersonalInfoByEmail(String email, PersonalInfo existingPersonalInfo);

	public PersonalInfo updatePersonalInfo(String email, PersonalInfo personalInfo);

	public void updateFirstVisaEmail(String email);

	public void updateSecondVisaEmail(String email);

	public void updatefirstContinuouslyVisaEmailSend(String email);

	public void updateSecondContinuouslyVisaEmailSend(String email);

	public void updateThirdContinuouslyVisaEmailSend(String email);

	public void updatefourContinuouslyVisaEmailSend(String email);

	public PersonalInfo updateVisaDetails(Long employeeId, String visaIssueDate, String visaExpiryDate);
}
