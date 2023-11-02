package com.erp.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Data
@Entity
@Table(name = "training_detail")
public class Trainingdetails {

	@Id
	@Column(name = "training_details.personal_info_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String trainingType;
	private String inHouseOutsource;
	private String paidUnpaid;
	private String trainingStartDate;
	private String trainingEndDate;
	private String trainerFeedback;

	private String trainerName;
	private String trainerPost;
	private String trainerDepartment;
	private String trainerPhoneCode;
	private String trainerPhoneNo;

	private String PaidTrainingDocumentProof;
	private String CertificateUploadedForOutsource;

	@ManyToOne
	@JoinColumn(name = "employee_id")
	@JsonBackReference
	private PersonalInfo personalinfo;

}
