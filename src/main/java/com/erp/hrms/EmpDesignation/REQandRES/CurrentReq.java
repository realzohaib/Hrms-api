package com.erp.hrms.EmpDesignation.REQandRES;

import java.util.List;

import lombok.Data;

@Data
public class CurrentReq {
	private Integer currentId;
	private Long empId;
	private String startDate;
	private String endDate;
	private List<Integer>locationId;
	private List<Integer>designationId;
	private List<Integer>taskId;



}
