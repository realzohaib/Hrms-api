package com.erp.hrms.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.api.security.response.StatusResponse;
import com.erp.hrms.api.service.IPersonalInfoService;
import com.erp.hrms.entity.PersonalInfo;
import com.erp.hrms.entity.notificationhelper.NotificationHelper;
import com.erp.hrms.exception.PersonalInfoNotFoundException;

@RestController
@RequestMapping("/api/v1")
//@CrossOrigin("*")
public class PersonalInfoController {

	private static final Logger logger = LoggerFactory.getLogger(PersonalInfoController.class);

	@Autowired
	private IPersonalInfoService personalInfoService;

	@PostMapping("/personal-info")
	public ResponseEntity<?> savePersonalInfo(@RequestParam("PersonalInfo") String personalinfo,
			@RequestParam(value = "passportSizePhoto", required = false) MultipartFile passportSizePhoto,
			@RequestParam(value = "OtherIdProofDoc", required = false) MultipartFile OtherIdProofDoc,
			@RequestParam(value = "passportScan", required = false) MultipartFile passportScan,
			@RequestParam(value = "licensecopy", required = false) MultipartFile licensecopy,
			@RequestParam(value = "relativeid", required = false) MultipartFile relativeid,
			@RequestParam(value = "raddressproof", required = false) MultipartFile raddressproof,
			@RequestParam(value = "visaDocs", required = false) MultipartFile visaDocs,
			@RequestParam(value = "secondaryDocumentScan", required = false) MultipartFile secondaryDocumentScan,
			@RequestParam(value = "seniorSecondaryDocumentScan", required = false) MultipartFile seniorSecondaryDocumentScan,
			@RequestParam(value = "graduationDocumentScan", required = false) MultipartFile graduationDocumentScan,
			@RequestParam(value = "postGraduationDocumentScan", required = false) MultipartFile postGraduationDocumentScan,
			@RequestParam(value = "othersDocumentScan", required = false) MultipartFile[] othersDocumentScan,
			@RequestParam(value = "degreeScan", required = false) MultipartFile[] degreeScan,
			@RequestParam(value = "payslipScan", required = false) MultipartFile payslipScan,
			@RequestParam(value = "recordsheet", required = false) MultipartFile recordsheet,
			@RequestParam(value = "CertificateUploadedForOutsource", required = false) MultipartFile CertificateUploadedForOutsource,
			@RequestParam(value = "PaidTrainingDocumentProof", required = false) MultipartFile PaidTrainingDocumentProof,
			@RequestParam(value = "diplomaDocumentScan", required = false) MultipartFile diplomaDocumentScan,
			@RequestParam(value = "declarationRequired", required = false) MultipartFile declarationRequired,
			@RequestParam(value = "achievementsRewardsDocs", required = false) MultipartFile[] achievementsRewardsDocs)
			throws IOException {
		try {

			personalInfoService.savedata(personalinfo, passportSizePhoto, OtherIdProofDoc, passportScan, licensecopy,
					relativeid, raddressproof, secondaryDocumentScan, seniorSecondaryDocumentScan,
					graduationDocumentScan, postGraduationDocumentScan, othersDocumentScan, degreeScan, payslipScan,
					recordsheet, PaidTrainingDocumentProof, CertificateUploadedForOutsource, visaDocs,
					diplomaDocumentScan, declarationRequired, achievementsRewardsDocs);
			return ResponseEntity.ok(new MessageResponse("Insert Personal info successfully"));
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.badRequest().body(new Exception("error occured " + e));
		}

	}

	@GetMapping("/personal-info/find/all/active")
	public ResponseEntity<?> findAllPersonalInfo() {
		List<PersonalInfo> findAllPersonalInfo = null;
		try {
			findAllPersonalInfo = personalInfoService.findAllPersonalInfo();

		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Exception occurred " + e);
		}
		return ResponseEntity.ok(findAllPersonalInfo);
	}

	@GetMapping("/personal-info/email/{email}")
	public ResponseEntity<?> getPersonalInfoByEmail(@PathVariable String email, HttpServletResponse response)
			throws IOException {
		try {

			return ResponseEntity.ok(personalInfoService.getPersonalInfoByEmail(email));
		} catch (PersonalInfoNotFoundException e) {
//			System.out.println(e);
			return ResponseEntity.badRequest().body(new Exception("error occured " + e));

		} catch (Exception e) {
//			System.out.println(e);
			return ResponseEntity.badRequest().body(new Exception("error occured " + e));
		}
	}

	@GetMapping("/personal-info/employeeId/{employeeId}")
	public ResponseEntity<?> getPersonalInfoByEmployeeId(@PathVariable Long employeeId, HttpServletResponse response)
			throws IOException {
		try {

			return ResponseEntity.ok(personalInfoService.getPersonalInfoByEmployeeId(employeeId));
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.badRequest().body(new Exception("error occured " + e));
		}
	}

