package com.erp.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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

	@Lob
	@Column(length = 2097152)
	private byte[] CertificateUploadedForOutsource;
	@Lob
	@Column(length = 2097152)
	private byte[] PaidTrainingDocumentProof;
	
	@ManyToOne
	@JoinColumn(name = "employee_id")
	@JsonBackReference
	private PersonalInfo personalinfo;

}
