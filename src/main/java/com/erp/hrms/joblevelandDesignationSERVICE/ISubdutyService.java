package com.erp.hrms.joblevelandDesignationSERVICE;

import java.util.List;

import com.erp.hrms.joblevelandDesignationEntity.SubDuties;
import com.erp.hrms.joblevelandDesignationREQ_RES.SubDutiesRequest;

public interface ISubdutyService {

	public void saveSubduty(SubDutiesRequest req);
	
	public List<SubDuties> loadAllSubduties();
	
	
}
