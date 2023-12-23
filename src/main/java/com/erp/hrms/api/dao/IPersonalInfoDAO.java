package com.erp.hrms.api.dao;

import java.util.List;

import com.erp.hrms.entity.PersonalInfo;

import com.erp.hrms.entity.notificationhelper.NotificationHelper;

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

	public PersonalInfo updateVisaDetails(Long employeeId, String visaIssueDate, String visaExpiryDate);
	
	public void update20and60daysBeforeVisaEmail(String email);

	public void update10and30daysBeforeVisaEmail(String email);

	public void update4daysBeforeVisaEmailSend(String email);

	public void update3daysBeforeVisaEmailSend(String email);

	public void update2daysBeforeVisaEmailSend(String email);

	public void update1dayBeforeVisaEmailSend(String email);
	
	public List<NotificationHelper> getNotificationFields();
}
