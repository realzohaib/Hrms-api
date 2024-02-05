package com.erp.hrms.joblevelanddesignation.request_responseentity;

import java.util.List;

import com.erp.hrms.joblevelanddesignation.entity.SubDuties;

import lombok.Data;

@Data
public class DutiesResponse {

	private Integer designationId;
	private String designationName;
	private Integer dutiesId;
	private String dutyName;
	private List<SubDuties>subduties;


}
