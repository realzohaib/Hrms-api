package com.erp.hrms.joblevelanddesignation.request_responseentity;

import java.util.List;

import lombok.Data;

@Data
public class DesignationRequest {
	private List<String> designationName;
	private Integer joblevelId;

}
