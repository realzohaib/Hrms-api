package com.erp.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
@Entity
public class Education {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ed_id")
	private Long id;

	// for 10th
	@Column(name = "Secondary_issuing_authority")
	private String secondaryIssuingAuthority;

	@Column(name = "Secondary_marks_percentage_grade")
	private String secondarymarksOrGrade;

	@Column(name = "Secondary_year")
	private String secondaryyear;

	private String secondaryDocumentScan;

	@Transient
	private byte[] secondaryDocumentScanData;

	// for 12th
	@Column(name = "Senior_Secondary_issuing_authority")
	private String seniorSecondaryIssuingAuthority;

	@Column(name = "Senior_Secondary_marks_percentage_grade")
	private String seniorSecondaryMarksOrGrade;

	@Column(name = "Senior_Secondary_year")
	private String seniorSecondaryYear;

	private String seniorSecondaryDocumentScan;

	@Transient
	private byte[] seniorSecondaryDocumentScanData;

	// for graduation
	@Column(name = "Graduation_issuing_authority")
	private String graduationIssuingAuthority;

	@Column(name = "Graduation_marks_percentage_grade")
	private String graduationMarksOrGrade;

	@Column(name = "Graduation_year")
	private String graduationYear;

	private String graduationDocumentScan;

	@Transient
	private byte[] graduationDocumentScanData;

	// for PG
	@Column(name = "Post_Graduation_issuing_authority")
	private String postGraduationIssuingAuthority;

	@Column(name = "Post_Graduation_marks_percentage_grade")
	private String postGraduationMarksOrGrade;

	@Column(name = "post_Graduation_year")
	private String postGraduationYear;

	private String postGraduationDocumentScan;

	@Transient
	private byte[] postGraduationDocumentScanData;

// diploma certificates
	@Column(name = "diploma_issuing_authority")
	private String diplomaIssuingAuthority;

	@Column(name = "diploma_marks_percentage_grade")
	private String diplomaMarksOrGrade;

	@Column(name = "diploma_year")
	private String diplomaYear;

	private String diplomaDocumentScan;

	@Transient
	private byte[] diplomaDocumentScanData;

	public void setSecondaryDocumentScanData(byte[] secondaryDocumentScanData) {
		validateAndSetData(secondaryDocumentScanData, "Secondary Document Scan Data");
	}

	public void setSeniorSecondaryDocumentScanData(byte[] seniorSecondaryDocumentScanData) {
		validateAndSetData(seniorSecondaryDocumentScanData, "Senior Secondary Document Scan Data");
	}

	public void setGraduationDocumentScanData(byte[] graduationDocumentScanData) {
		validateAndSetData(graduationDocumentScanData, "Graduation Document Scan Data");
	}

	public void setPostGraduationDocumentScanData(byte[] postGraduationDocumentScanData) {
		validateAndSetData(postGraduationDocumentScanData, "Post Graduation Document Scan Data");
	}

	public void setDiplomaDocumentScanData(byte[] diplomaDocumentScanData) {
		validateAndSetData(diplomaDocumentScanData, "Diploma Document Scan Data");
	}

	private void validateAndSetData(byte[] data, String dataType) {
		if (data != null && data.length <= 100 * 1024) {
			switch (dataType) {
			case "Secondary Document Scan Data":
				this.secondaryDocumentScanData = data;
				break;
			case "Senior Secondary Document Scan Data":
				this.seniorSecondaryDocumentScanData = data;
				break;
			case "Graduation Document Scan Data":
				this.graduationDocumentScanData = data;
				break;
			case "Post Graduation Document Scan Data":
				this.postGraduationDocumentScanData = data;
				break;
			case "Diploma Document Scan Data":
				this.diplomaDocumentScanData = data;
				break;
			}
		} else {
			throw new IllegalArgumentException(dataType + " size exceeds the allowed limit (100 KB)");
		}
	}

	@ManyToOne
	@JoinColumn(name = "employee_Id")
	@JsonBackReference
	private PersonalInfo personalinfo;

}
