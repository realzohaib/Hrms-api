package com.erp.hrms.EmpDesignation.REQandRES;

import java.util.List;

import com.erp.hrms.Location.entity.Location;
import com.erp.hrms.joblevelandDesignationEntity.Task;
import com.erp.hrms.joblevelandDesignationREQ_RES.DesignationResponse;

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
