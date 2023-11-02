package com.erp.hrms.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

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
	private String Raddressproof;
}
