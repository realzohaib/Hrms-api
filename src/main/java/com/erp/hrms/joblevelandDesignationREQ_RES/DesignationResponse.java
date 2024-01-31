package com.erp.hrms.joblevelandDesignationREQ_RES;

import java.util.List;

import com.erp.hrms.joblevelandDesignationEntity.Duties;

import lombok.Data;

@Data
public class DesignationResponse {
	private Integer jobLevelID;
	private String jobLevel;
	private Integer designationId;
	private String designationName;
	private List<Duties> duties;
	

}