package com.erp.hrms.Location.entity.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LeaveApproverDto {

	private Long lAId;
	private Long firstApproverEmpId;
	private Long secondApproverEmpId;
	private String startDate;
	private String endDate; 

}
