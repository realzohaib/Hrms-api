package com.erp.hrms.joblevelandDesignationSERVICE;

import java.util.List;

import com.erp.hrms.joblevelandDesignationEntity.Designations;
import com.erp.hrms.joblevelandDesignationREQ_RES.DesignationRequest;
import com.erp.hrms.joblevelandDesignationREQ_RES.DesignationResponse;

public interface IDesignationService {
	
	public List<Designations> savedesigDesignations(DesignationRequest designation);
	
	public List<?> loadAllDesignations();
	
	public Designations updateDesignation(DesignationResponse designations);
	
	public DesignationResponse loadDesignationById(Integer Id);

}
