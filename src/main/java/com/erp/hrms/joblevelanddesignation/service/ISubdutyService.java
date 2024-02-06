package com.erp.hrms.joblevelanddesignation.service;

import java.util.List;

import com.erp.hrms.joblevelanddesignation.entity.SubDuties;
import com.erp.hrms.joblevelanddesignation.request_responseentity.SubDutiesRequest;

public interface ISubdutyService {

	public void saveSubduty(SubDutiesRequest req);
	
	public List<SubDuties> loadAllSubduties();
	
	
}
