package com.erp.hrms.api.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erp.hrms.entity.JobDetails;

@Repository
public interface IjobDetailsDAO extends JpaRepository<JobDetails, Long>{
	
	

}
