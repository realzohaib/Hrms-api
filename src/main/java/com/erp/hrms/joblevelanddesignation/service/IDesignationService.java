package com.erp.hrms.joblevelanddesignation.service;

import java.util.List;

import com.erp.hrms.joblevelanddesignation.entity.Designations;
import com.erp.hrms.joblevelanddesignation.request_responseentity.DesignationRequest;
import com.erp.hrms.joblevelanddesignation.request_responseentity.DesignationResponse;

public interface IDesignationService {
	
	public List<Designations> savedesigDesignations(DesignationRequest designation);
	
	public List<?> loadAllDesignations();
	
	public Designations updateDesignation(DesignationResponse designations);
	
	public DesignationResponse loadDesignationById(Integer Id);
	
	public List<DesignationResponse> loadAllDesignationBYlevelId(Integer levelId);

}
