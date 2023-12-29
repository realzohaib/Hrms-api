package com.erp.hrms.joblevelandDesignationSERVICE;

import java.util.List;

import com.erp.hrms.joblevelandDesignationEntity.Duties;
import com.erp.hrms.joblevelandDesignationREQ_RES.DutiesRequest;

public interface IDutiesService {
	
	public List<Duties> saveDuties(DutiesRequest duties);
	
	public List<Duties>getallduties();

}
