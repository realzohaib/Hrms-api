package com.erp.hrms.joblevelanddesignation.service;

import java.util.List;

import com.erp.hrms.joblevelanddesignation.entity.Duties;
import com.erp.hrms.joblevelanddesignation.request_responseentity.DutiesRequest;
import com.erp.hrms.joblevelanddesignation.request_responseentity.DutiesResponse;

public interface IDutiesService {
	
	public void saveDuties(DutiesRequest duties);
	
	public List<Duties>getallduties();
	
	public List<DutiesResponse> loadDutiesByDesignationId(int designationId);
	

}
