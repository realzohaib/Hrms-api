package com.erp.hrms.joblevelandDesignationREQ_RES;

import java.util.List;

import lombok.Data;

@Data
public class DesignationRequest {
	private List<String> designationName;
	private Integer joblevelId;

}
