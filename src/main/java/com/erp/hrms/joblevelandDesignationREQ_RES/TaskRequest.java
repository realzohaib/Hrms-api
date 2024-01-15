package com.erp.hrms.joblevelandDesignationREQ_RES;

import java.util.List;

import lombok.Data;

@Data
public class TaskRequest {
	
	private List<String> TaskName;
	private Integer subDutiesId;

}
