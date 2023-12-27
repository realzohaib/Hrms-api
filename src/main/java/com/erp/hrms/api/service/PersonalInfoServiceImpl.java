package com.erp.hrms.api.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.erp.hrms.api.dao.IPersonalInfoDAO;
import com.erp.hrms.api.repo.IRoleRepository;
import com.erp.hrms.api.repo.IjobLevelRepo;
import com.erp.hrms.api.security.entity.RoleEntity;
import com.erp.hrms.api.security.entity.UserEntity;
import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.api.utill.ERole;
import com.erp.hrms.entity.BackgroundCheck;
import com.erp.hrms.entity.BloodRelative;
import com.erp.hrms.entity.Department;
import com.erp.hrms.entity.Designation;
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
import com.erp.hrms.weekOff.service.weekOffserviceImpl;
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
	private IPersonalInfoDAO dao;

	@Autowired
	private PersonalInfoFileService personalInfoFileService;

	@Autowired
	private JavaMailSender sender;
	
	@Autowired
	private weekOffserviceImpl weekoff;
	
	@Autowired
	private IjobLevelRepo joblevel;
	
	@Autowired
	private IRoleRepository roleRepository;

	/**
	 *
	 */
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
		com.erp.hrms.api.request.SignupRequest Signuprequest = mapper.readValue(SignupRequest, com.erp.hrms.api.request.SignupRequest.class);

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

				}
			}
			PersonalInfo.setOldEmployee(oldEmployee);

			List<EmpAchievement> empAchievements = PersonalInfo.getEmpAchievements();
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
							achievement.setAchievementsRewardsDocs(achievementsRewardsDocsfileNameWithUniqueIdentifier);
						}
					}
					achievement.setPersonalinfo(PersonalInfo);
				}
			}
			PersonalInfo.setEmpAchievements(empAchievements);

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

			List<Designation> designations = PersonalInfo.getDesignations();
			if (designations != null) {
				for (Designation designation : designations) {
					designation.setEmployeeId(employeeId);
					JobDetails jobDetails = PersonalInfo.getJobDetails().get(0);
					designation.setStartDate(jobDetails.getJoiningDate());
				}
				PersonalInfo.setDesignations(designations);
			}

			Set<String> strRoles = Signuprequest.getRoles();
			Set<RoleEntity> roles = new HashSet<>();

