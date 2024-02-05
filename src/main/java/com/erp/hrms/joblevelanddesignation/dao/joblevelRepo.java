package com.erp.hrms.joblevelanddesignation.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.erp.hrms.joblevelanddesignation.entity.JobLevel;

@Repository
public interface joblevelRepo extends JpaRepository<JobLevel, Integer> {
	
	public JobLevel findByLevelId(int id);
	
	public JobLevel findByLevelName(String levelName);
	
	@Query("SELECT jl FROM JobLevel jl LEFT JOIN FETCH jl.designations")
	public List<JobLevel> loadAllJobLevel();

}
