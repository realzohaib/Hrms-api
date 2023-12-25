//package com.erp.hrms.test.controller;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.io.IOException;
//import java.util.Collections;
//import java.util.List;
//
//import javax.servlet.http.HttpServletResponse;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.erp.hrms.api.security.response.MessageResponse;
//import com.erp.hrms.api.service.IPersonalInfoService;
//import com.erp.hrms.controller.PersonalInfoController;
//import com.erp.hrms.entity.PersonalInfo;
//import com.erp.hrms.exception.PersonalInfoNotFoundException;
//
//public class PersonalInfoControllerTest {
//
//	@Mock
//	private IPersonalInfoService personalInfoService;
//
//	@InjectMocks
//	private PersonalInfoController personalInfoController;
//
//	@BeforeEach
//	public void setup() {
//		MockitoAnnotations.openMocks(this);
//	}
//
//	@Test
//	public void testSavePersonalInfo() throws IOException {
//		// Mock the required dependencies
//		MultipartFile passportSizePhoto = mock(MultipartFile.class);
//		MultipartFile OtherIdProofDoc = mock(MultipartFile.class);
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
//		MultipartFile PaidTrainingDocumentProof = mock(MultipartFile.class);
//		MultipartFile CertificateUploadedForOutsource = mock(MultipartFile.class);
//		MultipartFile visaDocs = mock(MultipartFile.class);
//		MultipartFile diplomaDocumentScan = mock(MultipartFile.class);
//		MultipartFile declarationRequired = mock(MultipartFile.class);
//		MultipartFile[] achievementsRewardsDocs = { mock(MultipartFile.class) };
//
//		ResponseEntity<?> response = personalInfoController.savePersonalInfo("personalInfo", passportSizePhoto,
//				OtherIdProofDoc, passportScan, licensecopy, relativeid, raddressproof, visaDocs, secondaryDocumentScan,
//				seniorSecondaryDocumentScan, graduationDocumentScan, postGraduationDocumentScan, othersDocumentScan,
//				degreeScan, payslipScan, recordsheet, CertificateUploadedForOutsource, PaidTrainingDocumentProof,
//				diplomaDocumentScan, declarationRequired, achievementsRewardsDocs);
//
//		assertEquals(200, response.getStatusCodeValue());
//		assertEquals("Insert Personal info successfully", ((MessageResponse) response.getBody()).getMessage());
//
//		verify(personalInfoService).savedata(eq("personalInfo"), any(), any(), any(), any(), any(), any(), any(), any(),
//				any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
//	}
//
////	@Test
////	public void testFindAllPersonalInfo() {
////		List<PersonalInfo> personalInfos = Collections.singletonList(new PersonalInfo());
////		when(personalInfoService.findAllPersonalInfo()).thenReturn(personalInfos);
////		ResponseEntity<?> response = personalInfoController.findAllPersonalInfo();
////		assertEquals(200, response.getStatusCodeValue());
////		assertEquals(personalInfos, response.getBody());
////		verify(personalInfoService).findAllPersonalInfo();
////	}
//
////	@Test
////	public void testGetPersonalInfoByEmail() throws IOException {
////		HttpServletResponse response = mock(HttpServletResponse.class);
////		ResponseEntity<?> result = personalInfoController.getPersonalInfoByEmail("test@example.com", response);
////		assertEquals(200, result.getStatusCodeValue());
////		verify(personalInfoService).getPersonalInfoByEmail(eq("test@example.com"));
////	}
//
//	@Test
//	public void testGetPersonalInfoByEmployeeId() throws IOException {
//		HttpServletResponse response = mock(HttpServletResponse.class);
//		ResponseEntity<?> result = personalInfoController.getPersonalInfoByEmployeeId(123L, response);
//		assertEquals(200, result.getStatusCodeValue());
//		verify(personalInfoService).getPersonalInfoByEmployeeId(eq(123L));
//	}
//
//	@Test
//	public void testDeletePersonalInfoByEmailSuccess() throws IOException {
//		String email = "test@example.com";
//		String personalInfoJson = "{\"status\":\"Inactive\"}";
//		ResponseEntity<?> response = personalInfoController.deletePersonalInfoByEmail(email, personalInfoJson);
//		assertNotNull(response);
//		assertEquals(200, response.getStatusCodeValue());
//		assertEquals("Record delete successfully", ((MessageResponse) response.getBody()).getMessage());
//		verify(personalInfoService, times(1)).deletePersonalInfoByEmail(email, personalInfoJson);
//	}
//
//	@Test
//	public void testDeletePersonalInfoByEmailException() throws IOException {
//		String email = "test@example.com";
//		String personalInfoJson = "{\"status\":\"Inactive\"}";
//		doThrow(new PersonalInfoNotFoundException(new MessageResponse("Personal info not found")))
//				.when(personalInfoService).deletePersonalInfoByEmail(email, personalInfoJson);
//		PersonalInfoNotFoundException exception = assertThrows(PersonalInfoNotFoundException.class,
//				() -> personalInfoController.deletePersonalInfoByEmail(email, personalInfoJson));
//		assertEquals("Personal info not found", exception.getMessage());
//	}
//
//	@Test
//	public void testUpdatePersonalInfo() throws IOException {
//		String email = "test@example.com";
//		String personalInfo = "Personal info"; // Replace with actual personal info string
//
//		// Sample MultipartFiles for testing
//		MockMultipartFile passportSizePhoto = new MockMultipartFile("passportSizePhoto", "photo.jpg", "image/jpeg",
//				new byte[0]);
//		MockMultipartFile otherIdProofDoc = new MockMultipartFile("OtherIdProofDoc", "id_proof.pdf", "application/pdf",
//				new byte[0]);
//		MockMultipartFile passportScan = new MockMultipartFile("passportScan", "passportScan.pdf", "application/pdf",
//				new byte[0]);
//		MockMultipartFile licensecopy = new MockMultipartFile("licensecopy", "licensecopy.pdf", "application/pdf",
//				new byte[0]);
//		MockMultipartFile relativeid = new MockMultipartFile("relativeid", "relativeid.pdf", "application/pdf",
//				new byte[0]);
//		MockMultipartFile raddressproof = new MockMultipartFile("raddressproof", "raddressproof.pdf", "application/pdf",
//				new byte[0]);
//		MockMultipartFile secondaryDocumentScan = new MockMultipartFile("secondaryDocumentScan",
//				"secondaryDocumentScan.pdf", "application/pdf", new byte[0]);
//		MockMultipartFile seniorSecondaryDocumentScan = new MockMultipartFile("seniorSecondaryDocumentScan",
//				"seniorSecondaryDocumentScan.pdf", "application/pdf", new byte[0]);
//		MockMultipartFile graduationDocumentScan = new MockMultipartFile("graduationDocumentScan",
//				"graduationDocumentScan.pdf", "application/pdf", new byte[0]);
//		MockMultipartFile postGraduationDocumentScan = new MockMultipartFile("postGraduationDocumentScan",
//				"postGraduationDocumentScan.pdf", "application/pdf", new byte[0]);
//		MockMultipartFile[] othersDocumentScan = {
//				new MockMultipartFile("othersDocumentScan", "othersDocumentScan.pdf", "application/pdf", new byte[0]),
//				new MockMultipartFile("othersDocumentScan 2", "othersDocumentScan2.pdf", "application/pdf",
//						new byte[0]) };
//		MockMultipartFile[] degreeScan = {
//				new MockMultipartFile("degreeScan", "degreeScan.pdf", "application/pdf", new byte[0]) };
//		MockMultipartFile payslipScan = new MockMultipartFile("payslipScan", "payslipScan.pdf", "application/pdf",
//				new byte[0]);
//		MockMultipartFile recordsheet = new MockMultipartFile("recordsheet", "recordsheet.pdf", "application/pdf",
//				new byte[0]);
//		MockMultipartFile CertificateUploadedForOutsource = new MockMultipartFile("CertificateUploadedForOutsource",
//				"CertificateUploadedForOutsource.pdf", "application/pdf", new byte[0]);
//		MockMultipartFile PaidTrainingDocumentProof = new MockMultipartFile("PaidTrainingDocumentProof",
//				"PaidTrainingDocumentProof.pdf", "application/pdf", new byte[0]);
//		MockMultipartFile visaDocs = new MockMultipartFile("visaDocs", "visaDocs.pdf", "application/pdf", new byte[0]);
//		MockMultipartFile diplomaDocumentScan = new MockMultipartFile("diplomaDocumentScan", "diplomaDocumentScan.pdf",
//				"application/pdf", new byte[0]);
//		MockMultipartFile declarationRequired = new MockMultipartFile("declarationRequired", "declarationRequired.pdf",
//				"application/pdf", new byte[0]);
//		MockMultipartFile[] achievementsRewardsDocs = { new MockMultipartFile("achievementsRewardsDocs",
//				"achievementsRewardsDocs.pdf", "application/pdf", new byte[0]) };
//
//		ResponseEntity<?> response = personalInfoController.updatePersonalInfo(email, personalInfo, passportSizePhoto,
//				otherIdProofDoc, passportScan, licensecopy, relativeid, raddressproof, secondaryDocumentScan,
//				seniorSecondaryDocumentScan, graduationDocumentScan, postGraduationDocumentScan, othersDocumentScan,
//				degreeScan, payslipScan, recordsheet, CertificateUploadedForOutsource, PaidTrainingDocumentProof,
//				visaDocs, diplomaDocumentScan, declarationRequired, achievementsRewardsDocs);
//		assertEquals(200, response.getStatusCodeValue());
//		assertEquals("Record update successfully", ((MessageResponse) response.getBody()).getMessage());
//		verify(personalInfoService).updatePersonalInfo(email, personalInfo, passportSizePhoto, otherIdProofDoc,
//				passportScan, licensecopy, relativeid, raddressproof, secondaryDocumentScan,
//				seniorSecondaryDocumentScan, graduationDocumentScan, postGraduationDocumentScan, othersDocumentScan,
//				degreeScan, payslipScan, recordsheet, PaidTrainingDocumentProof, CertificateUploadedForOutsource,
//				visaDocs, diplomaDocumentScan, declarationRequired, achievementsRewardsDocs);
//	}
//}
