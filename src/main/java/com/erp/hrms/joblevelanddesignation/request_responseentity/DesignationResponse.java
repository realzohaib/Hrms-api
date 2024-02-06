package com.erp.hrms.joblevelanddesignation.request_responseentity;

import java.util.List;

import com.erp.hrms.joblevelanddesignation.entity.Duties;

import lombok.Data;

@Data
public class DesignationResponse {
	private Integer jobLevelID;
	private String jobLevel;
	private Integer designationId;
	private String designationName;
	private List<Duties> duties;
	

}
