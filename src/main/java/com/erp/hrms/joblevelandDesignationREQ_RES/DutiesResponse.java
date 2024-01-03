package com.erp.hrms.joblevelandDesignationREQ_RES;

import java.util.List;

import com.erp.hrms.joblevelandDesignationEntity.SubDuties;

import lombok.Data;

@Data
public class DutiesResponse {

	private Integer designationId;
	private String designationName;
	private Integer dutiesId;
	private String dutyName;
	private List<SubDuties>subduties;


}
