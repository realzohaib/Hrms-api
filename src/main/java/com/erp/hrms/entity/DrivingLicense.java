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

	public void setLicenseCopyData(byte[] licenseCopyData) {
		validateAndSetData(licenseCopyData, "License Copy Data");
	}

	private void validateAndSetData(byte[] data, String dataType) {
		if (data != null && data.length <= 100 * 1024) {
			if ("License Copy Data".equals(dataType)) {
				this.licenseCopyData = data;
			}
		} else {
			throw new IllegalArgumentException(dataType + " size exceeds the allowed limit (100 KB)");
		}
	}

}
