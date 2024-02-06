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

	public void setDegreeScanData(byte[] degreeScanData) {
		validateAndSetData(degreeScanData, "Degree Scan Data");
	}

	private void validateAndSetData(byte[] data, String dataType) {
		if (data != null && data.length <= 100 * 1024) {
			if ("Degree Scan Data".equals(dataType)) {
				this.degreeScanData = data;
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
