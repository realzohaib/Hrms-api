package com.erp.hrms.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.erp.hrms.exception.PersonalEmailExistsException;
import com.erp.hrms.exception.PersonalInfoNotFoundException;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("*")
public class PersonalInfoController {

	@Autowired
	private IPersonalInfoService personalInfoService;

	@PostMapping("/personal-info")
	public ResponseEntity<?> savePersonalInfo(@RequestParam("PersonalInfo") String personalinfo,
			@RequestParam("CurrentDesignationandAdditionalTask") String CurrentDesignationandAdditionalTask,
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
			@RequestParam(value = "achievementsRewardsDocs", required = false) MultipartFile[] achievementsRewardsDocs,
			HttpServletRequest req) throws IOException {

		String url = req.getRequestURL().toString();
		url = url.replace(req.getServletPath(), "");

		try {
			personalInfoService.savedata(personalinfo,CurrentDesignationandAdditionalTask, passportSizePhoto, OtherIdProofDoc,
					passportScan, licensecopy, relativeid, raddressproof, secondaryDocumentScan,
					seniorSecondaryDocumentScan, graduationDocumentScan, postGraduationDocumentScan, othersDocumentScan,
					degreeScan, payslipScan, recordsheet, PaidTrainingDocumentProof, CertificateUploadedForOutsource,
					visaDocs, diplomaDocumentScan, declarationRequired, achievementsRewardsDocs);
			return ResponseEntity.ok(new MessageResponse("Insert Personal info successfully"));
		} catch (PersonalEmailExistsException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Email ID already exists"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageResponse("An error occurred: " + e.getMessage()));
		}
	}

	@GetMapping("/personal-info/find/all/active")
	public ResponseEntity<?> findAllPersonalInfo() {
		try {
			List<PersonalInfo> findAllPersonalInfo = personalInfoService.findAllPersonalInfo();
			return ResponseEntity.ok(findAllPersonalInfo);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Exception occurred: " + e.getMessage());
		}
	}

	@GetMapping("/personal-info/email/{email}")
	public ResponseEntity<?> getPersonalInfoByEmail(@PathVariable String email, HttpServletResponse response)
			throws IOException {
		try {
			PersonalInfo personalInfo = personalInfoService.getPersonalInfoByEmail(email);
			return ResponseEntity.ok(personalInfo);
		} catch (PersonalInfoNotFoundException e) {
			return ResponseEntity.badRequest()
					.body(new MessageResponse("Personal information not found: " + e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error occurred: " + e.getMessage()));
		}
	}

	@GetMapping("/personal-info/employeeId/{employeeId}")
	public ResponseEntity<?> getPersonalInfoByEmployeeId(@PathVariable Long employeeId) throws IOException {
		try {
			PersonalInfo personalInfo = personalInfoService.getPersonalInfoByEmployeeId(employeeId);
			return ResponseEntity.ok(personalInfo);
		} catch (PersonalInfoNotFoundException e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error occurred: " + e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error occurred: " + e.getMessage()));
		}
	}

	@PutMapping("/personal-info/delete/{email}")
	public ResponseEntity<?> deletePersonalInfoByEmail(@PathVariable String email,
			@RequestParam("PersonalInfo") String personalInfo) throws IOException {
		try {
			PersonalInfo deletedInfo = personalInfoService.deletePersonalInfoByEmail(email, personalInfo);
			if (deletedInfo != null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body(new MessageResponse("No personal information found for this email ID: " + email
								+ " or this email is inactive ---"));
			} else {

				return ResponseEntity.ok(new MessageResponse("Record deleted successfully"));
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(new MessageResponse("An error occurred: " + e.getMessage()));
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
			PersonalInfo updatedPersonalInfo = personalInfoService.updatePersonalInfo(email, personalinfo,
					passportSizePhoto, OtherIdProofDoc, passportScan, licensecopy, relativeid, raddressproof,
					secondaryDocumentScan, seniorSecondaryDocumentScan, graduationDocumentScan,
					postGraduationDocumentScan, othersDocumentScan, degreeScan, payslipScan, recordsheet,
					PaidTrainingDocumentProof, CertificateUploadedForOutsource, visaDocs, diplomaDocumentScan,
					declarationRequired, achievementsRewardsDocs);

			if (updatedPersonalInfo != null) {
				return ResponseEntity.ok(new MessageResponse("Record updated successfully"));
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (PersonalInfoNotFoundException e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error occurred: " + e.getMessage()));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error occurred: " + e.getMessage()));
		}
	}

	@GetMapping("/dashboard")
	public ResponseEntity<?> dashbord() {
		try {
			return ResponseEntity.ok(new StatusResponse(true));
		} catch (Exception e) {
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
			return ResponseEntity.badRequest().body(new Exception("error occured " + e));
		}
	}

}
