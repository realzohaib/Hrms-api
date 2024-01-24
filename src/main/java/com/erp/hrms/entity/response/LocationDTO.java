package com.erp.hrms.entity.response;

import lombok.Data;

@Data
public class LocationDTO {
	private Long locationId;
	private Long concernedAuthorityEmpId;
	private String name;
	private String address;
	private String latitude;
	private String longitude;
	private Boolean isMaintenanceRequired;
	private String commentsForMaintenance;
	private String country;
	private String inchargeInfo;
}