package com.erp.hrms.joblevelandDesignationREQ_RES;

import com.erp.hrms.joblevelandDesignationEntity.Designations;

import lombok.Data;

@Data
public class DesignationResponse {
	
	private Designations designation;
	private Integer jobLevelID;
	private String  jobLevel;
	

}
