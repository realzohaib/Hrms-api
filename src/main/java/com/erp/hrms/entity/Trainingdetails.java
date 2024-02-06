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

	@Transient
	private byte[] PaidTrainingDocumentProofData;

	private String CertificateUploadedForOutsource;

	@Transient
	private byte[] CertificateUploadedForOutsourceData;

	public void setPaidTrainingDocumentProofData(byte[] paidTrainingDocumentProofData) {
		validateAndSetData(paidTrainingDocumentProofData, "Paid Training Document Proof Data");
	}

	public void setCertificateUploadedForOutsourceData(byte[] certificateUploadedForOutsourceData) {
		validateAndSetData(certificateUploadedForOutsourceData, "Certificate Uploaded For Outsource Data");
	}

	private void validateAndSetData(byte[] data, String dataType) {
		if (data != null && data.length <= 100 * 1024) {
			switch (dataType) {
			case "Paid Training Document Proof Data":
				this.PaidTrainingDocumentProofData = data;
				break;
			case "Certificate Uploaded For Outsource Data":
				this.CertificateUploadedForOutsourceData = data;
				break;
			}
		} else {
			throw new IllegalArgumentException(dataType + " size exceeds the allowed limit (100 KB)");
		}
	}

	@ManyToOne
	@JoinColumn(name = "employee_id")
	@JsonBackReference
	private PersonalInfo personalinfo;

}
