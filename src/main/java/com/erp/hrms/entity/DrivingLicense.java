package com.erp.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Lob;

import lombok.Data;

@Data
@Embeddable
public class DrivingLicense {
	@Column(name = "driving_license_uae")
	private String drivinglicense;
	
	@Column(name= "license_type")
	private String licenseType;
	
	@Column(name = "own_vehicle")
	private String ownvehicle;
	@Column(name = "license_copy",length = 2097152)
	@Lob
	private byte[] licensecopy;
  
}
