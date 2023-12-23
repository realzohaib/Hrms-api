package com.erp.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Embeddable
public class DrivingLicense {

	@Column(name = "driving_license_uae")
	private String drivinglicense;

	@Column(name = "license_type")
	private String licenseType;

	@Column(name = "own_vehicle")
	private String ownvehicle;

	private String licensecopy;
	
	@Transient
	private byte[] licenseCopyData;

}
