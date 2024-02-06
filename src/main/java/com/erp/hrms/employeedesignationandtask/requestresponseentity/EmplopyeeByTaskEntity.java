package com.erp.hrms.employeedesignationandtask.requestresponseentity;

import lombok.Data;

@Data
public class EmplopyeeByTaskEntity {
	private Integer currentDsAndTskId;
	private Long empId;
	private String empName;
	private String designationName;
	private String startDate;

}
