package com.erp.hrms.locationentity.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LocationDto {

	private Long locationId;
	private Long concernedAuthorityEmpId;
	private String name;
	private String address;
	private String latitude;
	private String longitude;
	private Boolean isMaintenanceRequired;
	private String commentsForMaintenance;
	private String inchargeInfo;

	private List<LeaveApproverDto> leaveApprovers;
}
