//package com.erp.hrms.test;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.erp.hrms.api.dao.IPersonalInfoDAO;
//import com.erp.hrms.api.service.PersonalInfoServiceImpl;
//import com.erp.hrms.entity.BackgroundCheck;
//import com.erp.hrms.entity.BloodRelative;
//import com.erp.hrms.entity.DrivingLicense;
//import com.erp.hrms.entity.Education;
//import com.erp.hrms.entity.EmpAchievement;
//import com.erp.hrms.entity.JobDetails;
//import com.erp.hrms.entity.OthersQualification;
//import com.erp.hrms.entity.PassportDetails;
//import com.erp.hrms.entity.PersonalInfo;
//import com.erp.hrms.entity.PreviousEmployee;
//import com.erp.hrms.entity.ProfessionalQualification;
//import com.erp.hrms.entity.Trainingdetails;
//import com.erp.hrms.entity.VisaDetail;
//import com.erp.hrms.exception.PersonalInfoNotFoundException;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//class PersonalInfoServiceTest {
//
//	@Mock
//	private IPersonalInfoDAO dao;
//
//	@Mock
//	private JavaMailSender mailSender;
//
//	@InjectMocks
//	private PersonalInfoServiceImpl service;
//
//	@BeforeEach
//	void setUp() {
//		MockitoAnnotations.openMocks(this);
//	}
//
//	@Test
//	void testSavedata() throws Exception {
//		// Prepare test data
//		String personalinfo = "{\r\n" + "\"namePrefix\" : \"mr\",\r\n" + "  \"firstName\": \"shahzeb a\",\r\n"
//				+ "  \"lastName\": \"khan\",\r\n" + "  \"middleName\": \"Smith\",\r\n"
//				+ "  \"fathersFirstName\": \"Michael\",\r\n" + "  \"fathersMiddleName\": \"William\",\r\n"
//				+ "  \"fathersLastName\": \"Doe\",\r\n" + "  \"dateOfBirth\": \"20-01-1093\",\r\n"
//				+ "  \"age\": 12 ,\r\n" + "  \"maritalStatus\": \"Single\",\r\n" + "\"phoneCode\" : \"+91\",\r\n"
//				+ "  \"personalContactNo\": \"1234567890\",\r\n" + "  \"email\" : \"Mustaufah@gmail.com\",\r\n"
//				+ "  \"citizenship\": \"USA\",\r\n" + " \r\n" + "  \"permanentResidenceCountry\": \"USA\",\r\n"
//				+ "  \"permanentResidentialAddress\": \"123 Main Street, City, State, Country\",\r\n"
//				+ "  \"bloodGroup\": \"A+\",\r\n" + "  \"workedInUAE\": \"yes\",\r\n"
//				+ "  \"emiratesID\": \"1234567890\",\r\n" + "  \"degreeAttestation\": \"yes\",\r\n"
//				+ "  \"hobbies\": \"Reading, Traveling\",\r\n" + "  \"psDetail\": {\r\n"
//				+ "    \"passportIssuingCountry\": \"USA\",\r\n" + "    \"passportNumber\": \"123456789\",\r\n"
//				+ "    \"passportExpiryDate\": \"10-07-2025\"\r\n" + "  },\r\n" + "  \"license\": {\r\n"
//				+ "    \"drivinglicense\": false,\r\n" + "    \r\n" + "    \"ownvehicle\": true\r\n" + "  },\r\n"
//				+ "  \"relative\": {\r\n" + "\"relativenamePrefix\" : \"Mr\",    \r\n" + "\"rfirstname\": \"Jane\",\r\n"
//				+ "    \"rmiddlename\": \"Doe\",\r\n" + "    \"rlastname\": \"Smith\",\r\n"
//				+ "    \"relationship\": \"Sibling\",\r\n"
//				+ "    \"raddress\": \"456 Oak Street, City, State, Country\",\r\n" + "\"rphoneCode\" : \"+91\",\r\n"
//				+ "    \"rcontactno\": \"9876543210\"\r\n" + "  },\r\n" + "  \"visainfo\": {\r\n"
//				+ "    \"visaType\": \"Visa\",\r\n" + "    \"workVisaEmirateId\": \"0987654321\",\r\n" + "    \r\n"
//				+ "\"categoryOfVisa\" : \"visa\",\r\n" + "    \"siGlobalWorkVisaCompany\": \"ACME Corp\",\r\n"
//				+ "    \"visaIssueyDate\": \"20-01-2010\",\r\n" + "    \"visaExpiryDate\": \"2024-07-19\"\r\n"
//				+ "  },\r\n" + " \"bgcheck\"   : {\r\n" + "     \"status\" : \"india \",\r\n"
//				+ "     \"doneBy\" : \"done\",\r\n" + "\"internalConcernedManager\" : \"llll\",\r\n"
//				+ "\"managerApproval\" : \"pppp\",\r\n" + "\"managerName\" : \"rahil\",\r\n"
//				+ "\"remark\" : \"good\",\r\n" + "\"addressVerified\" : \"oooo\",\r\n"
//				+ "\"previousEmploymentStatusVerified\" : \"eeeee\",\r\n"
//				+ "\"previousDesignationAndResponsibilityVerified\"  : \"uuuuuuu\",\r\n"
//				+ "\"idProofDocumentVerified\" : \"Verified\",\r\n"
//				+ "\"educationalQualificationVerified\" : \"errrrrrr\",\r\n" + "\"criminalRecords\" : \"tttt\",\r\n"
//				+ "\"punishmentForImprisonmentApproval\" : \"Approval\",\r\n"
//				+ "\"externalName\": \"externalName\",\r\n" + "	\"externalPost\" : \"externalPost\",\r\n"
//				+ "	\"externalCompanyName\" :\"externalCompanyName\",\r\n"
//				+ "	\"externalPhoneCode\": \"externalPhoneCode\",\r\n"
//				+ "	\"externalPhoneNo\" : \"externalPhoneNo\"\r\n" + " },\r\n" + " \"educations\" : [{\r\n"
//				+ "  \"secondaryIssuingAuthority\": \"10\",\r\n" + "  \"secondarymarksOrGrade\": \"90\",\r\n"
//				+ "  \"secondaryyear\": \"2010\",     \r\n" + "  \"seniorSecondaryIssuingAuthority\": \"Bhopal 2\",\r\n"
//				+ "  \"seniorSecondaryMarksOrGrade\": \"80\",\r\n" + "  \"seniorSecondaryYear\": \"2011\",\r\n"
//				+ "  \"graduationIssuingAuthority\": \"bhopal 3\",\r\n" + "  \"graduationMarksOrGrade\": \"70\",\r\n"
//				+ "  \"graduationYear\": \"2012\",\r\n" + "  \"postGraduationIssuingAuthority\": \"Bhopal 4\",\r\n"
//				+ "  \"postGraduationMarksOrGrade\": \"60\",\r\n" + "  \"postGraduationYear\": \"2013\",\r\n"
//				+ "\"diplomaIssuingAuthority\": \"diplomaIssuingAuthority\",\r\n"
//				+ "\"diplomaMarksOrGrade\" : \"diplomaMarksOrGrade\",\r\n" + "\"diplomaYear\":\"diplomaYear\"\r\n"
//				+ "}\r\n" + "],\r\n" + "\"othersQualifications\" : [{\r\n" + "\"others\" : \"other\",\r\n"
//				+ " \"othersIssuingAuthority\": \"bhopal \",\r\n" + "  \"othersMarksOrGrade\": \"5\",\r\n"
//				+ "  \"othersYear\": \"2010\"\r\n" + "},\r\n" + "{\r\n" + "\"others\" : \"new other\",\r\n"
//				+ " \"othersIssuingAuthority\": \"bhopal 5\",\r\n" + "  \"othersMarksOrGrade\": \"50\",\r\n"
//				+ "  \"othersYear\": \"2014\"\r\n" + "}\r\n" + "],\r\n" + " \"training\" : [{\r\n"
//				+ "     \"trainingType\" : \"lll\",\r\n" + "     \"inHouseOutsource\" : \"aaaa\",\r\n"
//				+ "     \"paidUnpaid\" : \"hhh\",\r\n" + "\"trainingStartDate\" :\"32223\",\r\n"
//				+ "\"trainingEndDate\" : \"wwww\",\r\n" + "\"trainerFeedback\" : \"jjjj\",\r\n"
//				+ "\"trainerName\": \"trainerName\",\r\n" + "	\"trainerPost\" : \"trainerPost\",\r\n"
//				+ "	\"trainerDepartment\":\"trainerDepartment\",\r\n"
//				+ "	\"trainerPhoneCode\" :\"trainerPhoneCode\",\r\n"
//				+ "	\"trainerPhoneNo\" : \"trainerPhoneNoString\"\r\n" + " }], \r\n" + " \"oldEmployee\" : [{\r\n"
//				+ "     \"companyName\" : \"aibs\",\r\n" + "     \"companyAddress\" : \"bhopal\",\r\n"
//				+ "	\"designation\" : \"aaa\",\r\n" + "	\"description\" : \"DDD\",\r\n"
//				+ "	\"dateFrom\" : \"20-02-2016\",\r\n" + "	\"dateTo\": \"20-02-2030\",\r\n"
//				+ "\"previousManagerName\" : \"SSSSS\",\r\n" + "\"previousManagerPhoneCode\" : \"91\",\r\n"
//				+ "\"previousManagerContact\" : \"09876543\",\r\n" + "\"previousHRName\" : \"ali\",\r\n"
//				+ "\"previousHRPhoneCode\": \"91\",\r\n" + "\"previousHRContact\" : \"dddd\",\r\n"
//				+ "\"lastWithdrawnSalary\" : 900001,\r\n" + "\"empAchievements\" : [{\r\n"
//				+ "\"achievementRewardsName\" : \"Best of employee of the year\"\r\n" + "},\r\n" + "{\r\n"
//				+ "\"achievementRewardsName\" : \"Best of employee of the month\"\r\n" + "}\r\n" + "]\r\n" + "}\r\n"
//				+ "],\r\n" + " \"professionalQualifications\" :[{\r\n" + "     \"qualification\" : \"MBA \" ,\r\n"
//				+ "     \"issuingAuthority\" : \"bhopal \",\r\n" + "     \"gradingSystem\" : \"dddd \",\r\n"
//				+ "     \"yearOfQualification\" : \"2010 \",\r\n" + "     \"grade\" : \"B+\"\r\n" + "     \r\n"
//				+ " },\r\n" + "{\r\n" + "     \"qualification\" : \"MBA 2\" ,\r\n"
//				+ "     \"issuingAuthority\" : \"bhopal 2\",\r\n" + "     \"gradingSystem\" : \"dddd 2\",\r\n"
//				+ "     \"yearOfQualification\" : \"2010 2\",\r\n" + "     \"grade\" : \"B+ 2\"\r\n" + "     \r\n"
//				+ " }\r\n" + "],\r\n" + "\"jobDetails\" : [{\r\n" + " \r\n" + "  \"jobPostDesignation\": \"job\",\r\n"
//				+ "  \"companyEmailIdProvided\": \"job\",\r\n" + "  \"companyEmailId\": \"job\",\r\n"
//				+ "  \"jobLevel\": \"job\",\r\n" + "  \"postedLocation\": \"job\",\r\n"
//				+ "  \"basicAllowance\": \"job\",\r\n" + "  \"houseRentAllowance\": \"job\",\r\n"
//				+ "  \"houseRentAmount\": \"job\",\r\n" + "  \"foodAllowance\": \"job\",\r\n"
//				+ "  \"foodAllowanceAmount\": \"job\",\r\n" + "  \"vehicleAllowance\": \"job\",\r\n"
//				+ "  \"vehicleAllowanceAmount\": \"job\",\r\n" + "  \"uniformAllowance\": \"job\",\r\n"
//				+ "  \"uniformAllowanceAmount\": \"job\",\r\n" + "  \"travellingAllowances\": \"job\",\r\n"
//				+ "  \"travellingAllowancesAmount\": \"job\",\r\n" + "  \"educationalAllowance\": \"job\",\r\n"
//				+ "  \"educationalAllowanceAmount\": \"job\",\r\n" + "  \"otherAllowance\": \"job\",\r\n"
//				+ "  \"otherAllowanceAmount\": \"job\",\r\n" + "  \"vehicle\": \"job\",\r\n"
//				+ "  \"vehicleNumber\": \"job\",\r\n" + "  \"vehicleModelName\": \"job\",\r\n"
//				+ "  \"vehicleModelYear\": \"job\",\r\n" + "  \"isVehicleNewOrPreowned\": \"job\",\r\n" + "  \r\n"
//				+ "  \"accommodationYesOrNo\": \"job\",\r\n" + "  \"isShredOrSeparate\": \"job\",\r\n"
//				+ "  \"isExeutiveOrLabourFacility\": \"job\",\r\n" + "  \"electricityAllocationYesOrNo\": \"job\",\r\n"
//				+ "  \"electricityAllocationAmount\": \"job\",\r\n" + "  \"rentAllocationYesOrNo\": \"job\",\r\n"
//				+ "  \"rentAllocationAmount\": \"job\",\r\n" + "  \"mobileIssuedOrNot\": \"job\",\r\n"
//				+ "  \"simIssuedOrNot\": \"job\",\r\n" + "  \"flightFacilities\": \"job\",\r\n"
//				+ "  \"howMuchTime\": \"job\",\r\n" + "  \"familyTicketsAlsoProvidedOrNot\": \"job\",\r\n"
//				+ "  \"othersAccomandation\": \"job\",\r\n" + "  \"healthInsuranceCoverage\": \"job\",\r\n"
//				+ "  \"maximumAmountGiven\": \"job\",\r\n" + "  \"familyHealthInsuranceGivenOrNot\": \"job\",\r\n"
//				+ "  \"familyHealthInsuranceType\" :\"type\",\r\n" + "  \"punchingMachineYesOrNo\": \"job\",\r\n"
//				+ "  \"joiningDate\": \"job\",\r\n" + "\"jobdepartment\" : \"joib\",\r\n"
//				+ "\"chipNumber\" : \"0987654987\",\r\n" + "\"cashOrChipFacility\": \"yes\",\r\n"
//				+ "\"referredBy\" : \"yes\",\r\n" + "\"byWhom\" : \"mustufa\"\r\n" + "}]\r\n" + "}\r\n";
//
//		MultipartFile passportSizePhoto = mock(MultipartFile.class);
//		MultipartFile otherIdProofDoc = mock(MultipartFile.class);
//		MultipartFile passportScan = mock(MultipartFile.class);
//		MultipartFile licensecopy = mock(MultipartFile.class);
//		MultipartFile relativeid = mock(MultipartFile.class);
//		MultipartFile raddressproof = mock(MultipartFile.class);
//		MultipartFile secondaryDocumentScan = mock(MultipartFile.class);
//		MultipartFile seniorSecondaryDocumentScan = mock(MultipartFile.class);
//		MultipartFile graduationDocumentScan = mock(MultipartFile.class);
//		MultipartFile postGraduationDocumentScan = mock(MultipartFile.class);
//		MultipartFile[] othersDocumentScan = { mock(MultipartFile.class) };
//		MultipartFile[] degreeScan = { mock(MultipartFile.class) };
//		MultipartFile payslipScan = mock(MultipartFile.class);
//		MultipartFile recordsheet = mock(MultipartFile.class);
//		MultipartFile paidTrainingDocumentProof = mock(MultipartFile.class);
//		MultipartFile certificateUploadedForOutsource = mock(MultipartFile.class);
//		MultipartFile visaDocs = mock(MultipartFile.class);
//		MultipartFile diplomaDocumentScan = mock(MultipartFile.class);
//		MultipartFile declarationRequired = mock(MultipartFile.class);
//		MultipartFile[] achievementsRewardsDocs = { mock(MultipartFile.class) };
//		doNothing().when(dao).savePersonalInfo(any(PersonalInfo.class));
//		service.savedata(personalinfo, passportSizePhoto, otherIdProofDoc, passportScan, licensecopy, relativeid,
//				raddressproof, secondaryDocumentScan, seniorSecondaryDocumentScan, graduationDocumentScan,
//				postGraduationDocumentScan, othersDocumentScan, degreeScan, payslipScan, recordsheet,
//				paidTrainingDocumentProof, certificateUploadedForOutsource, visaDocs, diplomaDocumentScan,
//				declarationRequired, achievementsRewardsDocs);
//		verify(dao).savePersonalInfo(any(PersonalInfo.class));
//
//	}
//
//	@Test
//	public void testFindAllPersonalInfo_WithData() {
//		List<PersonalInfo> personalInfoList = new ArrayList<>();
//		personalInfoList.add(new PersonalInfo());
//		when(dao.findAllPersonalInfo()).thenReturn(personalInfoList);
//		List<PersonalInfo> result = service.findAllPersonalInfo();
//		Assertions.assertEquals(personalInfoList, result);
//	}
//
//	@Test
//	void testFindAllPersonalInfo_WithNoData() {
//		when(dao.findAllPersonalInfo()).thenReturn(new ArrayList<>());
//		assertThrows(PersonalInfoNotFoundException.class, () -> service.findAllPersonalInfo());
//	}
//
//	@Test
//	public void testGetPersonalInfoByEmail_WithData() {
//		String email = "example@example.com";
//		PersonalInfo personalInfo = new PersonalInfo();
//		when(dao.getPersonalInfoByEmail(email)).thenReturn(personalInfo);
//		PersonalInfo result = service.getPersonalInfoByEmail(email);
//		Assertions.assertEquals(personalInfo, result);
//	}
//
//	@Test
//	public void testGetPersonalInfoByEmail_WithoutData() {
//		String email = "example@example.com";
//		when(dao.getPersonalInfoByEmail(email)).thenReturn(null);
//		service.getPersonalInfoByEmail(email);
//	}
//
//	@Test
//	public void testGetPersonalInfoByEmployeeId_WithData() {
//		Long employeeId = (long) 1001;
//		PersonalInfo personalInfo = new PersonalInfo();
//		when(dao.getPersonalInfoByEmployeeId(employeeId)).thenReturn(personalInfo);
//		PersonalInfo result = service.getPersonalInfoByEmployeeId(employeeId);
//		Assertions.assertEquals(personalInfo, result);
//	}
//
//	@Test
//	public void testGetPersonalInfoEmployeeId_WithoutData() {
//		Long employeeId = (long) 1001;
//		when(dao.getPersonalInfoByEmployeeId(employeeId)).thenReturn(null);
//		service.getPersonalInfoByEmployeeId(employeeId);
//	}
//
//	 @Test
//	    public void testDeletePersonalInfoByEmail() throws JsonProcessingException, JsonMappingException {
//	        String email = "test@example.com";
//	        String personalInfoJson = "{\"status\":\"Inactive\"}";
//	        PersonalInfo existingPersonalInfo = new PersonalInfo();
//	        existingPersonalInfo.setStatus("Active");
//	        ObjectMapper objectMapper = new ObjectMapper();
//	        PersonalInfo updatedPersonalInfo = objectMapper.readValue(personalInfoJson, PersonalInfo.class);
//	        when(dao.getPersonalInfoByEmailForUpdate(email)).thenReturn(existingPersonalInfo);
//	        when(dao.deletePersonalInfoByEmail(email, existingPersonalInfo)).thenReturn(updatedPersonalInfo);
//	        PersonalInfo result = service.deletePersonalInfoByEmail(email, personalInfoJson);
//	        assertNotNull(result);
//	        assertEquals(updatedPersonalInfo.getStatus(), result.getStatus());
//	    }
//
//	    @Test
//	    public void testDeletePersonalInfoByEmailNotFound() throws JsonProcessingException, JsonMappingException {
//	        String email = "nonexistent@example.com";
//	        String personalInfoJson = "{\"status\":\"Inactive\"}";
//	        when(dao.getPersonalInfoByEmailForUpdate(email)).thenReturn(null);
//	        PersonalInfoNotFoundException exception = assertThrows(
//	            PersonalInfoNotFoundException.class,
//	            () -> service.deletePersonalInfoByEmail(email, personalInfoJson)
//	        );	        
//	        assertEquals("No personal information found for this email ID: " + email, exception.getMessage());
//	    }
//	
//	@Test
//	void testUpdatePersonalInfo() throws IOException {
//		String email = "example@example.com";
//		String personalInfoJson = "{\r\n" + "\"namePrefix\" : \"mr\",\r\n" + "  \"firstName\": \"shahzeb a\",\r\n"
//				+ "  \"lastName\": \"khan\",\r\n" + "  \"middleName\": \"Smith\",\r\n"
//				+ "  \"fathersFirstName\": \"Michael\",\r\n" + "  \"fathersMiddleName\": \"William\",\r\n"
//				+ "  \"fathersLastName\": \"Doe\",\r\n" + "  \"dateOfBirth\": \"20-01-1093\",\r\n"
//				+ "  \"age\": 12 ,\r\n" + "  \"maritalStatus\": \"Single\",\r\n" + "\"phoneCode\" : \"+91\",\r\n"
//				+ "  \"personalContactNo\": \"1234567890\",\r\n" + "  \"email\" : \"Mustaufah@gmail.com\",\r\n"
//				+ "  \"citizenship\": \"USA\",\r\n" + " \r\n" + "  \"permanentResidenceCountry\": \"USA\",\r\n"
//				+ "  \"permanentResidentialAddress\": \"123 Main Street, City, State, Country\",\r\n"
//				+ "  \"bloodGroup\": \"A+\",\r\n" + "  \"workedInUAE\": \"yes\",\r\n"
//				+ "  \"emiratesID\": \"1234567890\",\r\n" + "  \"degreeAttestation\": \"yes\",\r\n"
//				+ "  \"hobbies\": \"Reading, Traveling\",\r\n" + "  \"psDetail\": {\r\n"
//				+ "    \"passportIssuingCountry\": \"USA\",\r\n" + "    \"passportNumber\": \"123456789\",\r\n"
//				+ "    \"passportExpiryDate\": \"10-07-2025\"\r\n" + "  },\r\n" + "  \"license\": {\r\n"
//				+ "    \"drivinglicense\": false,\r\n" + "    \r\n" + "    \"ownvehicle\": true\r\n" + "  },\r\n"
//				+ "  \"relative\": {\r\n" + "\"relativenamePrefix\" : \"Mr\",    \r\n" + "\"rfirstname\": \"Jane\",\r\n"
//				+ "    \"rmiddlename\": \"Doe\",\r\n" + "    \"rlastname\": \"Smith\",\r\n"
//				+ "    \"relationship\": \"Sibling\",\r\n"
//				+ "    \"raddress\": \"456 Oak Street, City, State, Country\",\r\n" + "\"rphoneCode\" : \"+91\",\r\n"
//				+ "    \"rcontactno\": \"9876543210\"\r\n" + "  },\r\n" + "  \"visainfo\": {\r\n"
//				+ "    \"visaType\": \"Visa\",\r\n" + "    \"workVisaEmirateId\": \"0987654321\",\r\n" + "    \r\n"
//				+ "\"categoryOfVisa\" : \"visa\",\r\n" + "    \"siGlobalWorkVisaCompany\": \"ACME Corp\",\r\n"
//				+ "    \"visaIssueyDate\": \"20-01-2010\",\r\n" + "    \"visaExpiryDate\": \"2023-07-19\"\r\n"
//				+ "  },\r\n" + " \"bgcheck\"   : {\r\n" + "     \"status\" : \"india \",\r\n"
//				+ "     \"doneBy\" : \"done\",\r\n" + "\"internalConcernedManager\" : \"llll\",\r\n"
//				+ "\"managerApproval\" : \"pppp\",\r\n" + "\"managerName\" : \"rahil\",\r\n"
//				+ "\"remark\" : \"good\",\r\n" + "\"addressVerified\" : \"oooo\",\r\n"
//				+ "\"previousEmploymentStatusVerified\" : \"eeeee\",\r\n"
//				+ "\"previousDesignationAndResponsibilityVerified\"  : \"uuuuuuu\",\r\n"
//				+ "\"idProofDocumentVerified\" : \"Verified\",\r\n"
//				+ "\"educationalQualificationVerified\" : \"errrrrrr\",\r\n" + "\"criminalRecords\" : \"tttt\",\r\n"
//				+ "\"punishmentForImprisonmentApproval\" : \"Approval\",\r\n"
//				+ "\"externalName\": \"externalName\",\r\n" + "	\"externalPost\" : \"externalPost\",\r\n"
//				+ "	\"externalCompanyName\" :\"externalCompanyName\",\r\n"
//				+ "	\"externalPhoneCode\": \"externalPhoneCode\",\r\n"
//				+ "	\"externalPhoneNo\" : \"externalPhoneNo\"\r\n" + " },\r\n" + " \"educations\" : [{\r\n"
//				+ "  \"secondaryIssuingAuthority\": \"10\",\r\n" + "  \"secondarymarksOrGrade\": \"90\",\r\n"
//				+ "  \"secondaryyear\": \"2010\",     \r\n" + "  \"seniorSecondaryIssuingAuthority\": \"Bhopal 2\",\r\n"
//				+ "  \"seniorSecondaryMarksOrGrade\": \"80\",\r\n" + "  \"seniorSecondaryYear\": \"2011\",\r\n"
//				+ "  \"graduationIssuingAuthority\": \"bhopal 3\",\r\n" + "  \"graduationMarksOrGrade\": \"70\",\r\n"
//				+ "  \"graduationYear\": \"2012\",\r\n" + "  \"postGraduationIssuingAuthority\": \"Bhopal 4\",\r\n"
//				+ "  \"postGraduationMarksOrGrade\": \"60\",\r\n" + "  \"postGraduationYear\": \"2013\",\r\n"
//				+ "\"diplomaIssuingAuthority\": \"diplomaIssuingAuthority\",\r\n"
//				+ "\"diplomaMarksOrGrade\" : \"diplomaMarksOrGrade\",\r\n" + "\"diplomaYear\":\"diplomaYear\"\r\n"
//				+ "}\r\n" + "],\r\n" + "\"othersQualifications\" : [{\r\n" + "\"others\" : \"other\",\r\n"
//				+ " \"othersIssuingAuthority\": \"bhopal \",\r\n" + "  \"othersMarksOrGrade\": \"5\",\r\n"
//				+ "  \"othersYear\": \"2010\"\r\n" + "},\r\n" + "{\r\n" + "\"others\" : \"new other\",\r\n"
//				+ " \"othersIssuingAuthority\": \"bhopal 5\",\r\n" + "  \"othersMarksOrGrade\": \"50\",\r\n"
//				+ "  \"othersYear\": \"2014\"\r\n" + "}\r\n" + "],\r\n" + " \"training\" : [{\r\n"
//				+ "     \"trainingType\" : \"lll\",\r\n" + "     \"inHouseOutsource\" : \"aaaa\",\r\n"
//				+ "     \"paidUnpaid\" : \"hhh\",\r\n" + "\"trainingStartDate\" :\"32223\",\r\n"
//				+ "\"trainingEndDate\" : \"wwww\",\r\n" + "\"trainerFeedback\" : \"jjjj\",\r\n"
//				+ "\"trainerName\": \"trainerName\",\r\n" + "	\"trainerPost\" : \"trainerPost\",\r\n"
//				+ "	\"trainerDepartment\":\"trainerDepartment\",\r\n"
//				+ "	\"trainerPhoneCode\" :\"trainerPhoneCode\",\r\n"
//				+ "	\"trainerPhoneNo\" : \"trainerPhoneNoString\"\r\n" + " }], \r\n" + " \"oldEmployee\" : [{\r\n"
//				+ "     \"companyName\" : \"aibs\",\r\n" + "     \"companyAddress\" : \"bhopal\",\r\n"
//				+ "	\"designation\" : \"aaa\",\r\n" + "	\"description\" : \"DDD\",\r\n"
//				+ "	\"dateFrom\" : \"20-02-2016\",\r\n" + "	\"dateTo\": \"20-02-2030\",\r\n"
//				+ "\"previousManagerName\" : \"SSSSS\",\r\n" + "\"previousManagerPhoneCode\" : \"91\",\r\n"
//				+ "\"previousManagerContact\" : \"09876543\",\r\n" + "\"previousHRName\" : \"ali\",\r\n"
//				+ "\"previousHRPhoneCode\": \"91\",\r\n" + "\"previousHRContact\" : \"dddd\",\r\n"
//				+ "\"lastWithdrawnSalary\" : 900001,\r\n" + "\"empAchievements\" : [{\r\n"
//				+ "\"achievementRewardsName\" : \"Best of employee of the year\"\r\n" + "},\r\n" + "{\r\n"
//				+ "\"achievementRewardsName\" : \"Best of employee of the month\"\r\n" + "}\r\n" + "]\r\n" + "}\r\n"
//				+ "],\r\n" + " \"professionalQualifications\" :[{\r\n" + "     \"qualification\" : \"MBA \" ,\r\n"
//				+ "     \"issuingAuthority\" : \"bhopal \",\r\n" + "     \"gradingSystem\" : \"dddd \",\r\n"
//				+ "     \"yearOfQualification\" : \"2010 \",\r\n" + "     \"grade\" : \"B+\"\r\n" + "     \r\n"
//				+ " },\r\n" + "{\r\n" + "     \"qualification\" : \"MBA 2\" ,\r\n"
//				+ "     \"issuingAuthority\" : \"bhopal 2\",\r\n" + "     \"gradingSystem\" : \"dddd 2\",\r\n"
//				+ "     \"yearOfQualification\" : \"2010 2\",\r\n" + "     \"grade\" : \"B+ 2\"\r\n" + "     \r\n"
//				+ " }\r\n" + "],\r\n" + "\"jobDetails\" : [{\r\n" + " \r\n" + "  \"jobPostDesignation\": \"job\",\r\n"
//				+ "  \"companyEmailIdProvided\": \"job\",\r\n" + "  \"companyEmailId\": \"job\",\r\n"
//				+ "  \"jobLevel\": \"job\",\r\n" + "  \"postedLocation\": \"job\",\r\n"
//				+ "  \"basicAllowance\": \"job\",\r\n" + "  \"houseRentAllowance\": \"job\",\r\n"
//				+ "  \"houseRentAmount\": \"job\",\r\n" + "  \"foodAllowance\": \"job\",\r\n"
//				+ "  \"foodAllowanceAmount\": \"job\",\r\n" + "  \"vehicleAllowance\": \"job\",\r\n"
//				+ "  \"vehicleAllowanceAmount\": \"job\",\r\n" + "  \"uniformAllowance\": \"job\",\r\n"
//				+ "  \"uniformAllowanceAmount\": \"job\",\r\n" + "  \"travellingAllowances\": \"job\",\r\n"
//				+ "  \"travellingAllowancesAmount\": \"job\",\r\n" + "  \"educationalAllowance\": \"job\",\r\n"
//				+ "  \"educationalAllowanceAmount\": \"job\",\r\n" + "  \"otherAllowance\": \"job\",\r\n"
//				+ "  \"otherAllowanceAmount\": \"job\",\r\n" + "  \"vehicle\": \"job\",\r\n"
//				+ "  \"vehicleNumber\": \"job\",\r\n" + "  \"vehicleModelName\": \"job\",\r\n"
//				+ "  \"vehicleModelYear\": \"job\",\r\n" + "  \"isVehicleNewOrPreowned\": \"job\",\r\n" + "  \r\n"
//				+ "  \"accommodationYesOrNo\": \"job\",\r\n" + "  \"isShredOrSeparate\": \"job\",\r\n"
//				+ "  \"isExeutiveOrLabourFacility\": \"job\",\r\n" + "  \"electricityAllocationYesOrNo\": \"job\",\r\n"
//				+ "  \"electricityAllocationAmount\": \"job\",\r\n" + "  \"rentAllocationYesOrNo\": \"job\",\r\n"
//				+ "  \"rentAllocationAmount\": \"job\",\r\n" + "  \"mobileIssuedOrNot\": \"job\",\r\n"
//				+ "  \"simIssuedOrNot\": \"job\",\r\n" + "  \"flightFacilities\": \"job\",\r\n"
//				+ "  \"howMuchTime\": \"job\",\r\n" + "  \"familyTicketsAlsoProvidedOrNot\": \"job\",\r\n"
//				+ "  \"othersAccomandation\": \"job\",\r\n" + "  \"healthInsuranceCoverage\": \"job\",\r\n"
//				+ "  \"maximumAmountGiven\": \"job\",\r\n" + "  \"familyHealthInsuranceGivenOrNot\": \"job\",\r\n"
//				+ "  \"familyHealthInsuranceType\" :\"type\",\r\n" + "  \"punchingMachineYesOrNo\": \"job\",\r\n"
//				+ "  \"joiningDate\": \"job\",\r\n" + "\"jobdepartment\" : \"joib\",\r\n"
//				+ "\"chipNumber\" : \"0987654987\",\r\n" + "\"cashOrChipFacility\": \"yes\",\r\n"
//				+ "\"referredBy\" : \"yes\",\r\n" + "\"byWhom\" : \"mustufa\"\r\n" + "}]\r\n" + "}\r\n" + "";
//		MockMultipartFile passportSizePhoto = new MockMultipartFile("passportSizePhoto", "photo.jpg", "image/jpeg",
//				"photo data".getBytes());
//		MockMultipartFile OtherIdProofDoc = new MockMultipartFile("OtherIdProofDoc", "OtherIdProofDoc.jpg",
//				"image/jpeg", "OtherIdProofDoc scan data".getBytes());
//		MockMultipartFile passportScan = new MockMultipartFile("passportScan", "passport.jpg", "image/jpeg",
//				"passport scan data".getBytes());
//		MockMultipartFile licensecopy = new MockMultipartFile("licensecopy", "licensecopy.jpg", "image/jpeg",
//				"licensecopy scan data".getBytes());
//		MockMultipartFile relativeid = new MockMultipartFile("relativeid", "relativeid.jpg", "image/jpeg",
//				"relativeid scan data".getBytes());
//		MockMultipartFile raddressproof = new MockMultipartFile("raddressproof", "raddressproof.jpg", "image/jpeg",
//				"raddressproof scan data".getBytes());
//		MockMultipartFile secondaryDocumentScan = new MockMultipartFile("secondaryDocumentScan",
//				"secondaryDocumentScan.jpg", "image/jpeg", "secondaryDocumentScan scan data".getBytes());
//		MockMultipartFile seniorSecondaryDocumentScan = new MockMultipartFile("seniorSecondaryDocumentScan",
//				"seniorSecondaryDocumentScan.jpg", "image/jpeg", "seniorSecondaryDocumentScan scan data".getBytes());
//		MockMultipartFile graduationDocumentScan = new MockMultipartFile("graduationDocumentScan",
//				"graduationDocumentScan.jpg", "image/jpeg", "graduationDocumentScan scan data".getBytes());
//		MockMultipartFile postGraduationDocumentScan = new MockMultipartFile("postGraduationDocumentScan",
//				"postGraduationDocumentScan.jpg", "image/jpeg", "postGraduationDocumentScan scan data".getBytes());
//		MockMultipartFile[] othersDocumentScan = {
//				new MockMultipartFile("othersDocumentScan1", "othersDocumentScan1.jpg", "image/jpeg",
//						"othersDocumentScan1 scan data".getBytes()),
//				new MockMultipartFile("othersDocumentScan2", "othersDocumentScan2.jpg", "image/jpeg",
//						"othersDocumentScan2 scan data".getBytes()) };
//		MockMultipartFile[] degreeScan = {
//				new MockMultipartFile("degreeScan1", "degreeScan1.jpg", "image/jpeg",
//						"degreeScan1 scan data".getBytes()),
//				new MockMultipartFile("degreeScan2", "degreeScan2.jpg", "image/jpeg",
//						"degreeScan2 scan data".getBytes()) };
//		MockMultipartFile payslipScan = new MockMultipartFile("payslipScan", "payslipScan.jpg", "image/jpeg",
//				"payslipScan scan data".getBytes());
//		MockMultipartFile recordsheet = new MockMultipartFile("recordsheet", "recordsheet.jpg", "image/jpeg",
//				"recordsheet scan data".getBytes());
//		MockMultipartFile PaidTrainingDocumentProof = new MockMultipartFile("PaidTrainingDocumentProof",
//				"PaidTrainingDocumentProof.jpg", "image/jpeg", "PaidTrainingDocumentProof scan data".getBytes());
//		MockMultipartFile CertificateUploadedForOutsource = new MockMultipartFile("CertificateUploadedForOutsource",
//				"CertificateUploadedForOutsource.jpg", "image/jpeg",
//				"CertificateUploadedForOutsource scan data".getBytes());
//		MockMultipartFile visaDocs = new MockMultipartFile("visaDocs", "visaDocs.jpg", "image/jpeg",
//				"visaDocs scan data".getBytes());
//		MockMultipartFile diplomaDocumentScan = new MockMultipartFile("diplomaDocumentScan", "diplomaDocumentScan.jpg",
//				"image/jpeg", "diplomaDocumentScan scan data".getBytes());
//		MockMultipartFile declarationRequired = new MockMultipartFile("declarationRequired", "declarationRequired.jpg",
//				"image/jpeg", "declarationRequired scan data".getBytes());
//		MockMultipartFile[] achievementsRewardsDocs = {
//				new MockMultipartFile("achievementsRewardsDocs", "achievementsRewardsDocs.jpg", "image/jpeg",
//						"achievementsRewardsDocs scan data".getBytes()),
//				new MockMultipartFile("achievementsRewardsDocs2", "achievementsRewardsDocs2.jpg", "image/jpeg",
//						"achievementsRewardsDocs scan data".getBytes()) };
//		PersonalInfo existingPersonalInfo = new PersonalInfo();
//		PassportDetails passportDetails = new PassportDetails();
//		existingPersonalInfo.setPsDetail(passportDetails);
//		DrivingLicense drivingLicense = new DrivingLicense();
//		existingPersonalInfo.setLicense(drivingLicense);
//		BloodRelative bloodRelative = new BloodRelative();
//		existingPersonalInfo.setRelative(bloodRelative);
//		VisaDetail visaDetail = new VisaDetail();
//		existingPersonalInfo.setVisainfo(visaDetail);
//		List<Education> existingEducations = new ArrayList<>();
//		Education education1 = new Education();
//		education1.setSecondaryIssuingAuthority("Secondary Authority 1");
//		education1.setSecondarymarksOrGrade("Secondary Marks/Grade 1");
//		education1.setSecondaryyear("Secondary Year 1");
//		education1.setSeniorSecondaryIssuingAuthority("Senior secondary Issuing Authority");
//		education1.setSeniorSecondaryMarksOrGrade("Senior Secondary marks");
//		education1.setSeniorSecondaryYear("Senior year 2");
//		education1.setGraduationIssuingAuthority("Graduation Issuing Authority");
//		education1.setGraduationMarksOrGrade("Graduation Marks");
//		education1.setGraduationYear("graduation year 3");
//		education1.setPostGraduationIssuingAuthority("post graduation issuing ");
//		education1.setPostGraduationMarksOrGrade("Post marks");
//		education1.setPostGraduationYear("post year 1");
//		education1.setDiplomaIssuingAuthority("Diploma issuing ");
//		education1.setDiplomaMarksOrGrade("diploma marks");
//		education1.setDiplomaYear("Diploma year");
//		existingEducations.add(education1);
//		existingPersonalInfo.setEducations(existingEducations);
//
//		List<OthersQualification> existingothersQualifications = new ArrayList<>();
//		OthersQualification othersQualification1 = new OthersQualification();
//		othersQualification1.setOthers("Other qualification 1");
//		othersQualification1.setOthersIssuingAuthority("Other issuing authority 1");
//		othersQualification1.setOthersMarksOrGrade("Other marks 1");
//		othersQualification1.setOthersYear("Other year 1");
//		existingothersQualifications.add(othersQualification1);
//		OthersQualification othersQualification2 = new OthersQualification();
//		othersQualification2.setOthers("others qualification 2");
//		othersQualification2.setOthersIssuingAuthority("others issuing authoruty 2");
//		othersQualification2.setOthersMarksOrGrade("marks ");
//		othersQualification2.setOthersYear("others year 2");
//		existingothersQualifications.add(othersQualification2);
//		existingPersonalInfo.setOthersQualifications(existingothersQualifications);
//
//		List<ProfessionalQualification> existingProfessionalQualifications = new ArrayList<>();
//		ProfessionalQualification professionalQualification1 = new ProfessionalQualification();
//		professionalQualification1.setIssuingAuthority("Professional Issuing");
//		professionalQualification1.setQualification("Professional qualification");
//		professionalQualification1.setGradingSystem("Gradig system");
//		professionalQualification1.setGrade("professionl grade");
//		professionalQualification1.setYearOfQualification("professional year");
//		existingProfessionalQualifications.add(professionalQualification1);
//
//		ProfessionalQualification professionalQualification2 = new ProfessionalQualification();
//		professionalQualification2.setIssuingAuthority("Professional Issuing 2");
//		professionalQualification2.setQualification("Professional qualification 2");
//		professionalQualification2.setGradingSystem("Gradig system 2");
//		professionalQualification2.setGrade("professionl grade 2");
//		professionalQualification2.setYearOfQualification("professional year 2");
//		existingProfessionalQualifications.add(professionalQualification2);
//
//		existingPersonalInfo.setProfessionalQualifications(existingProfessionalQualifications);
//
//		List<PreviousEmployee> existingPreviousEmployees = new ArrayList<>();
//		PreviousEmployee previousEmployee1 = new PreviousEmployee();
//		previousEmployee1.setCompanyName("company name");
//		previousEmployee1.setCompanyAddress("Company address");
//		previousEmployee1.setDesignation("company desi	gnation");
//		previousEmployee1.setDescription("company description");
//		previousEmployee1.setDateFrom("2000-02-02");
//		previousEmployee1.setDateTo("2023-02-02");
//		previousEmployee1.setPreviousHRName("Sameer");
//		previousEmployee1.setPreviousHRPhoneCode("+91");
//		previousEmployee1.setPreviousHRContact("09876545678");
//		previousEmployee1.setPreviousManagerName("Rahil");
//		previousEmployee1.setPreviousManagerPhoneCode("+91");
//		previousEmployee1.setPreviousManagerContact("234321234543");
//		previousEmployee1.setLastWithdrawnSalary(20000);
//		List<EmpAchievement> existingAchievements = new ArrayList<>();
//		EmpAchievement empAchievement1 = new EmpAchievement();
//		empAchievement1.setAchievementRewardsName("Achievement Rewards");
//		existingAchievements.add(empAchievement1);
//
//		EmpAchievement empAchievement2 = new EmpAchievement();
//		empAchievement2.setAchievementRewardsName("Achievement Rewards 2");
//		existingAchievements.add(empAchievement2);
//
//		previousEmployee1.setEmpAchievements(existingAchievements);
//		existingPreviousEmployees.add(previousEmployee1);
//
//		existingPersonalInfo.setOldEmployee(existingPreviousEmployees);
//
//		List<Trainingdetails> existingTrainingdetails = new ArrayList<>();
//		Trainingdetails trainingdetails1 = new Trainingdetails();
//		trainingdetails1.setTrainingType("Type of training");
//		trainingdetails1.setPaidUnpaid("Paid");
//		trainingdetails1.setInHouseOutsource("Inhouse OR out source");
//		trainingdetails1.setTrainingStartDate("2012-03-03");
//		trainingdetails1.setTrainingEndDate("2013-02-02");
//		trainingdetails1.setTrainerFeedback("Trainer Feedback");
//		trainingdetails1.setTrainerName("trainer name");
//		trainingdetails1.setTrainerPost("trainer post");
//		trainingdetails1.setTrainerDepartment("Trainer Department");
//		trainingdetails1.setTrainerPhoneCode("+91");
//		trainingdetails1.setTrainerPhoneNo("7654323456");
//
//		existingTrainingdetails.add(trainingdetails1);
//		existingPersonalInfo.setTraining(existingTrainingdetails);
//
//		List<JobDetails> existingJobDetails = new ArrayList<>();
//		JobDetails jobDetails1 = new JobDetails();
//		jobDetails1.setJobPostDesignation("Job post designation");
//		jobDetails1.setCompanyEmailIdProvided("Company email ID provided");
//		jobDetails1.setCompanyEmailId("Company email ID");
//		jobDetails1.setJobLevel("Job level");
//		jobDetails1.setPostedLocation("Posted location");
//		jobDetails1.setBasicAllowance("Basic allowance");
//		jobDetails1.setHouseRentAllowance("House rent allowance");
//		jobDetails1.setHouseRentAmount("House rent amount");
//		jobDetails1.setFoodAllowance("Food allowance");
//		jobDetails1.setFoodAllowanceAmount("Food allowance amount");
//		jobDetails1.setVehicleAllowance("Vehicle allowance");
//		jobDetails1.setVehicleAllowanceAmount("Vehicle allowance amount");
//		jobDetails1.setUniformAllowance("Uniform allowance");
//		jobDetails1.setUniformAllowanceAmount("Uniform allowance amount");
//		jobDetails1.setTravellingAllowances("Travelling allowances");
//		jobDetails1.setTravellingAllowancesAmount("Travelling allowances amount");
//		jobDetails1.setEducationalAllowance("Educational allowance");
//		jobDetails1.setEducationalAllowanceAmount("Educational allowance amount");
//		jobDetails1.setOtherAllowance("Other allowance");
//		jobDetails1.setOtherAllowanceAmount("Other allowance amount");
//		jobDetails1.setVehicle("Vehicle");
//		jobDetails1.setVehicleNumber("Vehicle number");
//		jobDetails1.setVehicleModelName("Vehicle model name");
//		jobDetails1.setVehicleModelYear("Vehicle model year");
//		jobDetails1.setIsVehicleNewOrPreowned("Is vehicle new or preowned");
//		jobDetails1.setCashOrChipFacility("Cash or chip facility");
//		jobDetails1.setChipNumber("Chip number");
//		jobDetails1.setAccommodationYesOrNo("Accommodation yes or no");
//		jobDetails1.setIsShredOrSeparate("Is shred or separate");
//		jobDetails1.setIsExeutiveOrLabourFacility("Is executive or labour facility");
//		jobDetails1.setElectricityAllocationYesOrNo("Electricity allocation yes or no");
//		jobDetails1.setElectricityAllocationAmount("Electricity allocation amount");
//		jobDetails1.setRentAllocationYesOrNo("Rent allocation yes or no");
//		jobDetails1.setRentAllocationAmount("Rent allocation amount");
//		jobDetails1.setMobileIssuedOrNot("Mobile issued or not");
//		jobDetails1.setSimIssuedOrNot("SIM issued or not");
//		jobDetails1.setFlightFacilities("Flight facilities");
//		jobDetails1.setHowMuchTime("How much time");
//		jobDetails1.setFamilyTicketsAlsoProvidedOrNot("Family tickets also provided or not");
//		jobDetails1.setOthersAccomandation("Others accommodation");
//		jobDetails1.setHealthInsuranceCoverage("Health insurance coverage");
//		jobDetails1.setMaximumAmountGiven("Maximum amount given");
//		jobDetails1.setFamilyHealthInsuranceGivenOrNot("Family health insurance given or not");
//		jobDetails1.setFamilyHealthInsuranceType("Family health insurance type");
//		jobDetails1.setPunchingMachineYesOrNo("Punching machine yes or no");
//		jobDetails1.setJoiningDate("Joining date");
////		jobDetails1.setJobdepartment("Job department");
//		jobDetails1.setReferredBy("Referred by");
//		jobDetails1.setByWhom("By whom");
//
//		existingJobDetails.add(jobDetails1);
//		existingPersonalInfo.setJobDetails(existingJobDetails);
//
//		BackgroundCheck existingBackgroundCheck = new BackgroundCheck();
//		existingBackgroundCheck.setStatus("back ground status");
//		existingBackgroundCheck.setDoneBy("Done by");
//		existingBackgroundCheck.setInternalConcernedManager("Internal concerned manager");
//		existingBackgroundCheck.setExternalName("External name");
//		existingBackgroundCheck.setExternalPost("External post");
//		existingBackgroundCheck.setExternalCompanyName("External company name");
//		existingBackgroundCheck.setExternalPhoneCode("External phone code");
//		existingBackgroundCheck.setExternalPhoneNo("External phone number");
//		existingBackgroundCheck.setManagerApproval("Manager approval");
//		existingBackgroundCheck.setManagerName("Manager name");
//		existingBackgroundCheck.setRemark("Remark");
//		existingBackgroundCheck.setAddressVerified("Address verified");
//		existingBackgroundCheck.setPreviousEmploymentStatusVerified("Previous employment status verified");
//		existingBackgroundCheck
//				.setPreviousDesignationAndResponsibilityVerified("Previous designation and responsibility verified");
//		existingBackgroundCheck.setIdProofDocumentVerified("ID proof document verified");
//		existingBackgroundCheck.setEducationalQualificationVerified("Educational qualification verified");
//		existingBackgroundCheck.setCriminalRecords("Criminal records");
//		existingBackgroundCheck.setPunishmentForImprisonmentApproval("Punishment for imprisonment approval");
//
//		existingPersonalInfo.setBgcheck(existingBackgroundCheck);
//
//		when(dao.getPersonalInfoByEmailForUpdate(email)).thenReturn(existingPersonalInfo);
//		when(dao.updatePersonalInfo(eq(email), any(PersonalInfo.class))).thenReturn(existingPersonalInfo);
//		PersonalInfo updatedPersonalInfo = service.updatePersonalInfo(email, personalInfoJson, passportSizePhoto,
//				OtherIdProofDoc, passportScan, licensecopy, relativeid, raddressproof, secondaryDocumentScan,
//				seniorSecondaryDocumentScan, graduationDocumentScan, postGraduationDocumentScan, othersDocumentScan,
//				degreeScan, payslipScan, recordsheet, PaidTrainingDocumentProof, CertificateUploadedForOutsource,
//				visaDocs, diplomaDocumentScan, declarationRequired, achievementsRewardsDocs);
//		assertEquals(existingPersonalInfo, updatedPersonalInfo);
//	}
//
//}
