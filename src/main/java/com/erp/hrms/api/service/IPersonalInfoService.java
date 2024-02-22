package com.erp.hrms.api.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.erp.hrms.entity.PersonalInfo;
import com.erp.hrms.entity.notificationhelper.NotificationHelper;
import com.erp.hrms.entity.response.EmployeeResponseDTO;
import com.erp.hrms.exception.PersonalInfoNotFoundException;

public interface IPersonalInfoService {


	public void savedata(String personalinfo, String CurrentDesignationandAdditionalTask,String url,
			MultipartFile passportSizePhoto, MultipartFile OtherIdProofDoc, MultipartFile passportScan,
			MultipartFile licensecopy, MultipartFile relativeid, MultipartFile raddressproof,
			MultipartFile secondaryDocumentScan, MultipartFile seniorSecondaryDocumentScan,
			MultipartFile graduationDocumentScan, MultipartFile postGraduationDocumentScan,
			MultipartFile[] othersDocumentScan, MultipartFile[] degreeScan, MultipartFile payslipScan,
			MultipartFile recordsheet, MultipartFile PaidTrainingDocumentProof,
			MultipartFile CertificateUploadedForOutsource, MultipartFile visaDocs, MultipartFile diplomaDocumentScan,
			MultipartFile declarationRequired, MultipartFile[] achievementsRewardsDocs) throws IOException;

	List<PersonalInfo> findAllPersonalInfo() throws IOException;

	PersonalInfo getPersonalInfoByEmail(String email) throws IOException;

	public PersonalInfo getPersonalInfoByEmployeeId(Long employeeId) throws IOException;

	public PersonalInfo deletePersonalInfoByEmail(String email, String PersonalInfo)
			throws PersonalInfoNotFoundException;

	public PersonalInfo updatePersonalInfo(String email, String personalinfo, MultipartFile passportSizePhoto,
			MultipartFile OtherIdProofDoc, MultipartFile passportScan, MultipartFile licensecopy,
			MultipartFile relativeid, MultipartFile raddressproof, MultipartFile secondaryDocumentScan,
			MultipartFile seniorSecondaryDocumentScan, MultipartFile graduationDocumentScan,
			MultipartFile postGraduationDocumentScan, MultipartFile[] othersDocumentScan, MultipartFile[] degreeScan,
			MultipartFile payslipScan, MultipartFile recordsheet, MultipartFile PaidTrainingDocumentProof,
			MultipartFile CertificateUploadedForOutsource, MultipartFile visaDocs, MultipartFile diplomaDocumentScan,
			MultipartFile declarationRequired, MultipartFile[] achievementsRewardsDocs) throws IOException;

	public PersonalInfo updateVisaDetails(Long employeeId, String visaIssueDate, String visaExpiryDate);

	public List<NotificationHelper> getRequestedField();

	public List<PersonalInfo> getPersonalInfoWithPendingBackgroundCheck();

	public List<EmployeeResponseDTO> getByPostedLocation(String postedLocation);

	public List<PersonalInfo> findAllPersonalInfoActive();

}