	@PutMapping("/personal-info/delete/{email}")
	public ResponseEntity<?> deletePersonalInfoByEmail(@PathVariable String email,
			@RequestParam("PersonalInfo") String personalinfo) throws IOException {
		try {
			personalInfoService.deletePersonalInfoByEmail(email, personalinfo);
			return ResponseEntity.ok(new MessageResponse("Record delete successfully"));

		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.badRequest().body(new Exception("error occured " + e));
		}
	}

	@PutMapping("/personal-info/update/email/{email}")
	public ResponseEntity<?> updatePersonalInfo(@PathVariable String email,
			@RequestParam("PersonalInfo") String personalinfo,
			@RequestParam(value = "passportSizePhoto", required = false) MultipartFile passportSizePhoto,
			@RequestParam(value = "OtherIdProofDoc", required = false) MultipartFile OtherIdProofDoc,
			@RequestParam(value = "passportScan", required = false) MultipartFile passportScan,
			@RequestParam(value = "licensecopy", required = false) MultipartFile licensecopy,
			@RequestParam(value = "relativeid", required = false) MultipartFile relativeid,
			@RequestParam(value = "raddressproof", required = false) MultipartFile raddressproof,
			@RequestParam(value = "secondaryDocumentScan", required = false) MultipartFile secondaryDocumentScan,
			@RequestParam(value = "seniorSecondaryDocumentScan", required = false) MultipartFile seniorSecondaryDocumentScan,
			@RequestParam(value = "graduationDocumentScan", required = false) MultipartFile graduationDocumentScan,
			@RequestParam(value = "postGraduationDocumentScan", required = false) MultipartFile postGraduationDocumentScan,
			@RequestParam(value = "othersDocumentScan", required = false) MultipartFile[] othersDocumentScan,
			@RequestParam(value = "degreeScan", required = false) MultipartFile[] degreeScan,
			@RequestParam(value = "payslipScan", required = false) MultipartFile payslipScan,
			@RequestParam(value = "recordsheet", required = false) MultipartFile recordsheet,
			@RequestParam(value = "CertificateUploadedForOutsource", required = false) MultipartFile CertificateUploadedForOutsource,
			@RequestParam(value = "PaidTrainingDocumentProof", required = false) MultipartFile PaidTrainingDocumentProof,
			@RequestParam(value = "visaDocs", required = false) MultipartFile visaDocs,
			@RequestParam(value = "diplomaDocumentScan", required = false) MultipartFile diplomaDocumentScan,
			@RequestParam(value = "declarationRequired", required = false) MultipartFile declarationRequired,
			@RequestParam(value = "achievementsRewardsDocs", required = false) MultipartFile[] achievementsRewardsDocs)
			throws IOException {
		try {
			personalInfoService.updatePersonalInfo(email, personalinfo, passportSizePhoto, OtherIdProofDoc,
					passportScan, licensecopy, relativeid, raddressproof, secondaryDocumentScan,
					seniorSecondaryDocumentScan, graduationDocumentScan, postGraduationDocumentScan, othersDocumentScan,
					degreeScan, payslipScan, recordsheet, PaidTrainingDocumentProof, CertificateUploadedForOutsource,
					visaDocs, diplomaDocumentScan, declarationRequired, achievementsRewardsDocs);

			return ResponseEntity.ok(new MessageResponse("Record update successfully"));
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.badRequest().body(new Exception("error occured " + e));
		}
	}

	@GetMapping("/dashboard")
	public ResponseEntity<?> dashbord() {
		try {
			logger.debug("Error occured");
			return ResponseEntity.ok(new StatusResponse(true));
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.badRequest().body(new Exception("error occured " + e));
		}
	}

	@PutMapping("/personal-info/update-visa-details/employeeId/{employeeId}")
	public ResponseEntity<?> updateVisaDetails(@PathVariable Long employeeId, @RequestParam String visaIssueDate,
			@RequestParam String visaExpiryDate) {
		try {
			personalInfoService.updateVisaDetails(employeeId, visaIssueDate, visaExpiryDate);
			return ResponseEntity.ok(new MessageResponse("Visa details updated successfully."));
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.badRequest().body(new Exception("error occured " + e));
		}
	}

	@GetMapping("/visa-notification")
	public ResponseEntity<?> getRequestFields() {
		List<NotificationHelper> requestedField = null;
		try {
			requestedField = personalInfoService.getRequestedField();
			return ResponseEntity.ok(requestedField);
		} catch (Exception e) {
			System.out.println(e);
			return ResponseEntity.badRequest().body(new Exception("error occured " + e));
		}
	}

}
