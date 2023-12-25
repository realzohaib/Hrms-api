package com.erp.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Table(name = "professional_qualifications")
@Data
public class ProfessionalQualification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "p_id")
	private Long id;
	private String qualification;
	private String issuingAuthority;
	private String gradingSystem;
	private String yearOfQualification;
	private String grade;

	private String degreeScan;

	@Transient
	private byte[] degreeScanData;

	@ManyToOne
	@JoinColumn(name = "employee_id")
	@JsonBackReference
	private PersonalInfo personalinfo;
}
