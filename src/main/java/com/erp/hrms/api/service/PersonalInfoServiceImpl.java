package com.erp.hrms.api.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.erp.hrms.api.dao.IPersonalInfoDAO;
import com.erp.hrms.api.repo.IRoleRepository;
import com.erp.hrms.api.request.SignupRequest;
import com.erp.hrms.api.security.entity.RoleEntity;
import com.erp.hrms.api.security.entity.UserEntity;
import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.api.utill.ERole;
import com.erp.hrms.entity.BackgroundCheck;
import com.erp.hrms.entity.BloodRelative;
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

	public static String uplaodDirectory = System.getProperty("user.dir") + "/src/main/webapp/PersonalInfo/images";

	public static int generateRandom4DigitNumber() {
		Random random = new Random();
		return 1000 + random.nextInt(9000);
	}

	public static long concatenateIdWithRandomNumber(long id, int randomPart) {
		return (id * 10000L) + randomPart;
	}

	@Autowired
	IRoleRepository roleRepository;

	@Autowired
	private IPersonalInfoDAO dao;

	@Autowired
	private PersonalInfoFileService personalInfoFileService;

	@Autowired
	private JavaMailSender sender;

	@Override
	public void savedata(String personalinfo, String SignupRequest, String url, MultipartFile passportSizePhoto,
			MultipartFile OtherIdProofDoc, MultipartFile passportScan, MultipartFile licensecopy,
			MultipartFile relativeid, MultipartFile raddressproof, MultipartFile secondaryDocumentScan,
			MultipartFile seniorSecondaryDocumentScan, MultipartFile graduationDocumentScan,
			MultipartFile postGraduationDocumentScan, MultipartFile[] othersDocumentScan, MultipartFile[] degreeScan,
			MultipartFile payslipScan, MultipartFile recordsheet, MultipartFile PaidTrainingDocumentProof,
			MultipartFile CertificateUploadedForOutsource, MultipartFile visaDocs, MultipartFile diplomaDocumentScan,
			MultipartFile declarationRequired, MultipartFile[] achievementsRewardsDocs) throws IOException {

		ObjectMapper mapper = new ObjectMapper();
		PersonalInfo PersonalInfo = mapper.readValue(personalinfo, PersonalInfo.class);
		SignupRequest Signuprequest = mapper.readValue(SignupRequest, SignupRequest.class);

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
			if (!dao.existByID(employeeId)) {
				employeeId = employeeId * 10;
			}

			PersonalInfo.setEmployeeId(employeeId);

			PersonalInfo.setFathersFirstName("Mr " + PersonalInfo.getFathersFirstName());
			PersonalInfo.setStatus("Active");
			PersonalInfo.setEmpStatus("New employee");

			if (passportSizePhoto != null && !passportSizePhoto.isEmpty()) {
				String passportoriginalFileName = passportSizePhoto.getOriginalFilename();
				String passportfileNameWithUniqueIdentifier = employeeId + "-" + passportoriginalFileName;
				Path passportSizePhotoNameWithPath = Paths.get(uplaodDirectory, passportfileNameWithUniqueIdentifier);
				Files.write(passportSizePhotoNameWithPath, passportSizePhoto.getBytes());
				PersonalInfo.setPassportSizePhoto(passportfileNameWithUniqueIdentifier);
			}
			if (OtherIdProofDoc != null && !OtherIdProofDoc.isEmpty()) {
				String OtherIdProoforiginalFileName = OtherIdProofDoc.getOriginalFilename();
				String OtherIdProofDocfileNameWithUniqueIdentifier = employeeId + "-" + OtherIdProoforiginalFileName;
				Path OtherIdProofDocNameWithData = Paths.get(uplaodDirectory,
						OtherIdProofDocfileNameWithUniqueIdentifier);
				Files.write(OtherIdProofDocNameWithData, OtherIdProofDoc.getBytes());
				PersonalInfo.setOtherIdProofDoc(OtherIdProofDocfileNameWithUniqueIdentifier);
			}

			PassportDetails passportDetails = new PassportDetails();

			if (passportScan != null && !passportScan.isEmpty()) {
				String passportScanoriginalFileName = passportScan.getOriginalFilename();
				String passportScanfileNameWithUniqueIdentifier = employeeId + "-" + passportScanoriginalFileName;
				Path passportScanNameWithData = Paths.get(uplaodDirectory, passportScanfileNameWithUniqueIdentifier);
				Files.write(passportScanNameWithData, passportScan.getBytes());
				passportDetails.setPassportScan(passportScanfileNameWithUniqueIdentifier);
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
					String visaDocsoriginalFileName = visaDocs.getOriginalFilename();
					String visaDocsfileNameWithUniqueIdentifier = employeeId + "-" + visaDocsoriginalFileName;
					Path visaDocsNameWithData = Paths.get(uplaodDirectory, visaDocsfileNameWithUniqueIdentifier);
					Files.write(visaDocsNameWithData, visaDocs.getBytes());
					visaDetail.setVisaDocs(visaDocsfileNameWithUniqueIdentifier);
				}
			}
			PersonalInfo.setVisainfo(visaDetail);
			DrivingLicense drivinglicense = new DrivingLicense();
			if (licensecopy != null && !licensecopy.isEmpty()) {
				String licensecopyoriginalFileName = licensecopy.getOriginalFilename();
				String licensecopyfileNameWithUniqueIdentifier = employeeId + "-" + licensecopyoriginalFileName;
				Path licensecopyNameWithData = Paths.get(uplaodDirectory, licensecopyfileNameWithUniqueIdentifier);
				Files.write(licensecopyNameWithData, licensecopy.getBytes());
				drivinglicense.setLicensecopy(licensecopyfileNameWithUniqueIdentifier);
			}

			if (PersonalInfo.getLicense() != null) {
				drivinglicense.setDrivinglicense(PersonalInfo.getLicense().getDrivinglicense());
				drivinglicense.setOwnvehicle(PersonalInfo.getLicense().getOwnvehicle());
				drivinglicense.setLicenseType(PersonalInfo.getLicense().getLicenseType());
			}
			PersonalInfo.setLicense(drivinglicense);
			BloodRelative relative = new BloodRelative();
			if (relativeid != null && !relativeid.isEmpty()) {
				String relativeidoriginalFileName = relativeid.getOriginalFilename();
				String relativeidfileNameWithUniqueIdentifier = employeeId + "-" + relativeidoriginalFileName;
				Path relativeidNameWithData = Paths.get(uplaodDirectory, relativeidfileNameWithUniqueIdentifier);
				Files.write(relativeidNameWithData, relativeid.getBytes());
				relative.setRelativeid(relativeidfileNameWithUniqueIdentifier);
			}
			if (raddressproof != null && !raddressproof.isEmpty()) {
				String raddressprooforiginalFileName = raddressproof.getOriginalFilename();
				String raddressprooffileNameWithUniqueIdentifier = employeeId + "-" + raddressprooforiginalFileName;
				Path raddressproofNameWithData = Paths.get(uplaodDirectory, raddressprooffileNameWithUniqueIdentifier);
				Files.write(raddressproofNameWithData, raddressproof.getBytes());
				relative.setRaddressproof(raddressprooffileNameWithUniqueIdentifier);
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
						String secondaryDocumentScanoriginalFileName = secondaryDocumentScan.getOriginalFilename();
						String secondaryDocumentScanfileNameWithUniqueIdentifier = employeeId + "-"
								+ secondaryDocumentScanoriginalFileName;
						Path secondaryDocumentScanNameWithData = Paths.get(uplaodDirectory,
								secondaryDocumentScanfileNameWithUniqueIdentifier);
						Files.write(secondaryDocumentScanNameWithData, secondaryDocumentScan.getBytes());
						edc.setSecondaryDocumentScan(secondaryDocumentScanfileNameWithUniqueIdentifier);
					}
					if (seniorSecondaryDocumentScan != null && !seniorSecondaryDocumentScan.isEmpty()) {
						String seniorSecondaryDocumentScanoriginalFileName = seniorSecondaryDocumentScan
								.getOriginalFilename();
						String seniorSecondaryDocumentScanfileNameWithUniqueIdentifier = employeeId + "-"
								+ seniorSecondaryDocumentScanoriginalFileName;
						Path seniorSecondaryDocumentScanNameWithData = Paths.get(uplaodDirectory,
								seniorSecondaryDocumentScanfileNameWithUniqueIdentifier);
						Files.write(seniorSecondaryDocumentScanNameWithData, seniorSecondaryDocumentScan.getBytes());
						edc.setSeniorSecondaryDocumentScan(seniorSecondaryDocumentScanfileNameWithUniqueIdentifier);
					}
					if (graduationDocumentScan != null && !graduationDocumentScan.isEmpty()) {
						String graduationDocumentScanoriginalFileName = graduationDocumentScan.getOriginalFilename();
						String graduationDocumentScanfileNameWithUniqueIdentifier = employeeId + "-"
								+ graduationDocumentScanoriginalFileName;
						Path graduationDocumentScanNameWithData = Paths.get(uplaodDirectory,
								graduationDocumentScanfileNameWithUniqueIdentifier);
						Files.write(graduationDocumentScanNameWithData, graduationDocumentScan.getBytes());
						edc.setGraduationDocumentScan(graduationDocumentScanfileNameWithUniqueIdentifier);
					}
					if (postGraduationDocumentScan != null && !postGraduationDocumentScan.isEmpty()) {
						String postGraduationDocumentScanoriginalFileName = postGraduationDocumentScan
								.getOriginalFilename();
						String postGraduationDocumentScanfileNameWithUniqueIdentifier = employeeId + "-"
								+ postGraduationDocumentScanoriginalFileName;
						Path postGraduationDocumentScanNameWithData = Paths.get(uplaodDirectory,
								postGraduationDocumentScanfileNameWithUniqueIdentifier);
						Files.write(postGraduationDocumentScanNameWithData, postGraduationDocumentScan.getBytes());
						edc.setPostGraduationDocumentScan(postGraduationDocumentScanfileNameWithUniqueIdentifier);
					}
					if (diplomaDocumentScan != null && !diplomaDocumentScan.isEmpty()) {
						String diplomaDocumentScanoriginalFileName = diplomaDocumentScan.getOriginalFilename();
						String diplomaDocumentScanfileNameWithUniqueIdentifier = employeeId + "-"
								+ diplomaDocumentScanoriginalFileName;
						Path diplomaDocumentScanNameWithData = Paths.get(uplaodDirectory,
								diplomaDocumentScanfileNameWithUniqueIdentifier);
						Files.write(diplomaDocumentScanNameWithData, diplomaDocumentScan.getBytes());
						edc.setDiplomaDocumentScan(diplomaDocumentScanfileNameWithUniqueIdentifier);
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
							String othersDocumentScanoriginalFileName = file.getOriginalFilename();
							String othersDocumentScanfileNameWithUniqueIdentifier = employeeId + "-"
									+ othersDocumentScanoriginalFileName;
							Path othersDocumentScanNameWithData = Paths.get(uplaodDirectory,
									othersDocumentScanfileNameWithUniqueIdentifier);
							Files.write(othersDocumentScanNameWithData, file.getBytes());
							newOthersQualification
									.setOthersDocumentScan(othersDocumentScanfileNameWithUniqueIdentifier);
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
							String degreeScanoriginalFileName = file.getOriginalFilename();
							String degreeScanfileNameWithUniqueIdentifier = employeeId + "-"
									+ degreeScanoriginalFileName;
							Path degreeScanNameWithData = Paths.get(uplaodDirectory,
									degreeScanfileNameWithUniqueIdentifier);
							Files.write(degreeScanNameWithData, file.getBytes());
							professionalQualification.setDegreeScan(degreeScanfileNameWithUniqueIdentifier);
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
						String payslipScanoriginalFileName = payslipScan.getOriginalFilename();
						String payslipScanfileNameWithUniqueIdentifier = employeeId + "-" + payslipScanoriginalFileName;
						Path payslipScanNameWithData = Paths.get(uplaodDirectory,
								payslipScanfileNameWithUniqueIdentifier);
						Files.write(payslipScanNameWithData, payslipScan.getBytes());
						oldemp.setPayslipScan(payslipScanfileNameWithUniqueIdentifier);
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
									String achievementsRewardsDocsoriginalFileName = file.getOriginalFilename();
									String achievementsRewardsDocsfileNameWithUniqueIdentifier = employeeId + "-"
											+ achievementsRewardsDocsoriginalFileName;
									Path achievementsRewardsDocsNameWithData = Paths.get(uplaodDirectory,
											achievementsRewardsDocsfileNameWithUniqueIdentifier);
									Files.write(achievementsRewardsDocsNameWithData, file.getBytes());
									achievement.setAchievementsRewardsDocs(
											achievementsRewardsDocsfileNameWithUniqueIdentifier);
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
					String recordsheetoriginalFileName = recordsheet.getOriginalFilename();
					String recordsheetfileNameWithUniqueIdentifier = employeeId + "-" + recordsheetoriginalFileName;
					Path recordsheetNameWithData = Paths.get(uplaodDirectory, recordsheetfileNameWithUniqueIdentifier);
					Files.write(recordsheetNameWithData, recordsheet.getBytes());
					bgcheck.setRecordsheet(recordsheetfileNameWithUniqueIdentifier);
				}
				if (declarationRequired != null && !declarationRequired.isEmpty()) {
					String declarationRequiredoriginalFileName = declarationRequired.getOriginalFilename();
					String declarationRequiredfileNameWithUniqueIdentifier = employeeId + "-"
							+ declarationRequiredoriginalFileName;
					Path declarationRequiredNameWithData = Paths.get(uplaodDirectory,
							declarationRequiredfileNameWithUniqueIdentifier);
					Files.write(declarationRequiredNameWithData, declarationRequired.getBytes());
					bgcheck.setDeclarationRequired(declarationRequiredfileNameWithUniqueIdentifier);
				}
				bgcheck.setPersonalinfo(PersonalInfo);
			}
			PersonalInfo.setBgcheck(bgcheck);
			List<Trainingdetails> training = PersonalInfo.getTraining();
			if (training != null) {
				for (Trainingdetails train : training) {
					if (CertificateUploadedForOutsource != null && !CertificateUploadedForOutsource.isEmpty()) {
						String CertificateUploadedForOutsourceoriginalFileName = CertificateUploadedForOutsource
								.getOriginalFilename();
						String CertificateUploadedForOutsourcefileNameWithUniqueIdentifier = employeeId + "-"
								+ CertificateUploadedForOutsourceoriginalFileName;
						Path CertificateUploadedForOutsourceNameWithData = Paths.get(uplaodDirectory,
								CertificateUploadedForOutsourcefileNameWithUniqueIdentifier);
						Files.write(CertificateUploadedForOutsourceNameWithData,
								CertificateUploadedForOutsource.getBytes());
						train.setCertificateUploadedForOutsource(
								CertificateUploadedForOutsourcefileNameWithUniqueIdentifier);
					}
					if (PaidTrainingDocumentProof != null && !PaidTrainingDocumentProof.isEmpty()) {
						String PaidTrainingDocumentProoforiginalFileName = PaidTrainingDocumentProof
								.getOriginalFilename();
						String PaidTrainingDocumentProoffileNameWithUniqueIdentifier = employeeId + "-"
								+ PaidTrainingDocumentProoforiginalFileName;
						Path PaidTrainingDocumentProofNameWithData = Paths.get(uplaodDirectory,
								PaidTrainingDocumentProoffileNameWithUniqueIdentifier);
						Files.write(PaidTrainingDocumentProofNameWithData, PaidTrainingDocumentProof.getBytes());
						train.setPaidTrainingDocumentProof(PaidTrainingDocumentProoffileNameWithUniqueIdentifier);
					}
					train.setPersonalinfo(PersonalInfo);
				}
				PersonalInfo.setTraining(training);
			}

			Set<String> strRoles = Signuprequest.getRole();
			Set<RoleEntity> roles = new HashSet<>();

			System.out.println(strRoles);

			if (strRoles == null) {
				RoleEntity userRole = roleRepository.findByName(ERole.ROLE_EMPLOYEE)
						.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
				roles.add(userRole);
			} else {
				strRoles.forEach(role -> {
					switch (role) {
					case "admin":
						RoleEntity adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(adminRole);
						break;
					case "employee":
						RoleEntity empRole = roleRepository.findByName(ERole.ROLE_EMPLOYEE)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(empRole);
						break;
					case "manager":
						RoleEntity modRole = roleRepository.findByName(ERole.ROLE_MANAGER)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(modRole);
					}
				});
			}

			UserEntity user = new UserEntity();

			user.setEmail(PersonalInfo.getEmail());
			user.setUsername(String.valueOf(employeeId));
			user.setPersonalinfo(PersonalInfo);
			user.setRoles(roles);
			user.setEnabled(false);

			String activationToken = UUID.randomUUID().toString();
			user.setActivationToken(activationToken);

			PersonalInfo.setUserentity(user);

			dao.savePersonalInfo(PersonalInfo);

			String activationLink = url + "/activate?token=" + activationToken;

			sendAccountActivationEmail(PersonalInfo.getEmail(), employeeId, PersonalInfo.getFirstName(),
					activationLink);
		} catch (Exception e) {
			throw new RuntimeException("An error occurred while saving personal information.", e);
		}
	}

	@Override
	public List<PersonalInfo> findAllPersonalInfo() {
		try {
			List<PersonalInfo> findAllPersonalInfo = dao.findAllPersonalInfo();

			for (PersonalInfo personalInfo : findAllPersonalInfo) {
				String passportSizePhotoName = personalInfo.getPassportSizePhoto();
				if (passportSizePhotoName != null && !passportSizePhotoName.isEmpty()) {
					byte[] passportSizePhoteData = personalInfoFileService.getFileData(passportSizePhotoName);
					if (passportSizePhoteData != null) {
						personalInfo.setPassportSizePhotoData(passportSizePhoteData);
					}
				}

				String OtherIdProofDocName = personalInfo.getOtherIdProofDoc();
				if (OtherIdProofDocName != null && !OtherIdProofDocName.isEmpty()) {
					byte[] OtherIdProofDocNameData = personalInfoFileService.getFileData(OtherIdProofDocName);
					if (OtherIdProofDocNameData != null) {
						personalInfo.setOtherIdProofDocData(OtherIdProofDocNameData);
					}
				}

				String passportScanName = personalInfo.getPsDetail().getPassportScan();
				if (passportScanName != null && !passportScanName.isEmpty()) {
					byte[] passportScanData = personalInfoFileService.getFileData(passportScanName);
					if (passportScanData != null) {
						personalInfo.getPsDetail().setPassportScanData(passportScanData);
					}
				}

				String licensecopyName = personalInfo.getLicense().getLicensecopy();
				if (licensecopyName != null && !licensecopyName.isEmpty()) {
					byte[] licensecopyData = personalInfoFileService.getFileData(licensecopyName);
					if (licensecopyData != null) {
						personalInfo.getLicense().setLicenseCopyData(licensecopyData);
					}
				}

				String relativeidName = personalInfo.getRelative().getRelativeid();
				if (relativeidName != null && !relativeidName.isEmpty()) {
					byte[] relativeidData = personalInfoFileService.getFileData(relativeidName);
					if (relativeidData != null) {
						personalInfo.getRelative().setRelativeIdData(relativeidData);
					}
				}

				String raddressproofName = personalInfo.getRelative().getRaddressproof();
				if (raddressproofName != null && !raddressproofName.isEmpty()) {
					byte[] raddressproofData = personalInfoFileService.getFileData(raddressproofName);
					if (raddressproofData != null) {
						personalInfo.getRelative().setRaddressProofData(raddressproofData);
					}
				}

				String visaDocsName = personalInfo.getVisainfo().getVisaDocs();
				if (visaDocsName != null && !visaDocsName.isEmpty()) {
					byte[] visaDocsData = personalInfoFileService.getFileData(visaDocsName);
					if (visaDocsData != null) {
						personalInfo.getVisainfo().setVisaDocsData(visaDocsData);
					}
				}

				for (Education education : personalInfo.getEducations()) {

					String secondaryDocumentScanName = education.getSecondaryDocumentScan();
					if (secondaryDocumentScanName != null && !secondaryDocumentScanName.isEmpty()) {
						byte[] secondaryDocumentScanData = personalInfoFileService
								.getFileData(secondaryDocumentScanName);
						if (secondaryDocumentScanData != null) {
							education.setSecondaryDocumentScanData(secondaryDocumentScanData);
						}
					}

					String seniorSecondaryDocumentScanName = education.getSeniorSecondaryDocumentScan();
					if (seniorSecondaryDocumentScanName != null && !seniorSecondaryDocumentScanName.isEmpty()) {
						byte[] seniorSecondaryDocumentScanData = personalInfoFileService
								.getFileData(seniorSecondaryDocumentScanName);
						if (seniorSecondaryDocumentScanData != null) {
							education.setSeniorSecondaryDocumentScanData(seniorSecondaryDocumentScanData);
						}
					}

					String graduationDocumentScanName = education.getGraduationDocumentScan();
					if (graduationDocumentScanName != null && !graduationDocumentScanName.isEmpty()) {
						byte[] graduationDocumentScanData = personalInfoFileService
								.getFileData(graduationDocumentScanName);
						if (graduationDocumentScanData != null) {
							education.setGraduationDocumentScanData(graduationDocumentScanData);
						}
					}

					String postGraduationDocumentScanName = education.getPostGraduationDocumentScan();
					if (postGraduationDocumentScanName != null && !postGraduationDocumentScanName.isEmpty()) {
						byte[] postGraduationDocumentScanData = personalInfoFileService
								.getFileData(postGraduationDocumentScanName);
						if (postGraduationDocumentScanData != null) {
							education.setPostGraduationDocumentScanData(postGraduationDocumentScanData);
						}
					}

					String diplomaDocumentScanName = education.getDiplomaDocumentScan();
					if (diplomaDocumentScanName != null && !diplomaDocumentScanName.isEmpty()) {
						byte[] diplomaDocumentScanData = personalInfoFileService.getFileData(diplomaDocumentScanName);
						if (diplomaDocumentScanData != null) {
							education.setDiplomaDocumentScanData(diplomaDocumentScanData);
						}
					}

				}

				for (OthersQualification othersQualification : personalInfo.getOthersQualifications()) {

					String othersDocumentScanName = othersQualification.getOthersDocumentScan();
					if (othersDocumentScanName != null && !othersDocumentScanName.isEmpty()) {
						byte[] othersDocumentScanData = personalInfoFileService.getFileData(othersDocumentScanName);
						if (othersDocumentScanData != null) {
							othersQualification.setOthersDocumentScanData(othersDocumentScanData);
						}
					}
				}

				for (ProfessionalQualification professionalQualification : personalInfo
						.getProfessionalQualifications()) {

					String degreeScanName = professionalQualification.getDegreeScan();
					if (degreeScanName != null && !degreeScanName.isEmpty()) {
						byte[] degreeScanData = personalInfoFileService.getFileData(degreeScanName);
						if (degreeScanData != null) {
							professionalQualification.setDegreeScanData(degreeScanData);
						}
					}
				}

				for (PreviousEmployee previousEmployee : personalInfo.getOldEmployee()) {

					String payslipScanName = previousEmployee.getPayslipScan();
					if (payslipScanName != null && !payslipScanName.isEmpty()) {
						byte[] degreeScanData = personalInfoFileService.getFileData(payslipScanName);
						if (degreeScanData != null) {
							previousEmployee.setPayslipScanData(degreeScanData);
						}
					}
					for (EmpAchievement empAchievement : previousEmployee.getEmpAchievements()) {

						String achievementsRewardsDocsName = empAchievement.getAchievementsRewardsDocs();
						if (achievementsRewardsDocsName != null && !achievementsRewardsDocsName.isEmpty()) {
							byte[] achievementsRewardsDocsData = personalInfoFileService
									.getFileData(achievementsRewardsDocsName);
							if (achievementsRewardsDocsData != null) {
								empAchievement.setAchievementsRewardsDocsData(achievementsRewardsDocsData);
							}
						}
					}
				}

				String recordsheetName = personalInfo.getBgcheck().getRecordsheet();
				if (recordsheetName != null && !recordsheetName.isEmpty()) {
					byte[] recordsheetData = personalInfoFileService.getFileData(recordsheetName);
					if (recordsheetData != null) {
						personalInfo.getBgcheck().setRecordSheetData(recordsheetData);
					}
				}

				String declarationRequiredName = personalInfo.getBgcheck().getDeclarationRequired();
				if (declarationRequiredName != null && !declarationRequiredName.isEmpty()) {
					byte[] declarationRequiredData = personalInfoFileService.getFileData(declarationRequiredName);
					if (declarationRequiredData != null) {
						personalInfo.getBgcheck().setDeclarationRequiredData(declarationRequiredData);
					}
				}

				for (Trainingdetails trainingdetails : personalInfo.getTraining()) {

					String PaidTrainingDocumentProofName = trainingdetails.getPaidTrainingDocumentProof();
					if (PaidTrainingDocumentProofName != null && !PaidTrainingDocumentProofName.isEmpty()) {
						byte[] PaidTrainingDocumentProofData = personalInfoFileService
								.getFileData(PaidTrainingDocumentProofName);
						if (PaidTrainingDocumentProofData != null) {
							trainingdetails.setPaidTrainingDocumentProofData(PaidTrainingDocumentProofData);
						}
					}
				}
			}

			return findAllPersonalInfo;
		} catch (Exception e) {
			throw new RuntimeException("Failed to retrieve personal info: " + e.getMessage());

		}
	}

	@Override
	public PersonalInfo getPersonalInfoByEmail(String email) throws IOException {
		try {
			PersonalInfo personalInfoByEmail = dao.getPersonalInfoByEmail(email);
			if (personalInfoByEmail == null) {
				throw new PersonalInfoNotFoundException(
						new MessageResponse("No personal information found for this email ID: " + email));
			}

			String passportSizePhotoName = personalInfoByEmail.getPassportSizePhoto();
			if (passportSizePhotoName != null && !passportSizePhotoName.isEmpty()) {
				byte[] passportSizePhoteData = personalInfoFileService.getFileData(passportSizePhotoName);
				if (passportSizePhoteData != null) {
					personalInfoByEmail.setPassportSizePhotoData(passportSizePhoteData);
				}
			}

			String OtherIdProofDocName = personalInfoByEmail.getOtherIdProofDoc();
			if (OtherIdProofDocName != null && !OtherIdProofDocName.isEmpty()) {
				byte[] OtherIdProofDocNameData = personalInfoFileService.getFileData(OtherIdProofDocName);
				if (OtherIdProofDocNameData != null) {
					personalInfoByEmail.setOtherIdProofDocData(OtherIdProofDocNameData);
				}
			}

			String passportScanName = personalInfoByEmail.getPsDetail().getPassportScan();
			if (passportScanName != null && !passportScanName.isEmpty()) {
				byte[] passportScanData = personalInfoFileService.getFileData(passportScanName);
				if (passportScanData != null) {
					personalInfoByEmail.getPsDetail().setPassportScanData(passportScanData);
				}
			}

			String licensecopyName = personalInfoByEmail.getLicense().getLicensecopy();
			if (licensecopyName != null && !licensecopyName.isEmpty()) {
				byte[] licensecopyData = personalInfoFileService.getFileData(licensecopyName);
				if (licensecopyData != null) {
					personalInfoByEmail.getLicense().setLicenseCopyData(licensecopyData);
				}
			}

			String relativeidName = personalInfoByEmail.getRelative().getRelativeid();
			if (relativeidName != null && !relativeidName.isEmpty()) {
				byte[] relativeidData = personalInfoFileService.getFileData(relativeidName);
				if (relativeidData != null) {
					personalInfoByEmail.getRelative().setRelativeIdData(relativeidData);
				}
			}

			String raddressproofName = personalInfoByEmail.getRelative().getRaddressproof();
			if (raddressproofName != null && !raddressproofName.isEmpty()) {
				byte[] raddressproofData = personalInfoFileService.getFileData(raddressproofName);
				if (raddressproofData != null) {
					personalInfoByEmail.getRelative().setRaddressProofData(raddressproofData);
				}
			}

			String visaDocsName = personalInfoByEmail.getVisainfo().getVisaDocs();
			if (visaDocsName != null && !visaDocsName.isEmpty()) {
				byte[] visaDocsData = personalInfoFileService.getFileData(visaDocsName);
				if (visaDocsData != null) {
					personalInfoByEmail.getVisainfo().setVisaDocsData(visaDocsData);
				}
			}

			for (Education education : personalInfoByEmail.getEducations()) {

				String secondaryDocumentScanName = education.getSecondaryDocumentScan();
				if (secondaryDocumentScanName != null && !secondaryDocumentScanName.isEmpty()) {
					byte[] secondaryDocumentScanData = personalInfoFileService.getFileData(secondaryDocumentScanName);
					if (secondaryDocumentScanData != null) {
						education.setSecondaryDocumentScanData(secondaryDocumentScanData);
					}
				}

				String seniorSecondaryDocumentScanName = education.getSeniorSecondaryDocumentScan();
				if (seniorSecondaryDocumentScanName != null && !seniorSecondaryDocumentScanName.isEmpty()) {
					byte[] seniorSecondaryDocumentScanData = personalInfoFileService
							.getFileData(seniorSecondaryDocumentScanName);
					if (seniorSecondaryDocumentScanData != null) {
						education.setSeniorSecondaryDocumentScanData(seniorSecondaryDocumentScanData);
					}
				}

				String graduationDocumentScanName = education.getGraduationDocumentScan();
				if (graduationDocumentScanName != null && !graduationDocumentScanName.isEmpty()) {
					byte[] graduationDocumentScanData = personalInfoFileService.getFileData(graduationDocumentScanName);
					if (graduationDocumentScanData != null) {
						education.setGraduationDocumentScanData(graduationDocumentScanData);
					}
				}

				String postGraduationDocumentScanName = education.getPostGraduationDocumentScan();
				if (postGraduationDocumentScanName != null && !postGraduationDocumentScanName.isEmpty()) {
					byte[] postGraduationDocumentScanData = personalInfoFileService
							.getFileData(postGraduationDocumentScanName);
					if (postGraduationDocumentScanData != null) {
						education.setPostGraduationDocumentScanData(postGraduationDocumentScanData);
					}
				}

				String diplomaDocumentScanName = education.getDiplomaDocumentScan();
				if (diplomaDocumentScanName != null && !diplomaDocumentScanName.isEmpty()) {
					byte[] diplomaDocumentScanData = personalInfoFileService.getFileData(diplomaDocumentScanName);
					if (diplomaDocumentScanData != null) {
						education.setDiplomaDocumentScanData(diplomaDocumentScanData);
					}
				}

			}

			for (OthersQualification othersQualification : personalInfoByEmail.getOthersQualifications()) {

				String othersDocumentScanName = othersQualification.getOthersDocumentScan();
				if (othersDocumentScanName != null && !othersDocumentScanName.isEmpty()) {
					byte[] othersDocumentScanData = personalInfoFileService.getFileData(othersDocumentScanName);
					if (othersDocumentScanData != null) {
						othersQualification.setOthersDocumentScanData(othersDocumentScanData);
					}
				}
			}

			for (ProfessionalQualification professionalQualification : personalInfoByEmail
					.getProfessionalQualifications()) {

				String degreeScanName = professionalQualification.getDegreeScan();
				if (degreeScanName != null && !degreeScanName.isEmpty()) {
					byte[] degreeScanData = personalInfoFileService.getFileData(degreeScanName);
					if (degreeScanData != null) {
						professionalQualification.setDegreeScanData(degreeScanData);
					}
				}
			}

			for (PreviousEmployee previousEmployee : personalInfoByEmail.getOldEmployee()) {

				String payslipScanName = previousEmployee.getPayslipScan();
				if (payslipScanName != null && !payslipScanName.isEmpty()) {
					byte[] degreeScanData = personalInfoFileService.getFileData(payslipScanName);
					if (degreeScanData != null) {
						previousEmployee.setPayslipScanData(degreeScanData);
					}
				}
				for (EmpAchievement empAchievement : previousEmployee.getEmpAchievements()) {

					String achievementsRewardsDocsName = empAchievement.getAchievementsRewardsDocs();
					if (achievementsRewardsDocsName != null && !achievementsRewardsDocsName.isEmpty()) {
						byte[] achievementsRewardsDocsData = personalInfoFileService
								.getFileData(achievementsRewardsDocsName);
						if (achievementsRewardsDocsData != null) {
							empAchievement.setAchievementsRewardsDocsData(achievementsRewardsDocsData);
						}
					}
				}
			}

			String recordsheetName = personalInfoByEmail.getBgcheck().getRecordsheet();
			if (recordsheetName != null && !recordsheetName.isEmpty()) {
				byte[] recordsheetData = personalInfoFileService.getFileData(recordsheetName);
				if (recordsheetData != null) {
					personalInfoByEmail.getBgcheck().setRecordSheetData(recordsheetData);
				}
			}

			String declarationRequiredName = personalInfoByEmail.getBgcheck().getDeclarationRequired();
			if (declarationRequiredName != null && !declarationRequiredName.isEmpty()) {
				byte[] declarationRequiredData = personalInfoFileService.getFileData(declarationRequiredName);
				if (declarationRequiredData != null) {
					personalInfoByEmail.getBgcheck().setDeclarationRequiredData(declarationRequiredData);
				}
			}

			for (Trainingdetails trainingdetails : personalInfoByEmail.getTraining()) {

				String PaidTrainingDocumentProofName = trainingdetails.getPaidTrainingDocumentProof();
				if (PaidTrainingDocumentProofName != null && !PaidTrainingDocumentProofName.isEmpty()) {
					byte[] PaidTrainingDocumentProofData = personalInfoFileService
							.getFileData(PaidTrainingDocumentProofName);
					if (PaidTrainingDocumentProofData != null) {
						trainingdetails.setPaidTrainingDocumentProofData(PaidTrainingDocumentProofData);
					}
				}
			}

			return personalInfoByEmail;
		} catch (Exception e) {
			throw new RuntimeException("An error occurred while retrieving personal information for email: " + email,
					e);
		}
	}

	@Override
	public PersonalInfo getPersonalInfoByEmployeeId(Long employeeId) throws IOException {
		try {
			PersonalInfo personalInfoByEmployeeId = dao.getPersonalInfoByEmployeeId(employeeId);
			if (personalInfoByEmployeeId == null) {
				throw new PersonalInfoNotFoundException(
						new MessageResponse("No personal information found for this employee ID: " + employeeId));
			}

			String passportSizePhotoName = personalInfoByEmployeeId.getPassportSizePhoto();
			if (passportSizePhotoName != null && !passportSizePhotoName.isEmpty()) {
				byte[] passportSizePhoteData = personalInfoFileService.getFileData(passportSizePhotoName);
				if (passportSizePhoteData != null) {
					personalInfoByEmployeeId.setPassportSizePhotoData(passportSizePhoteData);
				}
			}

			String OtherIdProofDocName = personalInfoByEmployeeId.getOtherIdProofDoc();
			if (OtherIdProofDocName != null && !OtherIdProofDocName.isEmpty()) {
				byte[] OtherIdProofDocNameData = personalInfoFileService.getFileData(OtherIdProofDocName);
				if (OtherIdProofDocNameData != null) {
					personalInfoByEmployeeId.setOtherIdProofDocData(OtherIdProofDocNameData);
				}
			}

			String passportScanName = personalInfoByEmployeeId.getPsDetail().getPassportScan();
			if (passportScanName != null && !passportScanName.isEmpty()) {
				byte[] passportScanData = personalInfoFileService.getFileData(passportScanName);
				if (passportScanData != null) {
					personalInfoByEmployeeId.getPsDetail().setPassportScanData(passportScanData);
				}
			}

			String licensecopyName = personalInfoByEmployeeId.getLicense().getLicensecopy();
			if (licensecopyName != null && !licensecopyName.isEmpty()) {
				byte[] licensecopyData = personalInfoFileService.getFileData(licensecopyName);
				if (licensecopyData != null) {
					personalInfoByEmployeeId.getLicense().setLicenseCopyData(licensecopyData);
				}
			}

			String relativeidName = personalInfoByEmployeeId.getRelative().getRelativeid();
			if (relativeidName != null && !relativeidName.isEmpty()) {
				byte[] relativeidData = personalInfoFileService.getFileData(relativeidName);
				if (relativeidData != null) {
					personalInfoByEmployeeId.getRelative().setRelativeIdData(relativeidData);
				}
			}

			String raddressproofName = personalInfoByEmployeeId.getRelative().getRaddressproof();
			if (raddressproofName != null && !raddressproofName.isEmpty()) {
				byte[] raddressproofData = personalInfoFileService.getFileData(raddressproofName);
				if (raddressproofData != null) {
					personalInfoByEmployeeId.getRelative().setRaddressProofData(raddressproofData);
				}
			}

			String visaDocsName = personalInfoByEmployeeId.getVisainfo().getVisaDocs();
			if (visaDocsName != null && !visaDocsName.isEmpty()) {
				byte[] visaDocsData = personalInfoFileService.getFileData(visaDocsName);
				if (visaDocsData != null) {
					personalInfoByEmployeeId.getVisainfo().setVisaDocsData(visaDocsData);
				}
			}

			for (Education education : personalInfoByEmployeeId.getEducations()) {

				String secondaryDocumentScanName = education.getSecondaryDocumentScan();
				if (secondaryDocumentScanName != null && !secondaryDocumentScanName.isEmpty()) {
					byte[] secondaryDocumentScanData = personalInfoFileService.getFileData(secondaryDocumentScanName);
					if (secondaryDocumentScanData != null) {
						education.setSecondaryDocumentScanData(secondaryDocumentScanData);
					}
				}

				String seniorSecondaryDocumentScanName = education.getSeniorSecondaryDocumentScan();
				if (seniorSecondaryDocumentScanName != null && !seniorSecondaryDocumentScanName.isEmpty()) {
					byte[] seniorSecondaryDocumentScanData = personalInfoFileService
							.getFileData(seniorSecondaryDocumentScanName);
					if (seniorSecondaryDocumentScanData != null) {
						education.setSeniorSecondaryDocumentScanData(seniorSecondaryDocumentScanData);
					}
				}

				String graduationDocumentScanName = education.getGraduationDocumentScan();
				if (graduationDocumentScanName != null && !graduationDocumentScanName.isEmpty()) {
					byte[] graduationDocumentScanData = personalInfoFileService.getFileData(graduationDocumentScanName);
					if (graduationDocumentScanData != null) {
						education.setGraduationDocumentScanData(graduationDocumentScanData);
					}
				}

				String postGraduationDocumentScanName = education.getPostGraduationDocumentScan();
				if (postGraduationDocumentScanName != null && !postGraduationDocumentScanName.isEmpty()) {
					byte[] postGraduationDocumentScanData = personalInfoFileService
							.getFileData(postGraduationDocumentScanName);
					if (postGraduationDocumentScanData != null) {
						education.setPostGraduationDocumentScanData(postGraduationDocumentScanData);
					}
				}

				String diplomaDocumentScanName = education.getDiplomaDocumentScan();
				if (diplomaDocumentScanName != null && !diplomaDocumentScanName.isEmpty()) {
					byte[] diplomaDocumentScanData = personalInfoFileService.getFileData(diplomaDocumentScanName);
					if (diplomaDocumentScanData != null) {
						education.setDiplomaDocumentScanData(diplomaDocumentScanData);
					}
				}

			}

			for (OthersQualification othersQualification : personalInfoByEmployeeId.getOthersQualifications()) {

				String othersDocumentScanName = othersQualification.getOthersDocumentScan();
				if (othersDocumentScanName != null && !othersDocumentScanName.isEmpty()) {
					byte[] othersDocumentScanData = personalInfoFileService.getFileData(othersDocumentScanName);
					if (othersDocumentScanData != null) {
						othersQualification.setOthersDocumentScanData(othersDocumentScanData);
					}
				}
			}

			for (ProfessionalQualification professionalQualification : personalInfoByEmployeeId
					.getProfessionalQualifications()) {

				String degreeScanName = professionalQualification.getDegreeScan();
				if (degreeScanName != null && !degreeScanName.isEmpty()) {
					byte[] degreeScanData = personalInfoFileService.getFileData(degreeScanName);
					if (degreeScanData != null) {
						professionalQualification.setDegreeScanData(degreeScanData);
					}
				}
			}

			for (PreviousEmployee previousEmployee : personalInfoByEmployeeId.getOldEmployee()) {

				String payslipScanName = previousEmployee.getPayslipScan();
				if (payslipScanName != null && !payslipScanName.isEmpty()) {
					byte[] degreeScanData = personalInfoFileService.getFileData(payslipScanName);
					if (degreeScanData != null) {
						previousEmployee.setPayslipScanData(degreeScanData);
					}
				}
				for (EmpAchievement empAchievement : previousEmployee.getEmpAchievements()) {

					String achievementsRewardsDocsName = empAchievement.getAchievementsRewardsDocs();
					if (achievementsRewardsDocsName != null && !achievementsRewardsDocsName.isEmpty()) {
						byte[] achievementsRewardsDocsData = personalInfoFileService
								.getFileData(achievementsRewardsDocsName);
						if (achievementsRewardsDocsData != null) {
							empAchievement.setAchievementsRewardsDocsData(achievementsRewardsDocsData);
						}
					}
				}
			}

			String recordsheetName = personalInfoByEmployeeId.getBgcheck().getRecordsheet();
			if (recordsheetName != null && !recordsheetName.isEmpty()) {
				byte[] recordsheetData = personalInfoFileService.getFileData(recordsheetName);
				if (recordsheetData != null) {
					personalInfoByEmployeeId.getBgcheck().setRecordSheetData(recordsheetData);
				}
			}

			String declarationRequiredName = personalInfoByEmployeeId.getBgcheck().getDeclarationRequired();
			if (declarationRequiredName != null && !declarationRequiredName.isEmpty()) {
				byte[] declarationRequiredData = personalInfoFileService.getFileData(declarationRequiredName);
				if (declarationRequiredData != null) {
					personalInfoByEmployeeId.getBgcheck().setDeclarationRequiredData(declarationRequiredData);
				}
			}

			for (Trainingdetails trainingdetails : personalInfoByEmployeeId.getTraining()) {

				String PaidTrainingDocumentProofName = trainingdetails.getPaidTrainingDocumentProof();
				if (PaidTrainingDocumentProofName != null && !PaidTrainingDocumentProofName.isEmpty()) {
					byte[] PaidTrainingDocumentProofData = personalInfoFileService
							.getFileData(PaidTrainingDocumentProofName);
					if (PaidTrainingDocumentProofData != null) {
						trainingdetails.setPaidTrainingDocumentProofData(PaidTrainingDocumentProofData);
					}
				}
			}

			return personalInfoByEmployeeId;
		} catch (Exception e) {
			throw new RuntimeException(
					"An error occurred while retrieving personal information for Employee Id: " + employeeId, e);
		}
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

			existingPersonalInfo.setEmployeeId(personalInfo.getEmployeeId());
			long employeeId = existingPersonalInfo.getEmployeeId();

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
				// Delete the old passportSizePhoto file if it exists
				if (existingPersonalInfo.getPassportSizePhoto() != null) {
					Path oldPassportSizePhotoPath = Paths.get(uplaodDirectory,
							existingPersonalInfo.getPassportSizePhoto());
					Files.deleteIfExists(oldPassportSizePhotoPath);
				}
				// Save the new passportSizePhoto file
				String passportoriginalFileName = passportSizePhoto.getOriginalFilename();
				String passportfileNameWithUniqueIdentifier = employeeId + "-" + passportoriginalFileName;
				Path passportSizePhotoNameWithPath = Paths.get(uplaodDirectory, passportfileNameWithUniqueIdentifier);
				Files.write(passportSizePhotoNameWithPath, passportSizePhoto.getBytes());
				existingPersonalInfo.setPassportSizePhoto(passportfileNameWithUniqueIdentifier);
			}
			existingPersonalInfo.setPersonalContactNo(personalInfo.getPersonalContactNo());
			existingPersonalInfo.setCitizenship(personalInfo.getCitizenship());
			if (OtherIdProofDoc != null && !OtherIdProofDoc.isEmpty()) {
				// Delete the old OtherIdProofDoc file if it exists
				if (existingPersonalInfo.getOtherIdProofDoc() != null) {
					Path oldOtherIdProofDocPath = Paths.get(uplaodDirectory, existingPersonalInfo.getOtherIdProofDoc());
					Files.deleteIfExists(oldOtherIdProofDocPath);
				}
				// Save the new OtherIdProofDoc file
				String OtherIdProoforiginalFileName = OtherIdProofDoc.getOriginalFilename();
				String OtherIdProofDocfileNameWithUniqueIdentifier = employeeId + "-" + OtherIdProoforiginalFileName;
				Path OtherIdProofDocNameWithData = Paths.get(uplaodDirectory,
						OtherIdProofDocfileNameWithUniqueIdentifier);
				Files.write(OtherIdProofDocNameWithData, OtherIdProofDoc.getBytes());
				existingPersonalInfo.setOtherIdProofDoc(OtherIdProofDocfileNameWithUniqueIdentifier);
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
					// Delete the old passportScan file if it exists
					if (passportDetails.getPassportScan() != null) {
						Path oldpassportScanPath = Paths.get(uplaodDirectory, passportDetails.getPassportScan());
						Files.deleteIfExists(oldpassportScanPath);
					}
					// Save the new passportScan file
					String passportScanoriginalFileName = passportScan.getOriginalFilename();
					String passportScanfileNameWithUniqueIdentifier = employeeId + "-" + passportScanoriginalFileName;
					Path passportScanNameWithData = Paths.get(uplaodDirectory,
							passportScanfileNameWithUniqueIdentifier);
					Files.write(passportScanNameWithData, passportScan.getBytes());
					passportDetails.setPassportScan(passportScanfileNameWithUniqueIdentifier);
				}
			}
			existingPersonalInfo.setPsDetail(passportDetails);

			DrivingLicense drivingLicense = existingPersonalInfo.getLicense();
			if (personalInfo.getLicense() != null) {
				drivingLicense.setDrivinglicense(personalInfo.getLicense().getDrivinglicense());
				drivingLicense.setOwnvehicle(personalInfo.getLicense().getOwnvehicle());
				drivingLicense.setLicenseType(personalInfo.getLicense().getLicenseType());
				if (licensecopy != null && !licensecopy.isEmpty()) {
					// Delete the old licensecopy file if it exists
					if (drivingLicense.getLicensecopy() != null) {
						Path oldlicensecopyPath = Paths.get(uplaodDirectory, drivingLicense.getLicensecopy());
						Files.deleteIfExists(oldlicensecopyPath);
					}
					// Save the new licensecopy file
					String licensecopyoriginalFileName = licensecopy.getOriginalFilename();
					String licensecopyfileNameWithUniqueIdentifier = employeeId + "-" + licensecopyoriginalFileName;
					Path licensecopyNameWithData = Paths.get(uplaodDirectory, licensecopyfileNameWithUniqueIdentifier);
					Files.write(licensecopyNameWithData, licensecopy.getBytes());
					drivingLicense.setLicensecopy(licensecopyfileNameWithUniqueIdentifier);
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
					// Delete the old relativeid file if it exists
					if (bloodRelative.getRelativeid() != null) {
						Path oldrelativeidPath = Paths.get(uplaodDirectory, bloodRelative.getRelativeid());
						Files.deleteIfExists(oldrelativeidPath);
					}
					// Save the new relativeid file
					String relativeidoriginalFileName = relativeid.getOriginalFilename();
					String relativeidfileNameWithUniqueIdentifier = employeeId + "-" + relativeidoriginalFileName;
					Path relativeidNameWithData = Paths.get(uplaodDirectory, relativeidfileNameWithUniqueIdentifier);
					Files.write(relativeidNameWithData, relativeid.getBytes());
					bloodRelative.setRelativeid(relativeidfileNameWithUniqueIdentifier);
				}
				if (raddressproof != null && !raddressproof.isEmpty()) {
					// Delete the old raddressproof file if it exists
					if (bloodRelative.getRaddressproof() != null) {
						Path oldraddressproofPath = Paths.get(uplaodDirectory, bloodRelative.getRaddressproof());
						Files.deleteIfExists(oldraddressproofPath);
					}
					// Save the new raddressproof file
					String raddressprooforiginalFileName = raddressproof.getOriginalFilename();
					String raddressprooffileNameWithUniqueIdentifier = employeeId + "-" + raddressprooforiginalFileName;
					Path raddressproofNameWithData = Paths.get(uplaodDirectory,
							raddressprooffileNameWithUniqueIdentifier);
					Files.write(raddressproofNameWithData, raddressproof.getBytes());
					bloodRelative.setRaddressproof(raddressprooffileNameWithUniqueIdentifier);
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
					// Delete the old visaDocs file if it exists
					if (visaDetail.getVisaDocs() != null) {
						Path oldvisaDocsPath = Paths.get(uplaodDirectory, visaDetail.getVisaDocs());
						Files.deleteIfExists(oldvisaDocsPath);
					}
					// Save the new visaDocs file
					String visaDocsoriginalFileName = visaDocs.getOriginalFilename();
					String visaDocsfileNameWithUniqueIdentifier = employeeId + "-" + visaDocsoriginalFileName;
					Path visaDocsNameWithData = Paths.get(uplaodDirectory, visaDocsfileNameWithUniqueIdentifier);
					Files.write(visaDocsNameWithData, visaDocs.getBytes());
					visaDetail.setVisaDocs(visaDocsfileNameWithUniqueIdentifier);
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
						// Delete the old secondaryDocumentScan file if it exists
						if (existingEducation.getSecondaryDocumentScan() != null) {
							Path oldsecondaryDocumentScanPath = Paths.get(uplaodDirectory,
									existingEducation.getSecondaryDocumentScan());
							Files.deleteIfExists(oldsecondaryDocumentScanPath);
						}
						// Save the new secondaryDocumentScan file
						String secondaryDocumentScanoriginalFileName = secondaryDocumentScan.getOriginalFilename();
						String secondaryDocumentScanfileNameWithUniqueIdentifier = employeeId + "-"
								+ secondaryDocumentScanoriginalFileName;
						Path secondaryDocumentScanNameWithData = Paths.get(uplaodDirectory,
								secondaryDocumentScanfileNameWithUniqueIdentifier);
						Files.write(secondaryDocumentScanNameWithData, secondaryDocumentScan.getBytes());
						existingEducation.setSecondaryDocumentScan(secondaryDocumentScanfileNameWithUniqueIdentifier);
					}
					if (seniorSecondaryDocumentScan != null && !seniorSecondaryDocumentScan.isEmpty()) {
						// Delete the old seniorSecondaryDocumentScan file if it exists
						if (existingEducation.getSeniorSecondaryDocumentScan() != null) {
							Path oldseniorSecondaryDocumentScanPath = Paths.get(uplaodDirectory,
									existingEducation.getSeniorSecondaryDocumentScan());
							Files.deleteIfExists(oldseniorSecondaryDocumentScanPath);
						}
						// Save the new seniorSecondaryDocumentScan file
						String seniorSecondaryDocumentScanoriginalFileName = seniorSecondaryDocumentScan
								.getOriginalFilename();
						String seniorSecondaryDocumentScanfileNameWithUniqueIdentifier = employeeId + "-"
								+ seniorSecondaryDocumentScanoriginalFileName;
						Path seniorSecondaryDocumentScanNameWithData = Paths.get(uplaodDirectory,
								seniorSecondaryDocumentScanfileNameWithUniqueIdentifier);
						Files.write(seniorSecondaryDocumentScanNameWithData, seniorSecondaryDocumentScan.getBytes());
						existingEducation.setSeniorSecondaryDocumentScan(
								seniorSecondaryDocumentScanfileNameWithUniqueIdentifier);
					}
					if (graduationDocumentScan != null && !graduationDocumentScan.isEmpty()) {
						// Delete the old graduationDocumentScan file if it exists
						if (existingEducation.getGraduationDocumentScan() != null) {
							Path oldGraduationDocumentScanPath = Paths.get(uplaodDirectory,
									existingEducation.getGraduationDocumentScan());
							Files.deleteIfExists(oldGraduationDocumentScanPath);
						}
						// Save the new graduationDocumentScan file
						String graduationDocumentScanoriginalFileName = graduationDocumentScan.getOriginalFilename();
						String graduationDocumentScanfileNameWithUniqueIdentifier = employeeId + "-"
								+ graduationDocumentScanoriginalFileName;
						Path graduationDocumentScanNameWithData = Paths.get(uplaodDirectory,
								graduationDocumentScanfileNameWithUniqueIdentifier);
						Files.write(graduationDocumentScanNameWithData, graduationDocumentScan.getBytes());
						existingEducation.setGraduationDocumentScan(graduationDocumentScanfileNameWithUniqueIdentifier);
					}
					if (postGraduationDocumentScan != null && !postGraduationDocumentScan.isEmpty()) {
						// Delete the old postGraduationDocumentScan file if it exists
						if (existingEducation.getPostGraduationDocumentScan() != null) {
							Path oldpostGraduationDocumentScanPath = Paths.get(uplaodDirectory,
									existingEducation.getPostGraduationDocumentScan());
							Files.deleteIfExists(oldpostGraduationDocumentScanPath);
						}
						// Save the new postGraduationDocumentScan file
						String postGraduationDocumentScanoriginalFileName = postGraduationDocumentScan
								.getOriginalFilename();
						String postGraduationDocumentScanfileNameWithUniqueIdentifier = employeeId + "-"
								+ postGraduationDocumentScanoriginalFileName;
						Path postGraduationDocumentScanNameWithData = Paths.get(uplaodDirectory,
								postGraduationDocumentScanfileNameWithUniqueIdentifier);
						Files.write(postGraduationDocumentScanNameWithData, postGraduationDocumentScan.getBytes());
						existingEducation
								.setPostGraduationDocumentScan(postGraduationDocumentScanfileNameWithUniqueIdentifier);
					}
					if (diplomaDocumentScan != null && !diplomaDocumentScan.isEmpty()) {
						// Delete the old diplomaDocumentScan file if it exists
						if (existingEducation.getDiplomaDocumentScan() != null) {
							Path olddiplomaDocumentScanPath = Paths.get(uplaodDirectory,
									existingEducation.getDiplomaDocumentScan());
							Files.deleteIfExists(olddiplomaDocumentScanPath);
						}
						// Save the new diplomaDocumentScan file
						String diplomaDocumentScanoriginalFileName = diplomaDocumentScan.getOriginalFilename();
						String diplomaDocumentScanfileNameWithUniqueIdentifier = employeeId + "-"
								+ diplomaDocumentScanoriginalFileName;
						Path diplomaDocumentScanNameWithData = Paths.get(uplaodDirectory,
								diplomaDocumentScanfileNameWithUniqueIdentifier);
						Files.write(diplomaDocumentScanNameWithData, diplomaDocumentScan.getBytes());
						existingEducation.setDiplomaDocumentScan(diplomaDocumentScanfileNameWithUniqueIdentifier);
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
							// Check and delete the old others document scan file if it exists
							if (existingOthersQualification.getOthersDocumentScan() != null) {
								Path oldOthersDocumentScanPath = Paths.get(uplaodDirectory,
										existingOthersQualification.getOthersDocumentScan());
								Files.deleteIfExists(oldOthersDocumentScanPath);
							}
							// Save the new othersDocumentScan file
							String othersDocumentScanoriginalFileName = file.getOriginalFilename();
							String othersDocumentScanfileNameWithUniqueIdentifier = employeeId + "-"
									+ othersDocumentScanoriginalFileName;
							Path othersDocumentScanNameWithData = Paths.get(uplaodDirectory,
									othersDocumentScanfileNameWithUniqueIdentifier);
							Files.write(othersDocumentScanNameWithData, file.getBytes());
							existingOthersQualification
									.setOthersDocumentScan(othersDocumentScanfileNameWithUniqueIdentifier);
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
							// Check and delete the old degreeScan file if it exists
							if (existingProfessionalQualification.getDegreeScan() != null) {
								Path olddegreeScanPath = Paths.get(uplaodDirectory,
										existingProfessionalQualification.getDegreeScan());
								Files.deleteIfExists(olddegreeScanPath);
							}
							// Save the new degreeScan file
							String degreeScanoriginalFileName = file.getOriginalFilename();
							String degreeScanfileNameWithUniqueIdentifier = employeeId + "-"
									+ degreeScanoriginalFileName;
							Path degreeScanNameWithData = Paths.get(uplaodDirectory,
									degreeScanfileNameWithUniqueIdentifier);
							Files.write(degreeScanNameWithData, file.getBytes());
							existingProfessionalQualification.setDegreeScan(degreeScanfileNameWithUniqueIdentifier);
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
						// Check and delete the old payslipScan file if it exists
						if (existingPreviousEmployee.getPayslipScan() != null) {
							Path oldpayslipScanPath = Paths.get(uplaodDirectory,
									existingPreviousEmployee.getPayslipScan());
							Files.deleteIfExists(oldpayslipScanPath);
						}
						// Save the new payslipScan file
						String payslipScanoriginalFileName = payslipScan.getOriginalFilename();
						String payslipScanfileNameWithUniqueIdentifier = employeeId + "-" + payslipScanoriginalFileName;
						Path payslipScanNameWithData = Paths.get(uplaodDirectory,
								payslipScanfileNameWithUniqueIdentifier);
						Files.write(payslipScanNameWithData, payslipScan.getBytes());
						existingPreviousEmployee.setPayslipScan(payslipScanfileNameWithUniqueIdentifier);
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
							if (achievementsRewardsDocs != null && i < achievementsRewardsDocs.length) {
								MultipartFile file = achievementsRewardsDocs[i];
								if (file != null && !file.isEmpty()) {
									// Check and delete the old achievementsRewardsDocs file if it exists
									if (existingAchievement.getAchievementsRewardsDocs() != null) {
										Path oldachievementsRewardsDocsPath = Paths.get(uplaodDirectory,
												existingAchievement.getAchievementsRewardsDocs());
										Files.deleteIfExists(oldachievementsRewardsDocsPath);
									}
									// Save the new achievementsRewardsDocs file
									String achievementsRewardsDocsoriginalFileName = file.getOriginalFilename();
									String achievementsRewardsDocsfileNameWithUniqueIdentifier = employeeId + "-"
											+ achievementsRewardsDocsoriginalFileName;
									Path achievementsRewardsDocsNameWithData = Paths.get(uplaodDirectory,
											achievementsRewardsDocsfileNameWithUniqueIdentifier);
									Files.write(achievementsRewardsDocsNameWithData, file.getBytes());
									existingAchievement.setAchievementsRewardsDocs(
											achievementsRewardsDocsfileNameWithUniqueIdentifier);
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
						// Check and delete the old CertificateUploadedForOutsource file if it exists
						if (existingTrainingdetail.getCertificateUploadedForOutsource() != null) {
							Path oldCertificateUploadedForOutsourcePath = Paths.get(uplaodDirectory,
									existingTrainingdetail.getCertificateUploadedForOutsource());
							Files.deleteIfExists(oldCertificateUploadedForOutsourcePath);
						}
						// Save the new CertificateUploadedForOutsource file
						String CertificateUploadedForOutsourceoriginalFileName = CertificateUploadedForOutsource
								.getOriginalFilename();
						String CertificateUploadedForOutsourcefileNameWithUniqueIdentifier = employeeId + "-"
								+ CertificateUploadedForOutsourceoriginalFileName;
						Path CertificateUploadedForOutsourceNameWithData = Paths.get(uplaodDirectory,
								CertificateUploadedForOutsourcefileNameWithUniqueIdentifier);
						Files.write(CertificateUploadedForOutsourceNameWithData,
								CertificateUploadedForOutsource.getBytes());
						existingTrainingdetail.setCertificateUploadedForOutsource(
								CertificateUploadedForOutsourcefileNameWithUniqueIdentifier);
					}
					if (PaidTrainingDocumentProof != null && !PaidTrainingDocumentProof.isEmpty()) {
						// Check and delete the old PaidTrainingDocumentProof file if it exists
						if (existingTrainingdetail.getPaidTrainingDocumentProof() != null) {
							Path oldPaidTrainingDocumentProofPath = Paths.get(uplaodDirectory,
									existingTrainingdetail.getPaidTrainingDocumentProof());
							Files.deleteIfExists(oldPaidTrainingDocumentProofPath);
						}
						// Save the new PaidTrainingDocumentProof file
						String PaidTrainingDocumentProoforiginalFileName = PaidTrainingDocumentProof
								.getOriginalFilename();
						String PaidTrainingDocumentProoffileNameWithUniqueIdentifier = employeeId + "-"
								+ PaidTrainingDocumentProoforiginalFileName;
						Path PaidTrainingDocumentProofNameWithData = Paths.get(uplaodDirectory,
								PaidTrainingDocumentProoffileNameWithUniqueIdentifier);
						Files.write(PaidTrainingDocumentProofNameWithData, PaidTrainingDocumentProof.getBytes());
						existingTrainingdetail
								.setPaidTrainingDocumentProof(PaidTrainingDocumentProoffileNameWithUniqueIdentifier);
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
					// Check and delete the old recordsheet file if it exists
					if (bgcheck.getRecordsheet() != null) {
						Path oldrecordsheetPath = Paths.get(uplaodDirectory, bgcheck.getRecordsheet());
						Files.deleteIfExists(oldrecordsheetPath);
					}
					// Save the new recordsheet file
					String recordsheetoriginalFileName = recordsheet.getOriginalFilename();
					String recordsheetfileNameWithUniqueIdentifier = employeeId + "-" + recordsheetoriginalFileName;
					Path recordsheetNameWithData = Paths.get(uplaodDirectory, recordsheetfileNameWithUniqueIdentifier);
					Files.write(recordsheetNameWithData, recordsheet.getBytes());
					bgcheck.setRecordsheet(recordsheetfileNameWithUniqueIdentifier);
				}
				if (declarationRequired != null && !declarationRequired.isEmpty()) {
					// Check and delete the old declarationRequired file if it exists
					if (bgcheck.getDeclarationRequired() != null) {
						Path olddeclarationRequiredPath = Paths.get(uplaodDirectory, bgcheck.getDeclarationRequired());
						Files.deleteIfExists(olddeclarationRequiredPath);
					}
					// Save the new declarationRequired file
					String declarationRequiredoriginalFileName = declarationRequired.getOriginalFilename();
					String declarationRequiredfileNameWithUniqueIdentifier = employeeId + "-"
							+ declarationRequiredoriginalFileName;
					Path declarationRequiredNameWithData = Paths.get(uplaodDirectory,
							declarationRequiredfileNameWithUniqueIdentifier);
					Files.write(declarationRequiredNameWithData, declarationRequired.getBytes());
					bgcheck.setDeclarationRequired(declarationRequiredfileNameWithUniqueIdentifier);
				}
				existingPersonalInfo.setBgcheck(bgcheck);
			}
		} catch (Exception e) {
			throw new RuntimeException("Something went wrong: " + e.getMessage());
		}
		return dao.updatePersonalInfo(email, existingPersonalInfo);
	}

	public PersonalInfo updateVisaDetails(Long employeeId, String visaIssueDate, String visaExpiryDate) {
		try {
			PersonalInfo updateVisaDetails = null;
			updateVisaDetails = dao.updateVisaDetails(employeeId, visaIssueDate, visaExpiryDate);
			return updateVisaDetails;
		} catch (Exception e) {
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
			throw new RuntimeException("Something went wrong. " + e);
		}
	}

	public void sendAccountActivationEmail(String email, long employeeId, String name, String activationLink) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(email);
		mailMessage.setSubject("Account Activation");

		// Create the account activation email message
		String emailText = "Dear Mr. " + name + ",\n\n" + "Welcome to our platform. Your employee ID is: " + employeeId
				+ ".\n\n" + "To activate your account, please click on the following link:\n" + activationLink + "\n\n"
				+ "If you have any questions or need assistance, please contact our support team.\n\n"
				+ "Best regards,\n" + "The SI Global Company Team";

		mailMessage.setText(emailText);

		sender.send(mailMessage);
	}

}
