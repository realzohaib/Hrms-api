package com.erp.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

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

	@Column(name = "secondary_supporting_document", length = 2147483647)
	
	@Lob
	private byte[] secondaryDocumentScan;

	// for 12th updated later
	@Column(name = "Senior_Secondary_issuing_authority")
	private String seniorSecondaryIssuingAuthority;

	@Column(name = "Senior_Secondary_marks_percentage_grade")
	private String seniorSecondaryMarksOrGrade;

	@Column(name = "Senior_Secondary_year")
	private String seniorSecondaryYear;

	@Column(name = "Senior_Secondary_supporting_document", length = 2147483647)
	@Lob
	private byte[] seniorSecondaryDocumentScan;

	// for graduation
	@Column(name = "Graduation_issuing_authority")
	private String graduationIssuingAuthority;

	@Column(name = "Graduation_marks_percentage_grade")
	private String graduationMarksOrGrade;

	@Column(name = "Graduation_year")
	private String graduationYear;

	@Column(name = "Graduation_supporting_document", length = 2147483647)
	@Lob
	private byte[] graduationDocumentScan;

	// for PG
	@Column(name = "Post_Graduation_issuing_authority")
	private String postGraduationIssuingAuthority;

	@Column(name = "Post_Graduation_marks_percentage_grade")
	private String postGraduationMarksOrGrade;

	@Column(name = "post_Graduation_year")
	private String postGraduationYear;

	@Column(name = "post_Graduation_supporting_document", length = 2147483647)
	@Lob
	private byte[] postGraduationDocumentScan;


// diploma certificates
	@Column(name = "diploma_issuing_authority")
	private String diplomaIssuingAuthority;

	@Column(name = "diploma_marks_percentage_grade")
	private String diplomaMarksOrGrade;

	@Column(name = "diploma_year")
	private String diplomaYear;

	// dynamic
	@Column(name = "diploma_document_scan", length = 2147483647)
	@Lob
	private byte[] diplomaDocumentScan;

	@ManyToOne
	@JoinColumn(name = "employee_id ")
	@JsonBackReference
	private PersonalInfo personalinfo;

}
