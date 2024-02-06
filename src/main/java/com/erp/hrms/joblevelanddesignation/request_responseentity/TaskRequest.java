package com.erp.hrms.joblevelanddesignation.request_responseentity;

import java.util.List;

import lombok.Data;

@Data
public class TaskRequest {
	
	private List<String> TaskName;
	private Integer subDutiesId;

}
