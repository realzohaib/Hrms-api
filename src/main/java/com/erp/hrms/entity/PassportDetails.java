package com.erp.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Embeddable
public class PassportDetails {

	@Column(name = "passport_issuing_country")
	private String passportIssuingCountry;
	@Column(name = "passport_number")
	private String passportNumber;
	@Column(name = "passport_expiry_date")
	private String passportExpiryDate;

	private String passportScan;
	
	@Transient
	private byte[] passportScanData;

}
