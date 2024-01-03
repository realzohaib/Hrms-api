package com.erp.hrms.joblevelandDesignationSERVICE;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.joblevelandDesignationEntity.JobLevel;
import com.erp.hrms.joblevelandDesignationREPO.joblevelRepo;

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
