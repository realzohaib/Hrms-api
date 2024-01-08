package com.erp.hrms.joblevelandDesignationSERVICE;

import java.util.List;

import com.erp.hrms.joblevelandDesignationEntity.Duties;
import com.erp.hrms.joblevelandDesignationREQ_RES.DutiesRequest;
import com.erp.hrms.joblevelandDesignationREQ_RES.DutiesResponse;

public interface IDutiesService {
	
	public void saveDuties(DutiesRequest duties);
	
	public List<Duties>getallduties();
	
	public List<DutiesResponse> loadDutiesByDesignationId(int designationId);
	

}
