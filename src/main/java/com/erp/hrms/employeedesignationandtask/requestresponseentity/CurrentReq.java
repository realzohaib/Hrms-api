package com.erp.hrms.employeedesignationandtask.requestresponseentity;

import java.util.List;

import lombok.Data;

@Data
public class CurrentReq {
	private Integer currentId;
	private Long empId;
	private String startDate;
	private String endDate;
	private Integer levelId;
	private List<Integer>locationId;
	private List<Integer>designationId;
	private List<Integer>taskId;



}
