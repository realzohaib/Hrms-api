package com.erp.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Embeddable
public class BloodRelative {

	@Column(name = "relative_name_prefix")
	private String relativenamePrefix;

	@Column(name = "relative_first_name")
	private String RFirstname;
	@Column(name = "relative_middle_name")
	private String Rmiddlename;
	@Column(name = "relative_last_name")
	private String Rlastname;
	@Column(name = "relationship")
	private String relationship;

	@Column(name = "relative_phone_code")
	private String RphoneCode;
	@Column(name = "contact_no")
	private String Rcontactno;
	@Column(name = "address")
	private String Raddress;

	private String relativeid;

	@Transient
	private byte[] relativeIdData;

	private String Raddressproof;

	@Transient
	private byte[] raddressProofData;

	public void setRelativeIdData(byte[] relativeIdData) {
		validateAndSetData(relativeIdData, "Relative ID Data");
	}

	public void setRaddressProofData(byte[] raddressProofData) {
		validateAndSetData(raddressProofData, "Relative Address Proof Data");
	}

	private void validateAndSetData(byte[] data, String dataType) {
		if (data != null && data.length <= 100 * 1024) {
			if ("Relative ID Data".equals(dataType)) {
				this.relativeIdData = data;
			} else if ("Relative Address Proof Data".equals(dataType)) {
				this.raddressProofData = data;
			}
		} else {
			throw new IllegalArgumentException(dataType + " size exceeds the allowed limit (100 KB)");
		}
	}
}
