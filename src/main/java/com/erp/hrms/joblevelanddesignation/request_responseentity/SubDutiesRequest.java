package com.erp.hrms.joblevelanddesignation.request_responseentity;

import java.util.List;

import lombok.Data;

@Data
public class SubDutiesRequest {
	
	private List<String>subDuties;
	private Integer dutiesIdl;

}
