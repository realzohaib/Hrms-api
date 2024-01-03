package com.erp.hrms.joblevelandDesignationSERVICE;

import java.util.List;

import com.erp.hrms.joblevelandDesignationEntity.JobLevel;


public interface jobLevelService {
	
	public JobLevel savejoblevel(JobLevel joblevel);
	
	public List<JobLevel> loadAllJobLevel();
	
	public JobLevel getJobLevelById(Integer levelId);

}