//			System.out.println(strRoles);

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
						break;
					case "hr":
						RoleEntity hrRole = roleRepository.findByName(ERole.ROLE_HR)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(hrRole);
						break;
					case "subadmin":
						RoleEntity subAdminRole = roleRepository.findByName(ERole.ROLE_SUBADMIN)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found"));
						roles.add(subAdminRole);
					}
				});

			}
			System.out.println(strRoles);


			UserEntity user = new UserEntity();

			user.setEmail(PersonalInfo.getEmail());
			user.setUsername(String.valueOf(employeeId));
			user.setPersonalinfo(PersonalInfo);
			user.setRoles(roles);
			user.setEnabled(false);

			Random random = new Random();
			int otpNumber = random.nextInt(900000) + 100000;
			String otp = String.valueOf(otpNumber);

			user.setOtp(otp);

			PersonalInfo.setUserentity(user);
			
			dao.savePersonalInfo(PersonalInfo);

			sendAccountActivationEmail(PersonalInfo.getEmail(), employeeId, PersonalInfo.getFirstName(), otp);
			
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

				String passportScanName = null;
				if (personalInfo.getPsDetail() != null) {
					passportScanName = personalInfo.getPsDetail().getPassportScan();
				}

				if (passportScanName != null && !passportScanName.isEmpty()) {
					byte[] passportScanData = personalInfoFileService.getFileData(passportScanName);
					if (passportScanData != null) {
						personalInfo.getPsDetail().setPassportScanData(passportScanData);
					}
				}

				String licensecopyName = null;
				if (personalInfo.getLicense() != null) {
					licensecopyName = personalInfo.getLicense().getLicensecopy();
				}
				if (licensecopyName != null && !licensecopyName.isEmpty()) {
					byte[] licensecopyData = personalInfoFileService.getFileData(licensecopyName);
					if (licensecopyData != null) {
						personalInfo.getLicense().setLicenseCopyData(licensecopyData);
					}
				}

				BloodRelative relative = personalInfo.getRelative();
				if (relative != null) {
					String raddressproofName = relative.getRaddressproof();
					if (raddressproofName != null && !raddressproofName.isEmpty()) {
						byte[] raddressproofData = personalInfoFileService.getFileData(raddressproofName);
						if (raddressproofData != null) {
							relative.setRaddressProofData(raddressproofData);
						}
					}
					String relativeidName = relative.getRelativeid();
					if (relativeidName != null && !relativeidName.isEmpty()) {
						byte[] relativeidData = personalInfoFileService.getFileData(relativeidName);
						if (relativeidData != null) {
							relative.setRelativeIdData(relativeidData);
						}
					}
				}

				String visaDocsName = null;
				if (personalInfo.getVisainfo() != null) {
					visaDocsName = personalInfo.getVisainfo().getVisaDocs();
				}
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

				for (EmpAchievement empAchievement : personalInfo.getEmpAchievements()) {

					String achievementsRewardsDocsName = empAchievement.getAchievementsRewardsDocs();
					if (achievementsRewardsDocsName != null && !achievementsRewardsDocsName.isEmpty()) {
						byte[] achievementsRewardsDocsData = personalInfoFileService
								.getFileData(achievementsRewardsDocsName);
						if (achievementsRewardsDocsData != null) {
							empAchievement.setAchievementsRewardsDocsData(achievementsRewardsDocsData);
						}
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

			String passportScanName = null;
			if (personalInfoByEmail.getPsDetail() != null) {
				passportScanName = personalInfoByEmail.getPsDetail().getPassportScan();
			}

			if (passportScanName != null && !passportScanName.isEmpty()) {
				byte[] passportScanData = personalInfoFileService.getFileData(passportScanName);
				if (passportScanData != null) {
					personalInfoByEmail.getPsDetail().setPassportScanData(passportScanData);
				}
			}

			String licensecopyName = null;
			if (personalInfoByEmail.getLicense() != null) {
				licensecopyName = personalInfoByEmail.getLicense().getLicensecopy();
			}
			if (licensecopyName != null && !licensecopyName.isEmpty()) {
				byte[] licensecopyData = personalInfoFileService.getFileData(licensecopyName);
				if (licensecopyData != null) {
					personalInfoByEmail.getLicense().setLicenseCopyData(licensecopyData);
				}
			}

			BloodRelative relative = personalInfoByEmail.getRelative();
			if (relative != null) {
				String raddressproofName = relative.getRaddressproof();
				if (raddressproofName != null && !raddressproofName.isEmpty()) {
					byte[] raddressproofData = personalInfoFileService.getFileData(raddressproofName);
					if (raddressproofData != null) {
						relative.setRaddressProofData(raddressproofData);
					}
				}
				String relativeidName = relative.getRelativeid();
				if (relativeidName != null && !relativeidName.isEmpty()) {
					byte[] relativeidData = personalInfoFileService.getFileData(relativeidName);
					if (relativeidData != null) {
						relative.setRelativeIdData(relativeidData);
					}
				}
			}

			String visaDocsName = null;
			if (personalInfoByEmail.getVisainfo() != null) {
				visaDocsName = personalInfoByEmail.getVisainfo().getVisaDocs();
			}
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

			}

			for (EmpAchievement empAchievement : personalInfoByEmail.getEmpAchievements()) {

				String achievementsRewardsDocsName = empAchievement.getAchievementsRewardsDocs();
				if (achievementsRewardsDocsName != null && !achievementsRewardsDocsName.isEmpty()) {
					byte[] achievementsRewardsDocsData = personalInfoFileService
							.getFileData(achievementsRewardsDocsName);
					if (achievementsRewardsDocsData != null) {
						empAchievement.setAchievementsRewardsDocsData(achievementsRewardsDocsData);
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

			String passportScanName = null;
			if (personalInfoByEmployeeId.getPsDetail() != null) {
				passportScanName = personalInfoByEmployeeId.getPsDetail().getPassportScan();
			}

			if (passportScanName != null && !passportScanName.isEmpty()) {
				byte[] passportScanData = personalInfoFileService.getFileData(passportScanName);
				if (passportScanData != null) {
					personalInfoByEmployeeId.getPsDetail().setPassportScanData(passportScanData);
				}
			}

			String licensecopyName = null;
			if (personalInfoByEmployeeId.getLicense() != null) {
				licensecopyName = personalInfoByEmployeeId.getLicense().getLicensecopy();
			}
			if (licensecopyName != null && !licensecopyName.isEmpty()) {
				byte[] licensecopyData = personalInfoFileService.getFileData(licensecopyName);
				if (licensecopyData != null) {
					personalInfoByEmployeeId.getLicense().setLicenseCopyData(licensecopyData);
				}
			}

			BloodRelative relative = personalInfoByEmployeeId.getRelative();
			if (relative != null) {
				String raddressproofName = relative.getRaddressproof();
				if (raddressproofName != null && !raddressproofName.isEmpty()) {
					byte[] raddressproofData = personalInfoFileService.getFileData(raddressproofName);
					if (raddressproofData != null) {
						relative.setRaddressProofData(raddressproofData);
					}
				}
				String relativeidName = relative.getRelativeid();
				if (relativeidName != null && !relativeidName.isEmpty()) {
					byte[] relativeidData = personalInfoFileService.getFileData(relativeidName);
					if (relativeidData != null) {
						relative.setRelativeIdData(relativeidData);
					}
				}
			}

			String visaDocsName = null;
			if (personalInfoByEmployeeId.getVisainfo() != null) {
				visaDocsName = personalInfoByEmployeeId.getVisainfo().getVisaDocs();
			}
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

			}

			for (EmpAchievement empAchievement : personalInfoByEmployeeId.getEmpAchievements()) {

				String achievementsRewardsDocsName = empAchievement.getAchievementsRewardsDocs();
				if (achievementsRewardsDocsName != null && !achievementsRewardsDocsName.isEmpty()) {
					byte[] achievementsRewardsDocsData = personalInfoFileService
							.getFileData(achievementsRewardsDocsName);
					if (achievementsRewardsDocsData != null) {
						empAchievement.setAchievementsRewardsDocsData(achievementsRewardsDocsData);
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

	@Override
	public PersonalInfo updatePersonalInfo(String email, String personalInfoJson, MultipartFile passportSizePhoto,
			MultipartFile OtherIdProofDoc, MultipartFile passportScan, MultipartFile licensecopy,
			MultipartFile relativeid, MultipartFile raddressproof, MultipartFile secondaryDocumentScan,
			MultipartFile seniorSecondaryDocumentScan, MultipartFile graduationDocumentScan,
			MultipartFile postGraduationDocumentScan, MultipartFile[] othersDocumentScan, MultipartFile[] degreeScan,
			MultipartFile payslipScan, MultipartFile recordsheet, MultipartFile PaidTrainingDocumentProof,
			MultipartFile CertificateUploadedForOutsource, MultipartFile visaDocs, MultipartFile diplomaDocumentScan,
			MultipartFile declarationRequired, MultipartFile[] achievementsRewardsDocs) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		PersonalInfo personalInfo = mapper.readValue(personalInfoJson, PersonalInfo.class);

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
			existingPersonalInfo.setFilledForm(personalInfo.isFilledForm());
			existingPersonalInfo.setOnboardHrApprovalStatus(personalInfo.getOnboardHrApprovalStatus());

			if (passportSizePhoto != null && !passportSizePhoto.isEmpty()) {
				if (existingPersonalInfo.getPassportSizePhoto() != null) {
					Path oldPassportSizePhotoPath = Paths.get(uplaodDirectory,
							existingPersonalInfo.getPassportSizePhoto());
					Files.deleteIfExists(oldPassportSizePhotoPath);
				}
				String passportoriginalFileName = passportSizePhoto.getOriginalFilename();
				String passportfileNameWithUniqueIdentifier = employeeId + "-" + passportoriginalFileName;
				Path passportSizePhotoNameWithPath = Paths.get(uplaodDirectory, passportfileNameWithUniqueIdentifier);
				Files.write(passportSizePhotoNameWithPath, passportSizePhoto.getBytes());
				existingPersonalInfo.setPassportSizePhoto(passportfileNameWithUniqueIdentifier);
			}
			existingPersonalInfo.setPersonalContactNo(personalInfo.getPersonalContactNo());
			existingPersonalInfo.setCitizenship(personalInfo.getCitizenship());
			if (OtherIdProofDoc != null && !OtherIdProofDoc.isEmpty()) {
				if (existingPersonalInfo.getOtherIdProofDoc() != null) {
					Path oldOtherIdProofDocPath = Paths.get(uplaodDirectory, existingPersonalInfo.getOtherIdProofDoc());
					Files.deleteIfExists(oldOtherIdProofDocPath);
				}
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
			if (passportDetails == null) {
				passportDetails = new PassportDetails();
				existingPersonalInfo.setPsDetail(passportDetails);
			}

			passportDetails.setPassportNumber(personalInfo.getPsDetail().getPassportNumber());
			passportDetails.setPassportIssuingCountry(personalInfo.getPsDetail().getPassportIssuingCountry());
			passportDetails.setPassportExpiryDate(personalInfo.getPsDetail().getPassportExpiryDate());
			if (passportScan != null && !passportScan.isEmpty()) {

				if (passportDetails.getPassportScan() != null) {
					Path oldpassportScanPath = Paths.get(uplaodDirectory, passportDetails.getPassportScan());
					Files.deleteIfExists(oldpassportScanPath);
				}

				String passportScanoriginalFileName = passportScan.getOriginalFilename();
				String passportScanfileNameWithUniqueIdentifier = employeeId + "-" + passportScanoriginalFileName;
				Path passportScanNameWithData = Paths.get(uplaodDirectory, passportScanfileNameWithUniqueIdentifier);
				Files.write(passportScanNameWithData, passportScan.getBytes());
				passportDetails.setPassportScan(passportScanfileNameWithUniqueIdentifier);
			}

			existingPersonalInfo.setPsDetail(passportDetails);

			DrivingLicense drivingLicense = existingPersonalInfo.getLicense();
			if (drivingLicense == null) {
				drivingLicense = new DrivingLicense();
				existingPersonalInfo.setLicense(drivingLicense);
			}
			drivingLicense.setDrivinglicense(personalInfo.getLicense().getDrivinglicense());
			drivingLicense.setOwnvehicle(personalInfo.getLicense().getOwnvehicle());
			drivingLicense.setLicenseType(personalInfo.getLicense().getLicenseType());
			if (licensecopy != null && !licensecopy.isEmpty()) {
				if (drivingLicense.getLicensecopy() != null) {
					Path oldlicensecopyPath = Paths.get(uplaodDirectory, drivingLicense.getLicensecopy());
					Files.deleteIfExists(oldlicensecopyPath);
				}
				String licensecopyoriginalFileName = licensecopy.getOriginalFilename();
				String licensecopyfileNameWithUniqueIdentifier = employeeId + "-" + licensecopyoriginalFileName;
				Path licensecopyNameWithData = Paths.get(uplaodDirectory, licensecopyfileNameWithUniqueIdentifier);
				Files.write(licensecopyNameWithData, licensecopy.getBytes());
				drivingLicense.setLicensecopy(licensecopyfileNameWithUniqueIdentifier);
			}
			existingPersonalInfo.setLicense(drivingLicense);

			BloodRelative bloodRelative = existingPersonalInfo.getRelative();
			if (bloodRelative == null) {
				bloodRelative = new BloodRelative();
				existingPersonalInfo.setRelative(bloodRelative);
			}

			bloodRelative.setRFirstname(personalInfo.getRelative().getRFirstname());
			bloodRelative.setRmiddlename(personalInfo.getRelative().getRmiddlename());
			bloodRelative.setRlastname(personalInfo.getRelative().getRlastname());
			bloodRelative.setRaddress(personalInfo.getRelative().getRaddress());
			bloodRelative.setRphoneCode(personalInfo.getRelative().getRphoneCode());
			bloodRelative.setRcontactno(personalInfo.getRelative().getRcontactno());
			bloodRelative.setRelationship(personalInfo.getRelative().getRelationship());

			if (relativeid != null && !relativeid.isEmpty()) {
				if (bloodRelative.getRelativeid() != null) {
					Path oldrelativeidPath = Paths.get(uplaodDirectory, bloodRelative.getRelativeid());
					Files.deleteIfExists(oldrelativeidPath);
				}
				String relativeidoriginalFileName = relativeid.getOriginalFilename();
				String relativeidfileNameWithUniqueIdentifier = employeeId + "-" + relativeidoriginalFileName;
				Path relativeidNameWithData = Paths.get(uplaodDirectory, relativeidfileNameWithUniqueIdentifier);
				Files.write(relativeidNameWithData, relativeid.getBytes());
				bloodRelative.setRelativeid(relativeidfileNameWithUniqueIdentifier);
			}
			if (raddressproof != null && !raddressproof.isEmpty()) {
				if (bloodRelative.getRaddressproof() != null) {
					Path oldraddressproofPath = Paths.get(uplaodDirectory, bloodRelative.getRaddressproof());
					Files.deleteIfExists(oldraddressproofPath);
				}
				String raddressprooforiginalFileName = raddressproof.getOriginalFilename();
				String raddressprooffileNameWithUniqueIdentifier = employeeId + "-" + raddressprooforiginalFileName;
				Path raddressproofNameWithData = Paths.get(uplaodDirectory, raddressprooffileNameWithUniqueIdentifier);
				Files.write(raddressproofNameWithData, raddressproof.getBytes());
				bloodRelative.setRaddressproof(raddressprooffileNameWithUniqueIdentifier);
			}

			existingPersonalInfo.setRelative(bloodRelative);

			VisaDetail visaDetail = existingPersonalInfo.getVisainfo();
			if (visaDetail == null) {
				visaDetail = new VisaDetail();
				existingPersonalInfo.setVisainfo(visaDetail);
			}
			visaDetail.setWorkVisaEmirateId(personalInfo.getVisainfo().getWorkVisaEmirateId());
			visaDetail.setCategoryOfVisa(personalInfo.getVisainfo().getCategoryOfVisa());
			visaDetail.setVisaType(personalInfo.getVisainfo().getVisaType());
			visaDetail.setSiGlobalWorkVisaCompany(personalInfo.getVisainfo().getSiGlobalWorkVisaCompany());
			visaDetail.setVisaIssueyDate(personalInfo.getVisainfo().getVisaIssueyDate());
			visaDetail.setVisaExpiryDate(personalInfo.getVisainfo().getVisaExpiryDate());
			if (visaDocs != null && !visaDocs.isEmpty()) {
				if (visaDetail.getVisaDocs() != null) {
					Path oldvisaDocsPath = Paths.get(uplaodDirectory, visaDetail.getVisaDocs());
					Files.deleteIfExists(oldvisaDocsPath);
				}
				String visaDocsoriginalFileName = visaDocs.getOriginalFilename();
				String visaDocsfileNameWithUniqueIdentifier = employeeId + "-" + visaDocsoriginalFileName;
				Path visaDocsNameWithData = Paths.get(uplaodDirectory, visaDocsfileNameWithUniqueIdentifier);
				Files.write(visaDocsNameWithData, visaDocs.getBytes());
				visaDetail.setVisaDocs(visaDocsfileNameWithUniqueIdentifier);
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
						if (existingEducation.getSecondaryDocumentScan() != null) {
							Path oldsecondaryDocumentScanPath = Paths.get(uplaodDirectory,
									existingEducation.getSecondaryDocumentScan());
							Files.deleteIfExists(oldsecondaryDocumentScanPath);
						}
						String secondaryDocumentScanoriginalFileName = secondaryDocumentScan.getOriginalFilename();
						String secondaryDocumentScanfileNameWithUniqueIdentifier = employeeId + "-"
								+ secondaryDocumentScanoriginalFileName;
						Path secondaryDocumentScanNameWithData = Paths.get(uplaodDirectory,
								secondaryDocumentScanfileNameWithUniqueIdentifier);
						Files.write(secondaryDocumentScanNameWithData, secondaryDocumentScan.getBytes());
						existingEducation.setSecondaryDocumentScan(secondaryDocumentScanfileNameWithUniqueIdentifier);
					}
					if (seniorSecondaryDocumentScan != null && !seniorSecondaryDocumentScan.isEmpty()) {
						if (existingEducation.getSeniorSecondaryDocumentScan() != null) {
							Path oldseniorSecondaryDocumentScanPath = Paths.get(uplaodDirectory,
									existingEducation.getSeniorSecondaryDocumentScan());
							Files.deleteIfExists(oldseniorSecondaryDocumentScanPath);
						}
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
						if (existingEducation.getGraduationDocumentScan() != null) {
							Path oldGraduationDocumentScanPath = Paths.get(uplaodDirectory,
									existingEducation.getGraduationDocumentScan());
							Files.deleteIfExists(oldGraduationDocumentScanPath);
						}
						String graduationDocumentScanoriginalFileName = graduationDocumentScan.getOriginalFilename();
						String graduationDocumentScanfileNameWithUniqueIdentifier = employeeId + "-"
								+ graduationDocumentScanoriginalFileName;
						Path graduationDocumentScanNameWithData = Paths.get(uplaodDirectory,
								graduationDocumentScanfileNameWithUniqueIdentifier);
						Files.write(graduationDocumentScanNameWithData, graduationDocumentScan.getBytes());
						existingEducation.setGraduationDocumentScan(graduationDocumentScanfileNameWithUniqueIdentifier);
					}
					if (postGraduationDocumentScan != null && !postGraduationDocumentScan.isEmpty()) {
						if (existingEducation.getPostGraduationDocumentScan() != null) {
							Path oldpostGraduationDocumentScanPath = Paths.get(uplaodDirectory,
									existingEducation.getPostGraduationDocumentScan());
							Files.deleteIfExists(oldpostGraduationDocumentScanPath);
						}
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
						if (existingEducation.getDiplomaDocumentScan() != null) {
							Path olddiplomaDocumentScanPath = Paths.get(uplaodDirectory,
									existingEducation.getDiplomaDocumentScan());
							Files.deleteIfExists(olddiplomaDocumentScanPath);
						}
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

			List<OthersQualification> existingOthersQualifications = existingPersonalInfo.getOthersQualifications();

			if (personalInfo.getOthersQualifications() != null) {
				List<OthersQualification> newOthersQualifications = personalInfo.getOthersQualifications();

				Map<String, OthersQualification> existingOthersMap = new HashMap<>();
				for (OthersQualification existingQualification : existingOthersQualifications) {
					String key = generateUniqueKey(existingQualification);

					existingOthersMap.put(key, existingQualification);
				}

				for (int i = 0; i < newOthersQualifications.size(); i++) {
					OthersQualification newQualification = newOthersQualifications.get(i);
					String key = generateUniqueKey(newQualification);

					OthersQualification existingQualification = existingOthersMap.get(key);
					if (existingQualification != null
							&& existingQualification.getId().equals(newQualification.getId())) {

						updateExistingQualification(existingQualification, newQualification);
						continue;
					} else {

						OthersQualification newQualificationToAdd = addOthersQualification(newQualification,
								existingPersonalInfo);

						if (othersDocumentScan != null && i < othersDocumentScan.length) {
							MultipartFile file = othersDocumentScan[i];

							if (file != null && !file.isEmpty()) {
								try {
									if (existingQualification != null
											&& existingQualification.getOthersDocumentScan() != null) {
										Path oldDocumentPath = Paths.get(uplaodDirectory,
												existingQualification.getOthersDocumentScan());
										Files.deleteIfExists(oldDocumentPath);
									}

									String originalFileName = file.getOriginalFilename();
									String fileNameWithUniqueId = employeeId + "-" + originalFileName;
									Path newDocumentPath = Paths.get(uplaodDirectory, fileNameWithUniqueId);
									Files.write(newDocumentPath, file.getBytes());

									newQualificationToAdd.setOthersDocumentScan(fileNameWithUniqueId);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
						existingOthersQualifications.add(newQualificationToAdd);
					}
				}

				existingPersonalInfo.setOthersQualifications(existingOthersQualifications);
			}

			List<ProfessionalQualification> existingProfessionalQualifications = existingPersonalInfo
					.getProfessionalQualifications();

			if (personalInfo.getProfessionalQualifications() != null) {
				List<ProfessionalQualification> newProfessionalQualifications = personalInfo
						.getProfessionalQualifications();

				Map<String, ProfessionalQualification> existingProfessionalsMap = new HashMap<>();
				for (ProfessionalQualification existingQualification : existingProfessionalQualifications) {
					String key = generateUniqueKey(existingQualification);
					existingProfessionalsMap.put(key, existingQualification);
				}
				for (int i = 0; i < newProfessionalQualifications.size(); i++) {
					ProfessionalQualification newQualification = newProfessionalQualifications.get(i);
					String key = generateUniqueKey(newQualification);

					ProfessionalQualification existingQualification = existingProfessionalsMap.get(key);

					if (existingQualification != null
							&& existingQualification.getId().equals(newQualification.getId())) {
						updateExistingQualification(existingQualification, newQualification);
						continue;
					} else {
						ProfessionalQualification newQualificationToAdd = addProfessionalQualification(newQualification,
								existingPersonalInfo);

						if (degreeScan != null && i < degreeScan.length) {
							MultipartFile file = degreeScan[i];

							if (file != null && !file.isEmpty()) {
								try {
									if (existingQualification != null
											&& existingQualification.getDegreeScan() != null) {
										Path oldDocumentPath = Paths.get(uplaodDirectory,
												existingQualification.getDegreeScan());
										Files.deleteIfExists(oldDocumentPath);
									}

									String originalFileName = file.getOriginalFilename();
									String fileNameWithUniqueId = employeeId + "-" + originalFileName;
									Path newDocumentPath = Paths.get(uplaodDirectory, fileNameWithUniqueId);
									Files.write(newDocumentPath, file.getBytes());

									newQualificationToAdd.setDegreeScan(fileNameWithUniqueId);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}

						existingProfessionalQualifications.add(newQualificationToAdd);
					}
				}

				existingPersonalInfo.setProfessionalQualifications(existingProfessionalQualifications);

			}

			List<EmpAchievement> existingEmpAchievements = existingPersonalInfo.getEmpAchievements();

			if (personalInfo.getEmpAchievements() != null) {
				List<EmpAchievement> newEmpAchievements = personalInfo.getEmpAchievements();

				Map<String, EmpAchievement> existingAchievementMap = new HashMap<>();
				for (EmpAchievement existingEmpAchievement : existingEmpAchievements) {
					String key = generateUniqueKey(existingEmpAchievement);

					existingAchievementMap.put(key, existingEmpAchievement);
				}

				for (int i = 0; i < newEmpAchievements.size(); i++) {
					EmpAchievement newAchievement = newEmpAchievements.get(i);
					String key = generateUniqueKey(newAchievement);

					EmpAchievement existingAchievemet = existingAchievementMap.get(key);
					if (existingAchievemet != null && existingAchievemet.getId().equals(newAchievement.getId())) {

						updateExistingAchievement(existingAchievemet, newAchievement);
						continue;
					} else {

						EmpAchievement newAchievementToAdd = addEmpAchievement(newAchievement, existingPersonalInfo);

						if (achievementsRewardsDocs != null && i < achievementsRewardsDocs.length) {
							MultipartFile file = achievementsRewardsDocs[i];

							if (file != null && !file.isEmpty()) {
								try {
									if (existingAchievemet != null
											&& existingAchievemet.getAchievementsRewardsDocs() != null) {
										Path oldDocumentPath = Paths.get(uplaodDirectory,
												existingAchievemet.getAchievementsRewardsDocs());
										Files.deleteIfExists(oldDocumentPath);
									}

									String originalFileName = file.getOriginalFilename();
									String fileNameWithUniqueId = employeeId + "-" + originalFileName;
									Path newDocumentPath = Paths.get(uplaodDirectory, fileNameWithUniqueId);
									Files.write(newDocumentPath, file.getBytes());

									newAchievementToAdd.setAchievementsRewardsDocs(fileNameWithUniqueId);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}
						existingEmpAchievements.add(newAchievementToAdd);
					}
				}

				existingPersonalInfo.setOthersQualifications(existingOthersQualifications);
			}

			List<PreviousEmployee> existingPreviousEmployees = existingPersonalInfo.getOldEmployee();
			if (personalInfo.getOldEmployee() != null) {
				List<PreviousEmployee> newPreviousEmployees = personalInfo.getOldEmployee();

				for (int i = 0; i < newPreviousEmployees.size(); i++) {
					PreviousEmployee newPreviousEmployee = newPreviousEmployees.get(i);
					boolean previousExists = false;

					for (PreviousEmployee existingPreviousEmployee : existingPreviousEmployees) {
						if (Objects.equals(existingPreviousEmployee.getPreviousId(),
								newPreviousEmployee.getPreviousId())) {
							existingPreviousEmployee.setCompanyName(newPreviousEmployee.getCompanyName());
							existingPreviousEmployee.setCompanyAddress(newPreviousEmployee.getCompanyAddress());
							existingPreviousEmployee.setDesignation(newPreviousEmployee.getDesignation());
							existingPreviousEmployee.setDescription(newPreviousEmployee.getDescription());
							existingPreviousEmployee.setDateFrom(newPreviousEmployee.getDateFrom());
							existingPreviousEmployee.setDateTo(newPreviousEmployee.getDateTo());
							existingPreviousEmployee.setPreviousHRContact(newPreviousEmployee.getPreviousHRContact());
							existingPreviousEmployee.setPreviousHRName(newPreviousEmployee.getPreviousHRName());
							existingPreviousEmployee
									.setPreviousManagerContact(newPreviousEmployee.getPreviousManagerContact());
							existingPreviousEmployee
									.setPreviousManagerName(newPreviousEmployee.getPreviousManagerName());
							existingPreviousEmployee
									.setLastWithdrawnSalary(newPreviousEmployee.getLastWithdrawnSalary());
							existingPreviousEmployee
									.setPreviousManagerPhoneCode(newPreviousEmployee.getPreviousManagerPhoneCode());
							existingPreviousEmployee
									.setPreviousHRPhoneCode(newPreviousEmployee.getPreviousHRPhoneCode());
							existingPreviousEmployee.setPersonalinfo(existingPersonalInfo);

							if (payslipScan != null && !payslipScan.isEmpty()) {
								try {
									if (existingPreviousEmployee.getPayslipScan() != null) {
										Path oldpayslipScanPath = Paths.get(uplaodDirectory,
												existingPreviousEmployee.getPayslipScan());
										Files.deleteIfExists(oldpayslipScanPath);
									}
									String payslipScanoriginalFileName = payslipScan.getOriginalFilename();
									String payslipScanfileNameWithUniqueIdentifier = employeeId + "-"
											+ payslipScanoriginalFileName;
									Path payslipScanNameWithData = Paths.get(uplaodDirectory,
											payslipScanfileNameWithUniqueIdentifier);
									Files.write(payslipScanNameWithData, payslipScan.getBytes());
									existingPreviousEmployee.setPayslipScan(payslipScanfileNameWithUniqueIdentifier);
								} catch (IOException e) {
									e.printStackTrace();
								}

							}

						}
						previousExists = true;
						break;

					}
					if (!previousExists) {
						PreviousEmployee newDataPreviousEmployee = new PreviousEmployee();
						newDataPreviousEmployee.setCompanyName(newPreviousEmployee.getCompanyName());
						newDataPreviousEmployee.setCompanyAddress(newPreviousEmployee.getCompanyAddress());
						newDataPreviousEmployee.setDesignation(newPreviousEmployee.getDesignation());
						newDataPreviousEmployee.setDescription(newPreviousEmployee.getDescription());
						newDataPreviousEmployee.setDateFrom(newPreviousEmployee.getDateFrom());
						newDataPreviousEmployee.setDateTo(newPreviousEmployee.getDateTo());
						newDataPreviousEmployee.setPreviousHRContact(newPreviousEmployee.getPreviousHRContact());
						newDataPreviousEmployee.setPreviousHRName(newPreviousEmployee.getPreviousHRName());
						newDataPreviousEmployee
								.setPreviousManagerContact(newPreviousEmployee.getPreviousManagerContact());
						newDataPreviousEmployee.setPreviousManagerName(newPreviousEmployee.getPreviousManagerName());
						newDataPreviousEmployee.setLastWithdrawnSalary(newPreviousEmployee.getLastWithdrawnSalary());
						newDataPreviousEmployee
								.setPreviousManagerPhoneCode(newPreviousEmployee.getPreviousManagerPhoneCode());
						newDataPreviousEmployee.setPreviousHRPhoneCode(newPreviousEmployee.getPreviousHRPhoneCode());
						newDataPreviousEmployee.setPersonalinfo(existingPersonalInfo);

						if (payslipScan != null && !payslipScan.isEmpty()) {
							try {
								if (newDataPreviousEmployee.getPayslipScan() != null) {
									Path oldpayslipScanPath = Paths.get(uplaodDirectory,
											newDataPreviousEmployee.getPayslipScan());
									Files.deleteIfExists(oldpayslipScanPath);
								}

								String payslipScanoriginalFileName = payslipScan.getOriginalFilename();
								String payslipScanfileNameWithUniqueIdentifier = employeeId + "-"
										+ payslipScanoriginalFileName;
								Path payslipScanNameWithData = Paths.get(uplaodDirectory,
										payslipScanfileNameWithUniqueIdentifier);
								Files.write(payslipScanNameWithData, payslipScan.getBytes());
								newDataPreviousEmployee.setPayslipScan(payslipScanfileNameWithUniqueIdentifier);
							} catch (IOException e) {
								e.printStackTrace();
							}

						}

						existingPreviousEmployees.add(newDataPreviousEmployee);
					}
				}

				existingPersonalInfo.setOldEmployee(existingPreviousEmployees);
			}

			List<Trainingdetails> trainingdetails = existingPersonalInfo.getTraining();

			if (personalInfo.getTraining() != null) {
				List<Trainingdetails> newTrainingdetails = personalInfo.getTraining();

				for (int i = 0; i < newTrainingdetails.size(); i++) {
					Trainingdetails newTrainingdetail = newTrainingdetails.get(i);

					boolean trainingExists = false;

					for (Trainingdetails existingTrainingdetail : trainingdetails) {
						if (Objects.equals(existingTrainingdetail.getId(), newTrainingdetail.getId())) {
							existingTrainingdetail.setInHouseOutsource(newTrainingdetail.getInHouseOutsource());
							existingTrainingdetail.setPaidUnpaid(newTrainingdetail.getPaidUnpaid());
							existingTrainingdetail.setTrainingType(newTrainingdetail.getTrainingType());
							existingTrainingdetail.setTrainerName(newTrainingdetail.getTrainerName());
							existingTrainingdetail.setTrainerPost(newTrainingdetail.getTrainerPost());
							existingTrainingdetail.setTrainerPhoneCode(newTrainingdetail.getTrainerPhoneCode());
							existingTrainingdetail.setTrainerPhoneNo(newTrainingdetail.getTrainerPhoneNo());
							existingTrainingdetail.setTrainerDepartment(newTrainingdetail.getTrainerDepartment());
							existingTrainingdetail.setTrainerFeedback(newTrainingdetail.getTrainerFeedback());
							existingTrainingdetail.setTrainingStartDate(newTrainingdetail.getTrainingStartDate());
							existingTrainingdetail.setTrainingEndDate(newTrainingdetail.getTrainingEndDate());
							existingTrainingdetail.setPersonalinfo(existingPersonalInfo);

							if (CertificateUploadedForOutsource != null && !CertificateUploadedForOutsource.isEmpty()) {
								try {
									if (existingTrainingdetail.getCertificateUploadedForOutsource() != null) {
										Path oldCertificateUploadedForOutsourcePath = Paths.get(uplaodDirectory,
												existingTrainingdetail.getCertificateUploadedForOutsource());
										Files.deleteIfExists(oldCertificateUploadedForOutsourcePath);
									}

									String certificateUploadedForOutsourceOriginalFileName = CertificateUploadedForOutsource
											.getOriginalFilename();
									String certificateUploadedForOutsourceFileNameWithUniqueIdentifier = employeeId
											+ "-" + certificateUploadedForOutsourceOriginalFileName;
									Path certificateUploadedForOutsourceNameWithData = Paths.get(uplaodDirectory,
											certificateUploadedForOutsourceFileNameWithUniqueIdentifier);
									Files.write(certificateUploadedForOutsourceNameWithData,
											CertificateUploadedForOutsource.getBytes());
									existingTrainingdetail.setCertificateUploadedForOutsource(
											certificateUploadedForOutsourceFileNameWithUniqueIdentifier);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}

							if (PaidTrainingDocumentProof != null && !PaidTrainingDocumentProof.isEmpty()) {
								try {
									if (existingTrainingdetail.getPaidTrainingDocumentProof() != null) {
										Path oldPaidTrainingDocumentProofPath = Paths.get(uplaodDirectory,
												existingTrainingdetail.getPaidTrainingDocumentProof());
										Files.deleteIfExists(oldPaidTrainingDocumentProofPath);
									}

									String paidTrainingDocumentProofOriginalFileName = PaidTrainingDocumentProof
											.getOriginalFilename();
									String paidTrainingDocumentProoffileNameWithUniqueIdentifier = employeeId + "-"
											+ paidTrainingDocumentProofOriginalFileName;
									Path paidTrainingDocumentProofNameWithData = Paths.get(uplaodDirectory,
											paidTrainingDocumentProoffileNameWithUniqueIdentifier);
									Files.write(paidTrainingDocumentProofNameWithData,
											PaidTrainingDocumentProof.getBytes());
									existingTrainingdetail.setPaidTrainingDocumentProof(
											paidTrainingDocumentProoffileNameWithUniqueIdentifier);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}

							trainingExists = true;
							break;
						}
					}

					if (!trainingExists) {
						Trainingdetails newTrainingdetailInstance = new Trainingdetails();
						newTrainingdetailInstance.setInHouseOutsource(newTrainingdetail.getInHouseOutsource());
						newTrainingdetailInstance.setPaidUnpaid(newTrainingdetail.getPaidUnpaid());
						newTrainingdetailInstance.setTrainingType(newTrainingdetail.getTrainingType());
						newTrainingdetailInstance.setTrainerName(newTrainingdetail.getTrainerName());
						newTrainingdetailInstance.setTrainerPost(newTrainingdetail.getTrainerPost());
						newTrainingdetailInstance.setTrainerPhoneCode(newTrainingdetail.getTrainerPhoneCode());
						newTrainingdetailInstance.setTrainerPhoneNo(newTrainingdetail.getTrainerPhoneNo());
						newTrainingdetailInstance.setTrainerDepartment(newTrainingdetail.getTrainerDepartment());
						newTrainingdetailInstance.setTrainerFeedback(newTrainingdetail.getTrainerFeedback());
						newTrainingdetailInstance.setTrainingStartDate(newTrainingdetail.getTrainingStartDate());
						newTrainingdetailInstance.setTrainingEndDate(newTrainingdetail.getTrainingEndDate());
						newTrainingdetailInstance.setPersonalinfo(existingPersonalInfo);

						if (CertificateUploadedForOutsource != null && !CertificateUploadedForOutsource.isEmpty()) {
							try {
								String certificateUploadedForOutsourceOriginalFileName = CertificateUploadedForOutsource
										.getOriginalFilename();
								String certificateUploadedForOutsourceFileNameWithUniqueIdentifier = employeeId + "-"
										+ certificateUploadedForOutsourceOriginalFileName;
								Path certificateUploadedForOutsourceNameWithData = Paths.get(uplaodDirectory,
										certificateUploadedForOutsourceFileNameWithUniqueIdentifier);
								Files.write(certificateUploadedForOutsourceNameWithData,
										CertificateUploadedForOutsource.getBytes());
								newTrainingdetailInstance.setCertificateUploadedForOutsource(
										certificateUploadedForOutsourceFileNameWithUniqueIdentifier);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

						if (PaidTrainingDocumentProof != null && !PaidTrainingDocumentProof.isEmpty()) {
							try {
								String paidTrainingDocumentProofOriginalFileName = PaidTrainingDocumentProof
										.getOriginalFilename();
								String paidTrainingDocumentProoffileNameWithUniqueIdentifier = employeeId + "-"
										+ paidTrainingDocumentProofOriginalFileName;
								Path paidTrainingDocumentProofNameWithData = Paths.get(uplaodDirectory,
										paidTrainingDocumentProoffileNameWithUniqueIdentifier);
								Files.write(paidTrainingDocumentProofNameWithData,
										PaidTrainingDocumentProof.getBytes());
								newTrainingdetailInstance.setPaidTrainingDocumentProof(
										paidTrainingDocumentProoffileNameWithUniqueIdentifier);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

						trainingdetails.add(newTrainingdetailInstance);
					}
				}

				existingPersonalInfo.setTraining(trainingdetails);
			}

			List<JobDetails> jobDetails = existingPersonalInfo.getJobDetails();

			if (personalInfo.getJobDetails() != null) {
				List<JobDetails> newJobDetails = personalInfo.getJobDetails();

				for (int i = 0; i < newJobDetails.size(); i++) {
					JobDetails newJobDetail = newJobDetails.get(i);

					boolean jobDetailExists = false;

					for (JobDetails existingJobDetails : jobDetails) {
						if (Objects.equals(existingJobDetails.getId(), newJobDetail.getId())) {
							existingJobDetails.setCompanyEmailIdProvided(newJobDetail.getCompanyEmailIdProvided());
							existingJobDetails.setCompanyEmailId(newJobDetail.getCompanyEmailId());
							existingJobDetails.setPostedLocation(newJobDetail.getPostedLocation());
							existingJobDetails.setBasicPay(newJobDetail.getBasicPay());
							existingJobDetails.setHouseRentAllowance(newJobDetail.getHouseRentAllowance());
							existingJobDetails.setHouseRentAmount(newJobDetail.getHouseRentAmount());
							existingJobDetails.setFoodAllowance(newJobDetail.getFoodAllowance());
							existingJobDetails.setFoodAllowanceAmount(newJobDetail.getFoodAllowanceAmount());
							existingJobDetails.setEducationalAllowance(newJobDetail.getEducationalAllowance());
							existingJobDetails
									.setEducationalAllowanceAmount(newJobDetail.getEducationalAllowanceAmount());
							existingJobDetails.setTravellingAllowances(newJobDetail.getTravellingAllowances());
							existingJobDetails
									.setTravellingAllowancesAmount(newJobDetail.getTravellingAllowancesAmount());
							existingJobDetails.setUniformAllowance(newJobDetail.getUniformAllowance());
							existingJobDetails.setUniformAllowanceAmount(newJobDetail.getUniformAllowanceAmount());
							existingJobDetails.setVehicleAllowance(newJobDetail.getVehicleAllowance());
							existingJobDetails.setVehicleAllowanceAmount(newJobDetail.getVehicleAllowanceAmount());
							existingJobDetails.setOtherAllowance(newJobDetail.getOtherAllowance());
							existingJobDetails.setOtherAllowanceAmount(newJobDetail.getOtherAllowanceAmount());
							existingJobDetails.setVehicle(newJobDetail.getVehicle());
							existingJobDetails.setVehicleNumber(newJobDetail.getVehicleNumber());
							existingJobDetails.setVehicleModelName(newJobDetail.getVehicleModelName());
							existingJobDetails.setVehicleModelYear(newJobDetail.getVehicleModelYear());
							existingJobDetails.setIsVehicleNewOrPreowned(newJobDetail.getIsVehicleNewOrPreowned());
							existingJobDetails.setMobileIssuedOrNot(newJobDetail.getMobileIssuedOrNot());
							existingJobDetails.setSimIssuedOrNot(newJobDetail.getSimIssuedOrNot());
							existingJobDetails.setFlightFacilities(newJobDetail.getFlightFacilities());
							existingJobDetails.setHowMuchTime(newJobDetail.getHowMuchTime());
							existingJobDetails.setFamilyTicketsAlsoProvidedOrNot(
									newJobDetail.getFamilyTicketsAlsoProvidedOrNot());
							existingJobDetails.setOthersAccomandation(newJobDetail.getOthersAccomandation());
							existingJobDetails.setHealthInsuranceCoverage(newJobDetail.getHealthInsuranceCoverage());
							existingJobDetails.setMaximumAmountGiven(newJobDetail.getMaximumAmountGiven());
							existingJobDetails.setFamilyHealthInsuranceGivenOrNot(
									newJobDetail.getFamilyHealthInsuranceGivenOrNot());
							existingJobDetails
									.setFamilyHealthInsuranceType(newJobDetail.getFamilyHealthInsuranceType());
							existingJobDetails.setPunchingMachineYesOrNo(newJobDetail.getPunchingMachineYesOrNo());
							existingJobDetails.setJoiningDate(newJobDetail.getJoiningDate());
							existingJobDetails.setAccommodationYesOrNo(newJobDetail.getAccommodationYesOrNo());
							existingJobDetails.setIsShredOrSeparate(newJobDetail.getIsShredOrSeparate());
							existingJobDetails
									.setIsExeutiveOrLabourFacility(newJobDetail.getIsExeutiveOrLabourFacility());
							existingJobDetails
									.setElectricityAllocationYesOrNo(newJobDetail.getElectricityAllocationYesOrNo());
							existingJobDetails
									.setElectricityAllocationAmount(newJobDetail.getElectricityAllocationAmount());
							existingJobDetails.setRentAllocationYesOrNo(newJobDetail.getRentAllocationYesOrNo());
							existingJobDetails.setRentAllocationAmount(newJobDetail.getRentAllocationAmount());
							existingJobDetails.setCashOrChipFacility(newJobDetail.getCashOrChipFacility());
							existingJobDetails.setChipNumber(newJobDetail.getChipNumber());
							existingJobDetails.setReferredBy(newJobDetail.getReferredBy());
							existingJobDetails.setByWhom(newJobDetail.getByWhom());
							existingJobDetails.setCashAmount(newJobDetail.getCashAmount());
							existingJobDetails.setPersonalinfo(existingPersonalInfo);

							jobDetailExists = true;
							break;
						}
					}

					if (!jobDetailExists) {
						JobDetails newJobDetailInstance = new JobDetails();
						newJobDetailInstance.setCompanyEmailIdProvided(newJobDetail.getCompanyEmailIdProvided());
						newJobDetailInstance.setCompanyEmailId(newJobDetail.getCompanyEmailId());
						newJobDetailInstance.setPostedLocation(newJobDetail.getPostedLocation());
						newJobDetailInstance.setBasicPay(newJobDetail.getBasicPay());
						newJobDetailInstance.setHouseRentAllowance(newJobDetail.getHouseRentAllowance());
						newJobDetailInstance.setHouseRentAmount(newJobDetail.getHouseRentAmount());
						newJobDetailInstance.setFoodAllowance(newJobDetail.getFoodAllowance());
						newJobDetailInstance.setFoodAllowanceAmount(newJobDetail.getFoodAllowanceAmount());
						newJobDetailInstance.setEducationalAllowance(newJobDetail.getEducationalAllowance());
						newJobDetailInstance
								.setEducationalAllowanceAmount(newJobDetail.getEducationalAllowanceAmount());
						newJobDetailInstance.setTravellingAllowances(newJobDetail.getTravellingAllowances());
						newJobDetailInstance
								.setTravellingAllowancesAmount(newJobDetail.getTravellingAllowancesAmount());
						newJobDetailInstance.setUniformAllowance(newJobDetail.getUniformAllowance());
						newJobDetailInstance.setUniformAllowanceAmount(newJobDetail.getUniformAllowanceAmount());
						newJobDetailInstance.setVehicleAllowance(newJobDetail.getVehicleAllowance());
						newJobDetailInstance.setVehicleAllowanceAmount(newJobDetail.getVehicleAllowanceAmount());
						newJobDetailInstance.setOtherAllowance(newJobDetail.getOtherAllowance());
						newJobDetailInstance.setOtherAllowanceAmount(newJobDetail.getOtherAllowanceAmount());
						newJobDetailInstance.setVehicle(newJobDetail.getVehicle());
						newJobDetailInstance.setVehicleNumber(newJobDetail.getVehicleNumber());
						newJobDetailInstance.setVehicleModelName(newJobDetail.getVehicleModelName());
						newJobDetailInstance.setVehicleModelYear(newJobDetail.getVehicleModelYear());
						newJobDetailInstance.setIsVehicleNewOrPreowned(newJobDetail.getIsVehicleNewOrPreowned());
						newJobDetailInstance.setMobileIssuedOrNot(newJobDetail.getMobileIssuedOrNot());
						newJobDetailInstance.setSimIssuedOrNot(newJobDetail.getSimIssuedOrNot());
						newJobDetailInstance.setFlightFacilities(newJobDetail.getFlightFacilities());
						newJobDetailInstance.setHowMuchTime(newJobDetail.getHowMuchTime());
						newJobDetailInstance
								.setFamilyTicketsAlsoProvidedOrNot(newJobDetail.getFamilyTicketsAlsoProvidedOrNot());
						newJobDetailInstance.setOthersAccomandation(newJobDetail.getOthersAccomandation());
						newJobDetailInstance.setHealthInsuranceCoverage(newJobDetail.getHealthInsuranceCoverage());
						newJobDetailInstance.setMaximumAmountGiven(newJobDetail.getMaximumAmountGiven());
						newJobDetailInstance
								.setFamilyHealthInsuranceGivenOrNot(newJobDetail.getFamilyHealthInsuranceGivenOrNot());
						newJobDetailInstance.setFamilyHealthInsuranceType(newJobDetail.getFamilyHealthInsuranceType());
						newJobDetailInstance.setPunchingMachineYesOrNo(newJobDetail.getPunchingMachineYesOrNo());
						newJobDetailInstance.setJoiningDate(newJobDetail.getJoiningDate());
						newJobDetailInstance.setAccommodationYesOrNo(newJobDetail.getAccommodationYesOrNo());
						newJobDetailInstance.setIsShredOrSeparate(newJobDetail.getIsShredOrSeparate());
						newJobDetailInstance
								.setIsExeutiveOrLabourFacility(newJobDetail.getIsExeutiveOrLabourFacility());
						newJobDetailInstance
								.setElectricityAllocationYesOrNo(newJobDetail.getElectricityAllocationYesOrNo());
						newJobDetailInstance
								.setElectricityAllocationAmount(newJobDetail.getElectricityAllocationAmount());
						newJobDetailInstance.setRentAllocationYesOrNo(newJobDetail.getRentAllocationYesOrNo());
						newJobDetailInstance.setRentAllocationAmount(newJobDetail.getRentAllocationAmount());
						newJobDetailInstance.setCashOrChipFacility(newJobDetail.getCashOrChipFacility());
						newJobDetailInstance.setChipNumber(newJobDetail.getChipNumber());
						newJobDetailInstance.setReferredBy(newJobDetail.getReferredBy());
						newJobDetailInstance.setByWhom(newJobDetail.getByWhom());
						newJobDetailInstance.setCashAmount(newJobDetail.getCashAmount());
						newJobDetailInstance.setPersonalinfo(existingPersonalInfo);

						jobDetails.add(newJobDetailInstance);
					}
				}

				existingPersonalInfo.setJobDetails(jobDetails);
			}
			Department newDepartment = new Department();
			if (personalInfo.getDepartment() != null) {
				newDepartment.setDepartmentId(personalInfo.getDepartment().getDepartmentId());
				newDepartment.setDepartmentName(personalInfo.getDepartment().getDepartmentName());

				existingPersonalInfo.setDepartment(newDepartment);
			}

			BackgroundCheck bgcheck = existingPersonalInfo.getBgcheck();
			if (bgcheck == null) {
				bgcheck = new BackgroundCheck();
				existingPersonalInfo.setBgcheck(bgcheck);
			}

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

				if (bgcheck.getRecordsheet() != null) {
					Path oldrecordsheetPath = Paths.get(uplaodDirectory, bgcheck.getRecordsheet());
					Files.deleteIfExists(oldrecordsheetPath);
				}

				String recordsheetoriginalFileName = recordsheet.getOriginalFilename();
				String recordsheetfileNameWithUniqueIdentifier = employeeId + "-" + recordsheetoriginalFileName;
				Path recordsheetNameWithData = Paths.get(uplaodDirectory, recordsheetfileNameWithUniqueIdentifier);
				Files.write(recordsheetNameWithData, recordsheet.getBytes());
				bgcheck.setRecordsheet(recordsheetfileNameWithUniqueIdentifier);
			}
			if (declarationRequired != null && !declarationRequired.isEmpty()) {

				if (bgcheck.getDeclarationRequired() != null) {
					Path olddeclarationRequiredPath = Paths.get(uplaodDirectory, bgcheck.getDeclarationRequired());
					Files.deleteIfExists(olddeclarationRequiredPath);
				}

				String declarationRequiredoriginalFileName = declarationRequired.getOriginalFilename();
				String declarationRequiredfileNameWithUniqueIdentifier = employeeId + "-"
						+ declarationRequiredoriginalFileName;
				Path declarationRequiredNameWithData = Paths.get(uplaodDirectory,
						declarationRequiredfileNameWithUniqueIdentifier);
				Files.write(declarationRequiredNameWithData, declarationRequired.getBytes());
				bgcheck.setDeclarationRequired(declarationRequiredfileNameWithUniqueIdentifier);
			}
			existingPersonalInfo.setBgcheck(bgcheck);

		} catch (Exception e) {
			throw new RuntimeException("Something went wrong: " + e.getMessage());
		}

		PersonalInfo updatePersonalInfo = dao.updatePersonalInfo(email, existingPersonalInfo);
		return updatePersonalInfo;

	}

	private String generateUniqueKey(OthersQualification qualification) {
		return String.format("%s", qualification.getId());
	}

	private void updateExistingQualification(OthersQualification existingQualification,
			OthersQualification newQualification) {
		existingQualification.setId(newQualification.getId());
		existingQualification.setOthers(newQualification.getOthers());
		existingQualification.setOthersIssuingAuthority(newQualification.getOthersIssuingAuthority());
		existingQualification.setOthersMarksOrGrade(newQualification.getOthersMarksOrGrade());
		existingQualification.setOthersYear(newQualification.getOthersYear());
	}

	private OthersQualification addOthersQualification(OthersQualification newQualification,
			PersonalInfo existingPersonalInfo) {
		OthersQualification newOthersQualification = new OthersQualification();

		newOthersQualification.setOthers(newQualification.getOthers());
		newOthersQualification.setOthersIssuingAuthority(newQualification.getOthersIssuingAuthority());
		newOthersQualification.setOthersMarksOrGrade(newQualification.getOthersMarksOrGrade());
		newOthersQualification.setOthersYear(newQualification.getOthersYear());
		newOthersQualification.setPersonalinfo(existingPersonalInfo);

		return newOthersQualification;
	}

	private String generateUniqueKey(ProfessionalQualification professionalQualification) {
		return String.format("%s", professionalQualification.getId());
	}

	private void updateExistingQualification(ProfessionalQualification existingProfessionalQualification,
			ProfessionalQualification newProfessionalQualification) {
		existingProfessionalQualification.setId(newProfessionalQualification.getId());
		existingProfessionalQualification.setQualification(newProfessionalQualification.getQualification());
		existingProfessionalQualification.setIssuingAuthority(newProfessionalQualification.getIssuingAuthority());
		existingProfessionalQualification.setGrade(newProfessionalQualification.getGrade());
		existingProfessionalQualification.setYearOfQualification(newProfessionalQualification.getYearOfQualification());
		existingProfessionalQualification.setGradingSystem(newProfessionalQualification.getGradingSystem());
	}

	private ProfessionalQualification addProfessionalQualification(
			ProfessionalQualification newProfessionalQualification, PersonalInfo existingPersonalInfo) {
		ProfessionalQualification newProfessionalQualifications = new ProfessionalQualification();

		newProfessionalQualifications.setQualification(newProfessionalQualification.getQualification());
		newProfessionalQualifications.setIssuingAuthority(newProfessionalQualification.getIssuingAuthority());
		newProfessionalQualifications.setGrade(newProfessionalQualification.getGrade());
		newProfessionalQualifications.setYearOfQualification(newProfessionalQualification.getYearOfQualification());
		newProfessionalQualifications.setGradingSystem(newProfessionalQualification.getGradingSystem());
		newProfessionalQualifications.setPersonalinfo(existingPersonalInfo);

		return newProfessionalQualifications;
	}

	private String generateUniqueKey(EmpAchievement achievement) {
		return String.format("%s", achievement.getId());
	}

	private void updateExistingAchievement(EmpAchievement existingEmpAchievement, EmpAchievement newEmpAchievement) {
		existingEmpAchievement.setId(newEmpAchievement.getId());
		existingEmpAchievement.setAchievementRewardsName(newEmpAchievement.getAchievementRewardsName());

	}

	private EmpAchievement addEmpAchievement(EmpAchievement newEmpAchievement, PersonalInfo existingPersonalInfo) {
		EmpAchievement newEmpAchievements = new EmpAchievement();

		newEmpAchievements.setAchievementRewardsName(newEmpAchievement.getAchievementRewardsName());

		newEmpAchievements.setPersonalinfo(existingPersonalInfo);

		return newEmpAchievements;
	}
}