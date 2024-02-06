package com.erp.hrms.joblevelanddesignation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.joblevelanddesignation.dao.joblevelRepo;
import com.erp.hrms.joblevelanddesignation.entity.JobLevel;

@Service
public class joblevelServiceImpl implements jobLevelService{
	
	@Autowired
	private joblevelRepo repo;
	

	@Override
	public JobLevel savejoblevel(JobLevel joblevel) {
		return repo.save(joblevel);
	}

	@Override
	public List<JobLevel> loadAllJobLevel() {
		return repo.findAll();
	}

	@Override
	public JobLevel getJobLevelById(Integer levelId) {
		return repo.findByLevelId(levelId);
	}

}
