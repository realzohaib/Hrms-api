package com.erp.hrms.entity.response;

import java.util.List;

import lombok.Data;

@Data
public class LeaveApproverDTO {
	private Long LAId;
	private Long firstApproverEmpId;
	private String firstApproverEmail;
	private Long secondApproverEmpId;
	private String secondApproverEmail;
	private String startDate;
	private String endDate;
	private List<LocationDTO> locations;
	private List<String> approverLevels;

	private List<EmployeeResponseDTO> employeeData;

	private List<EmployeeNotificationDTO> employeeNotifications;
}