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
public class OthersQualification {

	// other certificates
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String others;
	@Column(name = "others_issuing_authority")
	private String othersIssuingAuthority;

	@Column(name = "others_marks_percentage_grade")
	private String othersMarksOrGrade;

	@Column(name = "others_year")
	private String othersYear;

	private String othersDocumentScan;

	@Transient
	private byte[] othersDocumentScanData;

	@ManyToOne
	@JoinColumn(name = "employee_id ")
	@JsonBackReference
	private PersonalInfo personalinfo;

}
