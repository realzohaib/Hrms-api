package com.erp.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Lob;

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
	@Column(name = "Passport_Docs",length = 2147483647)
	@Lob
	private byte[] passportScan;

}
