package com.erp.hrms.employeedesignationandtask.requestresponseentity;

import java.util.List;

import com.erp.hrms.joblevelanddesignation.entity.Task;
import com.erp.hrms.joblevelanddesignation.request_responseentity.DesignationResponse;
import com.erp.hrms.locationentity.Location;

import lombok.Data;

@Data
public class CurrentRes {
	private long currentDesAndTaskId;
	private Long empId;
	private String startDate;
	private String endDate;
	private List<Location>location;
	private List<DesignationResponse>designation;
	private List<Task>additionaltask;

}
