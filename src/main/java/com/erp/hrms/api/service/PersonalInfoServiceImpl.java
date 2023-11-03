package com.erp.hrms.api.service;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.erp.hrms.api.dao.DepartmentRepository;
import com.erp.hrms.api.dao.IPersonalInfoDAO;
import com.erp.hrms.api.request.SignupRequest;
import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.entity.BackgroundCheck;
import com.erp.hrms.entity.BloodRelative;
import com.erp.hrms.entity.Department;
import com.erp.hrms.entity.DrivingLicense;
import com.erp.hrms.entity.Education;
import com.erp.hrms.entity.EmpAchievement;
import com.erp.hrms.entity.JobDetails;
import com.erp.hrms.entity.OthersQualification;
import com.erp.hrms.entity.PassportDetails;
import com.erp.hrms.entity.PersonalInfo;
import com.erp.hrms.entity.PreviousEmployee;
import com.erp.hrms.entity.ProfessionalQualification;
import com.erp.hrms.entity.Trainingdetails;
import com.erp.hrms.entity.VisaDetail;
import com.erp.hrms.entity.notificationhelper.NotificationHelper;
import com.erp.hrms.exception.PersonalEmailExistsException;
import com.erp.hrms.exception.PersonalInfoNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PersonalInfoServiceImpl implements IPersonalInfoService {
	
	public static int generateRandom4DigitNumber() {
        Random random = new Random();
        return 1000 + random.nextInt(9000); // Generates a random 4-digit number
    }

    public static long concatenateIdWithRandomNumber(long id, int randomPart) {
        return (id * 10000L) + randomPart;
    }
	@Autowired
	private IPersonalInfoDAO dao;

	@Autowired
	private DepartmentRepository departmentRepository;
	
	@Autowired
	private JavaMailSender sender;

	@Override
	public void savedata(String personalinfo, MultipartFile passportSizePhoto, MultipartFile OtherIdProofDoc,
			MultipartFile passportScan, MultipartFile licensecopy, MultipartFile relativeid,
			MultipartFile raddressproof, MultipartFile secondaryDocumentScan, MultipartFile seniorSecondaryDocumentScan,
			MultipartFile graduationDocumentScan, MultipartFile postGraduationDocumentScan,
			MultipartFile[] othersDocumentScan, MultipartFile[] degreeScan, MultipartFile payslipScan,
			MultipartFile recordsheet, MultipartFile PaidTrainingDocumentProof,
			MultipartFile CertificateUploadedForOutsource, MultipartFile visaDocs, MultipartFile diplomaDocumentScan,
			MultipartFile declarationRequired, MultipartFile[] achievementsRewardsDocs) throws IOException {

		ObjectMapper mapper = new ObjectMapper();
		PersonalInfo PersonalInfo = mapper.readValue(personalinfo, PersonalInfo.class);

		String email = PersonalInfo.getEmail();
		if (dao.existsByEmail(email)) {
			throw new PersonalEmailExistsException(new MessageResponse("Email ID already exists"));
		}
		long employeeId;
		try {
			Long departmentId = PersonalInfo.getDepartment().getDepartmentId();
			
			// Generate a 4-digit random number
	        int randomPart = generateRandom4DigitNumber();
	      
	        employeeId = concatenateIdWithRandomNumber(departmentId, randomPart);
	        if(!dao.existByID(employeeId)) {
	        	  employeeId = employeeId*10;
	        }
	        
	        PersonalInfo.setEmployeeId(employeeId);
			
			PersonalInfo.setFathersFirstName("Mr " + PersonalInfo.getFathersFirstName());
			PersonalInfo.setStatus("Active");
			PersonalInfo.setEmpStatus("New employee");
			if (passportSizePhoto != null && !passportSizePhoto.isEmpty()) {
				PersonalInfo.setPassportSizePhoto(passportSizePhoto.getBytes());
			}
			if (OtherIdProofDoc != null && !OtherIdProofDoc.isEmpty()) {
				PersonalInfo.setOtherIdProofDoc(OtherIdProofDoc.getBytes());
			}

			PassportDetails passportDetails = new PassportDetails();
			if (passportScan != null && !passportScan.isEmpty()) {
				passportDetails.setPassportScan(passportScan.getBytes());
			}
			if (PersonalInfo.getPsDetail() != null) {
				passportDetails.setPassportIssuingCountry(PersonalInfo.getPsDetail().getPassportIssuingCountry());
				passportDetails.setPassportExpiryDate(PersonalInfo.getPsDetail().getPassportExpiryDate());
				passportDetails.setPassportNumber(PersonalInfo.getPsDetail().getPassportNumber());
			}
			PersonalInfo.setPsDetail(passportDetails);
			VisaDetail visaDetail = new VisaDetail();
			if (PersonalInfo.getVisainfo() != null) {
				visaDetail.setWorkVisaEmirateId(PersonalInfo.getVisainfo().getWorkVisaEmirateId());
				visaDetail.setCategoryOfVisa(PersonalInfo.getVisainfo().getCategoryOfVisa());
				visaDetail.setSiGlobalWorkVisaCompany(PersonalInfo.getVisainfo().getSiGlobalWorkVisaCompany());
				visaDetail.setVisaIssueyDate(PersonalInfo.getVisainfo().getVisaIssueyDate());
				visaDetail.setVisaType(PersonalInfo.getVisainfo().getVisaType());
				
				visaDetail.setVisaEmailSend20and60daysBefore(false);
				visaDetail.setVisaEmailSend10and30daysBefore(false);
				visaDetail.setVisaEmailSend4daysBefore(false);
				visaDetail.setVisaEmailSend3daysBefore(false);
				visaDetail.setVisaEmailSend2daysBefore(false);
				visaDetail.setVisaEmailSend1dayBefore(false);

				visaDetail.setVisaEmailSend20and60daysBefore(false);
				visaDetail.setVisaEmailSend10and30daysBefore(false);
				visaDetail.setVisaEmailSend4daysBefore(false);
				visaDetail.setVisaEmailSend3daysBefore(false);
				visaDetail.setVisaEmailSend2daysBefore(false);
				visaDetail.setVisaEmailSend1dayBefore(false);

				String visaExpiryDateString = PersonalInfo.getVisainfo().getVisaExpiryDate();
				visaDetail.setVisaExpiryDate(visaExpiryDateString);
				if (visaDocs != null && !visaDocs.isEmpty()) {
					visaDetail.setVisaDocs(visaDocs.getBytes());
				}
			}
			PersonalInfo.setVisainfo(visaDetail);
			DrivingLicense drivinglicense = new DrivingLicense();
			if (licensecopy != null && !licensecopy.isEmpty()) {
				drivinglicense.setLicensecopy(licensecopy.getBytes());
			}
			if (PersonalInfo.getLicense() != null) {
				drivinglicense.setDrivinglicense(PersonalInfo.getLicense().getDrivinglicense());
				drivinglicense.setOwnvehicle(PersonalInfo.getLicense().getOwnvehicle());
				drivinglicense.setLicenseType(PersonalInfo.getLicense().getLicenseType());
			}
			PersonalInfo.setLicense(drivinglicense);
			BloodRelative relative = new BloodRelative();
			if (relativeid != null && !relativeid.isEmpty()) {
				relative.setRelativeid(relativeid.getBytes());
			}
			if (raddressproof != null && !raddressproof.isEmpty()) {
				relative.setRaddressproof(raddressproof.getBytes());
			}
			if (PersonalInfo.getRelative() != null) {
				relative.setRelativenamePrefix(PersonalInfo.getRelative().getRelativenamePrefix());
				relative.setRaddress(PersonalInfo.getRelative().getRaddress());
				relative.setRphoneCode(PersonalInfo.getRelative().getRphoneCode());
				relative.setRcontactno(PersonalInfo.getRelative().getRcontactno());
				relative.setRFirstname(PersonalInfo.getRelative().getRFirstname());
				relative.setRmiddlename(PersonalInfo.getRelative().getRmiddlename());
				relative.setRlastname(PersonalInfo.getRelative().getRlastname());
				relative.setRelationship(PersonalInfo.getRelative().getRelationship());
			}
			PersonalInfo.setRelative(relative);
			List<Education> educations = PersonalInfo.getEducations();
			if (educations != null) {
				for (Education edc : educations) {
					if (secondaryDocumentScan != null && !secondaryDocumentScan.isEmpty()) {
						edc.setSecondaryDocumentScan(secondaryDocumentScan.getBytes());
					}
					if (seniorSecondaryDocumentScan != null && !seniorSecondaryDocumentScan.isEmpty()) {
						edc.setSeniorSecondaryDocumentScan(seniorSecondaryDocumentScan.getBytes());
					}
					if (graduationDocumentScan != null && !graduationDocumentScan.isEmpty()) {
						edc.setGraduationDocumentScan(graduationDocumentScan.getBytes());
					}
					if (postGraduationDocumentScan != null && !postGraduationDocumentScan.isEmpty()) {
						edc.setPostGraduationDocumentScan(postGraduationDocumentScan.getBytes());
					}
					if (diplomaDocumentScan != null && !diplomaDocumentScan.isEmpty()) {
						edc.setDiplomaDocumentScan(diplomaDocumentScan.getBytes());
					}
					edc.setPersonalinfo(PersonalInfo);
				}
			}
			PersonalInfo.setEducations(educations);
			List<OthersQualification> othersQualification = PersonalInfo.getOthersQualifications();
			if (othersQualification != null) {
				for (int i = 0; i < othersQualification.size(); i++) {
					OthersQualification newOthersQualification = othersQualification.get(i);
					if (othersDocumentScan != null && i < othersDocumentScan.length) {
						MultipartFile file = othersDocumentScan[i];
						if (file != null && !file.isEmpty()) {
							newOthersQualification.setOthersDocumentScan(file.getBytes());
						}
					}
					newOthersQualification.setPersonalinfo(PersonalInfo);
				}
			}
			PersonalInfo.setOthersQualifications(othersQualification);
			List<ProfessionalQualification> qualifications = PersonalInfo.getProfessionalQualifications();
			if (qualifications != null) {
				for (int i = 0; i < qualifications.size(); i++) {
					ProfessionalQualification professionalQualification = qualifications.get(i);
					if (degreeScan != null && i < degreeScan.length) {
						MultipartFile file = degreeScan[i];
						if (file != null && !file.isEmpty()) {
							professionalQualification.setDegreeScan(file.getBytes());
						}
					}
					professionalQualification.setPersonalinfo(PersonalInfo);
				}
			}
			PersonalInfo.setProfessionalQualifications(qualifications);
			List<PreviousEmployee> oldEmployee = PersonalInfo.getOldEmployee();
			if (oldEmployee != null) {
				for (PreviousEmployee oldemp : oldEmployee) {
					if (payslipScan != null && !payslipScan.isEmpty()) {
						oldemp.setPayslipScan(payslipScan.getBytes());
					}
					oldemp.setPersonalinfo(PersonalInfo);
					// Retrieve the empAchievements list from the oldemp entity
					List<EmpAchievement> empAchievements = oldemp.getEmpAchievements();
					if (empAchievements != null) {
						for (int i = 0; i < empAchievements.size(); i++) {
							EmpAchievement achievement = empAchievements.get(i);
							if (achievementsRewardsDocs != null && i < achievementsRewardsDocs.length) {
								MultipartFile file = achievementsRewardsDocs[i];
								if (file != null && !file.isEmpty()) {
									achievement.setAchievementsRewardsDocs(file.getBytes());
								}
							}
							achievement.setPreviousEmployee(oldemp);
						}
					}
					oldemp.setEmpAchievements(empAchievements);
				}
			}
			PersonalInfo.setOldEmployee(oldEmployee);
			BackgroundCheck bgcheck = PersonalInfo.getBgcheck();
			if (bgcheck != null) {
				if (recordsheet != null && !recordsheet.isEmpty()) {
					bgcheck.setRecordsheet(recordsheet.getBytes());
				}
				if (declarationRequired != null && !declarationRequired.isEmpty()) {
					bgcheck.setDeclarationRequired(declarationRequired.getBytes());
				}
				bgcheck.setPersonalinfo(PersonalInfo);
			}
			PersonalInfo.setBgcheck(bgcheck);
			List<Trainingdetails> training = PersonalInfo.getTraining();
			if (training != null) {
				for (Trainingdetails train : training) {
					if (CertificateUploadedForOutsource != null && !CertificateUploadedForOutsource.isEmpty()) {
						train.setCertificateUploadedForOutsource(CertificateUploadedForOutsource.getBytes());
					}
					if (PaidTrainingDocumentProof != null && !PaidTrainingDocumentProof.isEmpty()) {
						train.setPaidTrainingDocumentProof(PaidTrainingDocumentProof.getBytes());
					}
					train.setPersonalinfo(PersonalInfo);
				}
				PersonalInfo.setTraining(training);
			}
			dao.savePersonalInfo(PersonalInfo);
		
			
			//sendOnboardingEmail(PersonalInfo.getEmail(), employeeId, PersonalInfo.getFirstName());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public List<PersonalInfo> findAllPersonalInfo() {
		List<PersonalInfo> findAllPersonalInfo = null;
		try {
			findAllPersonalInfo = dao.findAllPersonalInfo();
			return findAllPersonalInfo;
		} catch (Exception e) {
			throw new RuntimeException("Something wrong: " + e);
		}
	}

	@Override
	public PersonalInfo getPersonalInfoByEmail(String email) {
		try {
			PersonalInfo personalInfoByEmail = dao.getPersonalInfoByEmail(email);
			return personalInfoByEmail;
		} catch (Exception e) {
			throw new RuntimeException("No personal information found for this email ID: " + email, e);
		}
	}

//	@Override
//	public PersonalInfo getPersonalInfoByEmployeeId(Long employeeId) {
//		try {
//
//			PersonalInfo personalInfoByEmployeeId = dao.getPersonalInfoByEmployeeId(employeeId);
//			return personalInfoByEmployeeId;
//		} catch (Exception e) {
////			System.out.println(e);
//			throw new RuntimeException("No personal information found for this employee ID: " + employeeId, e);
//		}
//	}
	
	@Override
	public PersonalInfo getPersonalInfoByEmployeeId(Long employeeId) {
			PersonalInfo personalInfoByEmployeeId = dao.getPersonalInfoByEmployeeId(employeeId);
			return personalInfoByEmployeeId;
		
	}

	@Override
	public PersonalInfo deletePersonalInfoByEmail(String email, String personalInfoJson)
			throws PersonalInfoNotFoundException {
		ObjectMapper mapper = new ObjectMapper();
		PersonalInfo updatedPersonalInfo = null;
		try {
			PersonalInfo inputPersonalInfo = mapper.readValue(personalInfoJson, PersonalInfo.class);
			PersonalInfo existingPersonalInfo = dao.getPersonalInfoByEmailForUpdate(email);

			if (existingPersonalInfo == null) {
				throw new PersonalInfoNotFoundException(
						new MessageResponse("No personal information found for this email ID: " + email));
			}
			existingPersonalInfo.setStatus(inputPersonalInfo.getStatus());
			dao.deletePersonalInfoByEmail(email, existingPersonalInfo);

		} catch (Exception e) {
			throw new RuntimeException(
					"No personal information found for this email ID: " + email + " or this eamil is inactived", e);
		}
		return updatedPersonalInfo;
	}

	@Override
	public PersonalInfo updatePersonalInfo(String email, String PersonalInfo, MultipartFile passportSizePhoto,
			MultipartFile OtherIdProofDoc, MultipartFile passportScan, MultipartFile licensecopy,
			MultipartFile relativeid, MultipartFile raddressproof, MultipartFile secondaryDocumentScan,
			MultipartFile seniorSecondaryDocumentScan, MultipartFile graduationDocumentScan,
			MultipartFile postGraduationDocumentScan, MultipartFile[] othersDocumentScan, MultipartFile[] degreeScan,
			MultipartFile payslipScan, MultipartFile recordsheet, MultipartFile PaidTrainingDocumentProof,
			MultipartFile CertificateUploadedForOutsource, MultipartFile visaDocs, MultipartFile diplomaDocumentScan,
			MultipartFile declarationRequired, MultipartFile[] achievementsRewardsDocs) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		PersonalInfo personalInfo = mapper.readValue(PersonalInfo, PersonalInfo.class);

		PersonalInfo existingPersonalInfo = dao.getPersonalInfoByEmailForUpdate(email);
		if (existingPersonalInfo == null) {

			throw new PersonalInfoNotFoundException(new MessageResponse(
					"No personal information found for this email ID: " + email + " or this eamil is inactived"));
		}
		try {
			existingPersonalInfo.setNamePrefix(personalInfo.getNamePrefix());
			existingPersonalInfo.setFirstName(personalInfo.getFirstName());
			existingPersonalInfo.setMiddleName(personalInfo.getMiddleName());
			existingPersonalInfo.setLastName(personalInfo.getLastName());
			existingPersonalInfo.setFathersFirstName(personalInfo.getFathersFirstName());
			existingPersonalInfo.setFathersMiddleName(personalInfo.getFathersMiddleName());
			existingPersonalInfo.setFathersLastName(personalInfo.getFathersLastName());
			existingPersonalInfo.setDateOfBirth(personalInfo.getDateOfBirth());
			existingPersonalInfo.setAge(personalInfo.getAge());
			existingPersonalInfo.setMaritalStatus(personalInfo.getMaritalStatus());
			if (passportSizePhoto != null && !passportSizePhoto.isEmpty()) {
				existingPersonalInfo.setPassportSizePhoto(passportSizePhoto.getBytes());
			}
			existingPersonalInfo.setPersonalContactNo(personalInfo.getPersonalContactNo());
			existingPersonalInfo.setCitizenship(personalInfo.getCitizenship());
			if (OtherIdProofDoc != null && !OtherIdProofDoc.isEmpty()) {
				existingPersonalInfo.setOtherIdProofDoc(OtherIdProofDoc.getBytes());
			}
			existingPersonalInfo.setPermanentResidenceCountry(personalInfo.getPermanentResidenceCountry());
			existingPersonalInfo.setPermanentResidentialAddress(personalInfo.getPermanentResidentialAddress());
			existingPersonalInfo.setBloodGroup(personalInfo.getBloodGroup());
			existingPersonalInfo.setWorkedInUAE(personalInfo.getWorkedInUAE());
			existingPersonalInfo.setEmiratesID(personalInfo.getEmiratesID());
			existingPersonalInfo.setDegreeAttestation(personalInfo.getDegreeAttestation());
			existingPersonalInfo.setHobbies(personalInfo.getHobbies());
			existingPersonalInfo.setStatus("Active");
			existingPersonalInfo.setEmpStatus(personalInfo.getEmpStatus());

			PassportDetails passportDetails = existingPersonalInfo.getPsDetail();
			if (personalInfo.getPsDetail() != null) {
				passportDetails.setPassportNumber(personalInfo.getPsDetail().getPassportNumber());
				passportDetails.setPassportIssuingCountry(personalInfo.getPsDetail().getPassportIssuingCountry());
				passportDetails.setPassportExpiryDate(personalInfo.getPsDetail().getPassportExpiryDate());
				if (passportScan != null && !passportScan.isEmpty()) {
					passportDetails.setPassportScan(passportScan.getBytes());
				}
			}
			existingPersonalInfo.setPsDetail(passportDetails);

			DrivingLicense drivingLicense = existingPersonalInfo.getLicense();
			if (personalInfo.getLicense() != null) {
				drivingLicense.setDrivinglicense(personalInfo.getLicense().getDrivinglicense());
				drivingLicense.setOwnvehicle(personalInfo.getLicense().getOwnvehicle());
				if (licensecopy != null && !licensecopy.isEmpty()) {
					drivingLicense.setLicensecopy(licensecopy.getBytes());
				}
			}
			existingPersonalInfo.setLicense(drivingLicense);

			BloodRelative bloodRelative = existingPersonalInfo.getRelative();
			if (personalInfo.getRelative() != null) {
				bloodRelative.setRFirstname(personalInfo.getRelative().getRFirstname());
				bloodRelative.setRmiddlename(personalInfo.getRelative().getRmiddlename());
				bloodRelative.setRlastname(personalInfo.getRelative().getRlastname());
				bloodRelative.setRaddress(personalInfo.getRelative().getRaddress());
				bloodRelative.setRcontactno(personalInfo.getRelative().getRcontactno());
				bloodRelative.setRelationship(personalInfo.getRelative().getRelationship());
				if (relativeid != null && !relativeid.isEmpty()) {
					bloodRelative.setRelativeid(relativeid.getBytes());
				}
				if (raddressproof != null && !raddressproof.isEmpty()) {
					bloodRelative.setRaddressproof(raddressproof.getBytes());
				}
			}
			existingPersonalInfo.setRelative(bloodRelative);

			VisaDetail visaDetail = existingPersonalInfo.getVisainfo();
			if (personalInfo.getVisainfo() != null) {
				visaDetail.setWorkVisaEmirateId(personalInfo.getVisainfo().getWorkVisaEmirateId());
				visaDetail.setCategoryOfVisa(personalInfo.getVisainfo().getCategoryOfVisa());
				visaDetail.setVisaType(personalInfo.getVisainfo().getVisaType());
				visaDetail.setSiGlobalWorkVisaCompany(personalInfo.getVisainfo().getSiGlobalWorkVisaCompany());
				visaDetail.setVisaIssueyDate(personalInfo.getVisainfo().getVisaIssueyDate());
				visaDetail.setVisaExpiryDate(personalInfo.getVisainfo().getVisaExpiryDate());

				if (visaDocs != null && !visaDocs.isEmpty()) {
					visaDetail.setVisaDocs(visaDocs.getBytes());
				}
			}
			existingPersonalInfo.setVisainfo(visaDetail);

			List<Education> educations = existingPersonalInfo.getEducations();
			if (personalInfo.getEducations() != null) {
				List<Education> newEducations = personalInfo.getEducations();
				if (educations.size() != newEducations.size()) {
					return existingPersonalInfo;
				}
				for (int i = 0; i < educations.size(); i++) {
					Education existingEducation = educations.get(i);
					Education newEducation = newEducations.get(i);

					existingEducation.setSecondaryIssuingAuthority(newEducation.getSecondaryIssuingAuthority());
					existingEducation.setSecondarymarksOrGrade(newEducation.getSecondarymarksOrGrade());
					existingEducation.setSecondaryyear(newEducation.getSecondaryyear());
					existingEducation
							.setSeniorSecondaryIssuingAuthority(newEducation.getSeniorSecondaryIssuingAuthority());
					existingEducation.setSeniorSecondaryMarksOrGrade(newEducation.getSeniorSecondaryMarksOrGrade());
					existingEducation.setSeniorSecondaryYear(newEducation.getSeniorSecondaryYear());
					existingEducation.setGraduationIssuingAuthority(newEducation.getGraduationIssuingAuthority());
					existingEducation.setGraduationMarksOrGrade(newEducation.getGraduationMarksOrGrade());
					existingEducation.setGraduationYear(newEducation.getGraduationYear());
					existingEducation
							.setPostGraduationIssuingAuthority(newEducation.getPostGraduationIssuingAuthority());
					existingEducation.setPostGraduationMarksOrGrade(newEducation.getPostGraduationMarksOrGrade());
					existingEducation.setPostGraduationYear(newEducation.getPostGraduationYear());
					existingEducation.setDiplomaIssuingAuthority(newEducation.getDiplomaIssuingAuthority());
					existingEducation.setDiplomaMarksOrGrade(newEducation.getDiplomaMarksOrGrade());
					existingEducation.setDiplomaYear(newEducation.getDiplomaYear());
					if (secondaryDocumentScan != null && !secondaryDocumentScan.isEmpty()) {
						existingEducation.setSecondaryDocumentScan(secondaryDocumentScan.getBytes());
					}
					if (seniorSecondaryDocumentScan != null && !seniorSecondaryDocumentScan.isEmpty()) {
						existingEducation.setSeniorSecondaryDocumentScan(seniorSecondaryDocumentScan.getBytes());
					}
					if (postGraduationDocumentScan != null && !postGraduationDocumentScan.isEmpty()) {
						existingEducation.setPostGraduationDocumentScan(postGraduationDocumentScan.getBytes());
					}
					if (graduationDocumentScan != null && !graduationDocumentScan.isEmpty()) {
						existingEducation.setGraduationDocumentScan(graduationDocumentScan.getBytes());
					}
					if (diplomaDocumentScan != null && !diplomaDocumentScan.isEmpty()) {
						existingEducation.setDiplomaDocumentScan(diplomaDocumentScan.getBytes());
					}
				}
			}
			existingPersonalInfo.setEducations(educations);

			List<OthersQualification> othersQualifications = existingPersonalInfo.getOthersQualifications();
			if (personalInfo.getOthersQualifications() != null) {
				List<OthersQualification> newOthersQualification = personalInfo.getOthersQualifications();
				if (othersQualifications.size() != newOthersQualification.size()) {
					return existingPersonalInfo;
				}
				for (int i = 0; i < othersQualifications.size(); i++) {
					OthersQualification existingOthersQualification = othersQualifications.get(i);
					OthersQualification updateOthersQualification = newOthersQualification.get(i);
					existingOthersQualification.setOthers(updateOthersQualification.getOthers());
					existingOthersQualification
							.setOthersIssuingAuthority(updateOthersQualification.getOthersIssuingAuthority());
					existingOthersQualification
							.setOthersMarksOrGrade(updateOthersQualification.getOthersMarksOrGrade());
					existingOthersQualification.setOthersYear(updateOthersQualification.getOthersYear());
					if (othersDocumentScan != null && i < othersDocumentScan.length) {
						MultipartFile file = othersDocumentScan[i];
						if (file != null && !file.isEmpty()) {
							existingOthersQualification.setOthersDocumentScan(file.getBytes());
						}
					}
				}
			}
			existingPersonalInfo.setOthersQualifications(othersQualifications);

			List<ProfessionalQualification> professionalQualifications = existingPersonalInfo
					.getProfessionalQualifications();
			if (personalInfo.getProfessionalQualifications() != null) {
				List<ProfessionalQualification> newProfessionalQualifications = personalInfo
						.getProfessionalQualifications();
				if (professionalQualifications.size() != newProfessionalQualifications.size()) {
					return existingPersonalInfo;
				}
				for (int i = 0; i < professionalQualifications.size(); i++) {
					ProfessionalQualification existingProfessionalQualification = professionalQualifications.get(i);
					ProfessionalQualification newProfessionalQualification = newProfessionalQualifications.get(i);
					existingProfessionalQualification.setGrade(newProfessionalQualification.getGrade());
					existingProfessionalQualification
							.setIssuingAuthority(newProfessionalQualification.getIssuingAuthority());
					existingProfessionalQualification.setGradingSystem(newProfessionalQualification.getGradingSystem());
					existingProfessionalQualification.setQualification(newProfessionalQualification.getQualification());
					existingProfessionalQualification
							.setYearOfQualification(newProfessionalQualification.getYearOfQualification());
					if (degreeScan != null && i < degreeScan.length) {
						MultipartFile file = degreeScan[i];
						if (file != null && !file.isEmpty()) {
							existingProfessionalQualification.setDegreeScan(file.getBytes());
						}
					}
				}
			}
			existingPersonalInfo.setProfessionalQualifications(professionalQualifications);

			List<PreviousEmployee> previousEmployees = existingPersonalInfo.getOldEmployee();
			if (personalInfo.getOldEmployee() != null) {
				List<PreviousEmployee> newPreviousEmployees = personalInfo.getOldEmployee();
				if (previousEmployees.size() != newPreviousEmployees.size()) {
					return existingPersonalInfo;
				}
				for (int i = 0; i < previousEmployees.size(); i++) {
					PreviousEmployee existingPreviousEmployee = previousEmployees.get(i);
					PreviousEmployee newPreviousEmployee = newPreviousEmployees.get(i);
					existingPreviousEmployee.setCompanyName(newPreviousEmployee.getCompanyName());
					existingPreviousEmployee.setCompanyAddress(newPreviousEmployee.getCompanyAddress());
					existingPreviousEmployee.setDesignation(newPreviousEmployee.getDesignation());
					existingPreviousEmployee.setDescription(newPreviousEmployee.getDescription());
					existingPreviousEmployee.setDateFrom(newPreviousEmployee.getDateFrom());
					existingPreviousEmployee.setDateTo(newPreviousEmployee.getDateTo());
					if (payslipScan != null && !payslipScan.isEmpty()) {
						existingPreviousEmployee.setPayslipScan(payslipScan.getBytes());
					}
					existingPreviousEmployee.setPreviousHRContact(newPreviousEmployee.getPreviousHRContact());
					existingPreviousEmployee.setPreviousHRName(newPreviousEmployee.getPreviousHRName());
					existingPreviousEmployee.setPreviousManagerContact(newPreviousEmployee.getPreviousManagerContact());
					existingPreviousEmployee.setPreviousManagerName(newPreviousEmployee.getPreviousManagerName());
					existingPreviousEmployee.setLastWithdrawnSalary(newPreviousEmployee.getLastWithdrawnSalary());

					List<EmpAchievement> empAchievements = existingPreviousEmployee.getEmpAchievements();
					if (newPreviousEmployee.getEmpAchievements() != null) {
						List<EmpAchievement> newEmpAchievements = newPreviousEmployee.getEmpAchievements();
						if (empAchievements.size() != newEmpAchievements.size()) {
							return existingPersonalInfo;
						}
						for (int j = 0; j < empAchievements.size(); j++) {
							EmpAchievement existingAchievement = empAchievements.get(j);
							EmpAchievement newAchievement = newEmpAchievements.get(j);
							existingAchievement.setAchievementRewardsName(newAchievement.getAchievementRewardsName());
							if (achievementsRewardsDocs != null && j < achievementsRewardsDocs.length) {
								MultipartFile file = achievementsRewardsDocs[j];
								if (file != null && !file.isEmpty()) {
									existingAchievement.setAchievementsRewardsDocs(file.getBytes());
								}
							}
						}
					}
					existingPreviousEmployee.setEmpAchievements(empAchievements);
				}
			}
			existingPersonalInfo.setOldEmployee(previousEmployees);

			List<Trainingdetails> trainingdetails = existingPersonalInfo.getTraining();
			if (personalInfo.getTraining() != null) {
				List<Trainingdetails> newtrainingdetails = personalInfo.getTraining();
				if (trainingdetails.size() != newtrainingdetails.size()) {
					return existingPersonalInfo;
				}
				for (int i = 0; i < trainingdetails.size(); i++) {
					Trainingdetails existingTrainingdetail = trainingdetails.get(i);
					Trainingdetails newtrainingdetail = newtrainingdetails.get(i);
					existingTrainingdetail.setInHouseOutsource(newtrainingdetail.getInHouseOutsource());
					existingTrainingdetail.setPaidUnpaid(newtrainingdetail.getPaidUnpaid());
					existingTrainingdetail.setTrainingType(newtrainingdetail.getTrainingType());
					existingTrainingdetail.setTrainerName(newtrainingdetail.getTrainerName());
					existingTrainingdetail.setTrainerPost(newtrainingdetail.getTrainerPost());
					existingTrainingdetail.setTrainerPhoneCode(newtrainingdetail.getTrainerPhoneCode());
					existingTrainingdetail.setTrainerPhoneNo(newtrainingdetail.getTrainerPhoneNo());
					existingTrainingdetail.setTrainerDepartment(newtrainingdetail.getTrainerDepartment());
					existingTrainingdetail.setTrainerFeedback(newtrainingdetail.getTrainerFeedback());
					existingTrainingdetail.setTrainingStartDate(newtrainingdetail.getTrainingStartDate());
					existingTrainingdetail.setTrainingEndDate(newtrainingdetail.getTrainingEndDate());
					if (CertificateUploadedForOutsource != null && !CertificateUploadedForOutsource.isEmpty()) {
						existingTrainingdetail
								.setCertificateUploadedForOutsource(CertificateUploadedForOutsource.getBytes());
					}
					if (PaidTrainingDocumentProof != null && !PaidTrainingDocumentProof.isEmpty()) {
						existingTrainingdetail.setPaidTrainingDocumentProof(PaidTrainingDocumentProof.getBytes());
					}
				}
			}
			existingPersonalInfo.setTraining(trainingdetails);

			List<JobDetails> jobDetails = existingPersonalInfo.getJobDetails();
			if (personalInfo.getJobDetails() != null) {
				List<JobDetails> newJobDetails = personalInfo.getJobDetails();
				if (jobDetails.size() != newJobDetails.size()) {
					return existingPersonalInfo;
				}
				for (int i = 0; i < jobDetails.size(); i++) {
					JobDetails existingJobDetails = jobDetails.get(i);
					JobDetails updateJobDetails = newJobDetails.get(i);
					existingJobDetails.setJobPostDesignation(updateJobDetails.getJobPostDesignation());
					existingJobDetails.setCompanyEmailIdProvided(updateJobDetails.getCompanyEmailIdProvided());
					existingJobDetails.setCompanyEmailId(updateJobDetails.getCompanyEmailId());
					existingJobDetails.setJobLevel(updateJobDetails.getJobLevel());
					existingJobDetails.setPostedLocation(updateJobDetails.getPostedLocation());
					existingJobDetails.setBasicAllowance(updateJobDetails.getBasicAllowance());
					existingJobDetails.setHouseRentAllowance(updateJobDetails.getHouseRentAllowance());
					existingJobDetails.setHouseRentAmount(updateJobDetails.getHouseRentAmount());
					existingJobDetails.setFoodAllowance(updateJobDetails.getFoodAllowance());
					existingJobDetails.setFoodAllowanceAmount(updateJobDetails.getFoodAllowanceAmount());
					existingJobDetails.setEducationalAllowance(updateJobDetails.getEducationalAllowance());
					existingJobDetails.setEducationalAllowanceAmount(updateJobDetails.getEducationalAllowanceAmount());
					existingJobDetails.setTravellingAllowances(updateJobDetails.getTravellingAllowances());
					existingJobDetails.setTravellingAllowancesAmount(updateJobDetails.getTravellingAllowancesAmount());
					existingJobDetails.setUniformAllowance(updateJobDetails.getUniformAllowance());
					existingJobDetails.setUniformAllowanceAmount(updateJobDetails.getUniformAllowanceAmount());
					existingJobDetails.setVehicleAllowance(updateJobDetails.getVehicleAllowance());
					existingJobDetails.setVehicleAllowanceAmount(updateJobDetails.getVehicleAllowanceAmount());
					existingJobDetails.setOtherAllowance(updateJobDetails.getOtherAllowance());
					existingJobDetails.setOtherAllowanceAmount(updateJobDetails.getOtherAllowanceAmount());
					existingJobDetails.setVehicle(updateJobDetails.getVehicle());
					existingJobDetails.setVehicleNumber(updateJobDetails.getVehicleNumber());
					existingJobDetails.setVehicleModelName(updateJobDetails.getVehicleModelName());
					existingJobDetails.setVehicleModelYear(updateJobDetails.getVehicleModelYear());
					existingJobDetails.setIsVehicleNewOrPreowned(updateJobDetails.getIsVehicleNewOrPreowned());
					existingJobDetails.setMobileIssuedOrNot(updateJobDetails.getMobileIssuedOrNot());
					existingJobDetails.setSimIssuedOrNot(updateJobDetails.getSimIssuedOrNot());
					existingJobDetails.setFlightFacilities(updateJobDetails.getFlightFacilities());
					existingJobDetails.setHowMuchTime(updateJobDetails.getHowMuchTime());
					existingJobDetails
							.setFamilyTicketsAlsoProvidedOrNot(updateJobDetails.getFamilyTicketsAlsoProvidedOrNot());
					existingJobDetails.setOthersAccomandation(updateJobDetails.getOthersAccomandation());
					existingJobDetails.setHealthInsuranceCoverage(updateJobDetails.getHealthInsuranceCoverage());
					existingJobDetails.setMaximumAmountGiven(updateJobDetails.getMaximumAmountGiven());
					existingJobDetails
							.setFamilyHealthInsuranceGivenOrNot(updateJobDetails.getFamilyHealthInsuranceGivenOrNot());
					existingJobDetails.setFamilyHealthInsuranceType(updateJobDetails.getFamilyHealthInsuranceType());
					existingJobDetails.setPunchingMachineYesOrNo(updateJobDetails.getPunchingMachineYesOrNo());
					existingJobDetails.setJoiningDate(updateJobDetails.getJoiningDate());
					existingJobDetails.setAccommodationYesOrNo(updateJobDetails.getAccommodationYesOrNo());
					existingJobDetails.setIsShredOrSeparate(updateJobDetails.getIsShredOrSeparate());
					existingJobDetails.setIsExeutiveOrLabourFacility(updateJobDetails.getIsExeutiveOrLabourFacility());
					existingJobDetails
							.setElectricityAllocationYesOrNo(updateJobDetails.getElectricityAllocationYesOrNo());
					existingJobDetails
							.setElectricityAllocationAmount(updateJobDetails.getElectricityAllocationAmount());
					existingJobDetails.setRentAllocationYesOrNo(updateJobDetails.getRentAllocationYesOrNo());
					existingJobDetails.setRentAllocationAmount(updateJobDetails.getRentAllocationAmount());
					existingJobDetails.setCashOrChipFacility(updateJobDetails.getCashOrChipFacility());
					existingJobDetails.setChipNumber(updateJobDetails.getChipNumber());
					existingJobDetails.setReferredBy(updateJobDetails.getReferredBy());
					existingJobDetails.setByWhom(updateJobDetails.getByWhom());
				}
				existingPersonalInfo.setJobDetails(jobDetails);
			}
			BackgroundCheck bgcheck = existingPersonalInfo.getBgcheck();
			if (personalInfo.getBgcheck() != null) {
				bgcheck.setStatus(personalInfo.getBgcheck().getStatus());
				bgcheck.setDoneBy(personalInfo.getBgcheck().getDoneBy());
				bgcheck.setInternalConcernedManager(personalInfo.getBgcheck().getInternalConcernedManager());
				bgcheck.setManagerApproval(personalInfo.getBgcheck().getManagerApproval());
				bgcheck.setAddressVerified(personalInfo.getBgcheck().getAddressVerified());
				bgcheck.setRemark(personalInfo.getBgcheck().getRemark());
				bgcheck.setPreviousEmploymentStatusVerified(
						personalInfo.getBgcheck().getPreviousEmploymentStatusVerified());
				bgcheck.setPreviousDesignationAndResponsibilityVerified(
						personalInfo.getBgcheck().getPreviousDesignationAndResponsibilityVerified());
				bgcheck.setIdProofDocumentVerified(personalInfo.getBgcheck().getIdProofDocumentVerified());
				bgcheck.setEducationalQualificationVerified(
						personalInfo.getBgcheck().getEducationalQualificationVerified());
				bgcheck.setCriminalRecords(personalInfo.getBgcheck().getCriminalRecords());
				bgcheck.setPunishmentForImprisonmentApproval(
						personalInfo.getBgcheck().getPunishmentForImprisonmentApproval());
				bgcheck.setDeclarationRequired(personalInfo.getBgcheck().getDeclarationRequired());
				bgcheck.setExternalCompanyName(personalInfo.getBgcheck().getExternalCompanyName());
				bgcheck.setExternalName(personalInfo.getBgcheck().getExternalName());
				bgcheck.setExternalPost(personalInfo.getBgcheck().getExternalPost());
				bgcheck.setExternalPhoneCode(personalInfo.getBgcheck().getExternalPhoneCode());
				bgcheck.setExternalPhoneNo(personalInfo.getBgcheck().getExternalPhoneNo());
				if (recordsheet != null && !recordsheet.isEmpty()) {
					bgcheck.setRecordsheet(recordsheet.getBytes());
				}
				if (declarationRequired != null && !declarationRequired.isEmpty()) {
					bgcheck.setDeclarationRequired(declarationRequired.getBytes());
				}
				existingPersonalInfo.setBgcheck(bgcheck);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return dao.updatePersonalInfo(email, existingPersonalInfo);
	}

	public PersonalInfo updateVisaDetails(Long employeeId, String visaIssueDate, String visaExpiryDate) {
		try {
			PersonalInfo updateVisaDetails = null;
			updateVisaDetails = dao.updateVisaDetails(employeeId, visaIssueDate, visaExpiryDate);
			return updateVisaDetails;
		} catch (Exception e) {
			System.out.println(e);
			throw new RuntimeException("No personal information found for this employee ID: " + employeeId, e);
		}

	}

	@Override
	public List<NotificationHelper> getRequestedField() {
		List<NotificationHelper> notificationFields = null;
		try {
			notificationFields = dao.getNotificationFields();
			return notificationFields;
		} catch (Exception e) {
			System.out.println(e);
			throw new RuntimeException("Something went wrong. " + e);
		}
	}
	
	public void sendOnboardingEmail(String email, long employeeId, String name) {
	    SimpleMailMessage mailMessage = new SimpleMailMessage();
	    mailMessage.setTo(email);
	    mailMessage.setSubject("Welcome to SI Global Company");

	    // Create a more professional and welcoming email message
	    String emailText = "Dear Mr. " + name + ",\n\n"
	            + "We are pleased to welcome you to SI Global Company. Your employee ID is: " + employeeId + ".\n\n"
	            + "As a valued member of our team, you play a vital role in our organization's success.\n\n"
	            + "Please feel free to reach out to our HR department for any assistance during your onboarding process.\n\n"
	            + "We look forward to working with you and wish you a successful and rewarding career with us.\n\n"
	            + "Best regards,\n"
	            + "The SI Global Company Team";

	    mailMessage.setText(emailText);

	    sender.send(mailMessage);
	}
}
