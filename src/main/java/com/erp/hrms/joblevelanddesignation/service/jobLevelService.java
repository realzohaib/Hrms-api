package com.erp.hrms.joblevelanddesignation.service;

import java.util.List;

import com.erp.hrms.joblevelanddesignation.entity.JobLevel;


public interface jobLevelService {
	
	public JobLevel savejoblevel(JobLevel joblevel);
	
	public List<JobLevel> loadAllJobLevel();
	
	public JobLevel getJobLevelById(Integer levelId);

}
