
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
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "background_check")
@Data
public class BackgroundCheck {
	@Id
	@Column(name = "background_check.personal_info_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String status;
	private String doneBy;
	private String internalConcernedManager;
	private String externalName;
	private String externalPost;
	
	private String externalCompanyName;
	private String externalPhoneCode;
	private String externalPhoneNo;
	private String managerApproval;
	private String managerName;
	
	private String remark;
	private String addressVerified;
	private String previousEmploymentStatusVerified;
	private String previousDesignationAndResponsibilityVerified;
	private String idProofDocumentVerified;
	
	private String educationalQualificationVerified;
	private String criminalRecords;
	private String punishmentForImprisonmentApproval;

	@Lob

	@Column(length = 2147483647)
	private byte[] recordsheet;

	@Lob
	@Column(length = 2147483647)
	private byte[] declarationRequired;
	
	@ManyToOne
	@JoinColumn(name = "employee_id")
	@JsonBackReference
	private PersonalInfo personalinfo;
}
