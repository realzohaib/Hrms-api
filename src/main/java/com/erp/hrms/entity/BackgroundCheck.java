
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

	private String recordsheet;

	@Transient
	private byte[] recordSheetData;

	public void setRecordSheetData(byte[] recordSheetData) {
		if (recordSheetData != null && recordSheetData.length <= 100 * 1024) {
			this.recordSheetData = recordSheetData;
		} else {
			throw new IllegalArgumentException("Record Sheet Data size exceeds the allowed limit (100 KB)");
		}
	}

	private String declarationRequired;

	@Transient
	private byte[] declarationRequiredData;

	public void setDeclarationRequiredData(byte[] declarationRequiredData) {
		if (declarationRequiredData != null && declarationRequiredData.length <= 100 * 1024) {
			this.declarationRequiredData = declarationRequiredData;
		} else {
			throw new IllegalArgumentException("Declaration Required Data size exceeds the allowed limit (100 KB)");
		}
	}

	@ManyToOne
	@JoinColumn(name = "employee_id")
	@JsonBackReference
	private PersonalInfo personalinfo;
}
