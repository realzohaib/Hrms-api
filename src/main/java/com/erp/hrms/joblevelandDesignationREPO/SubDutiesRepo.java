package com.erp.hrms.joblevelandDesignationREPO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erp.hrms.joblevelandDesignationEntity.SubDuties;

@Repository
public interface SubDutiesRepo extends JpaRepository<SubDuties, Integer>{
	
	public SubDuties findBySubDutiesId(Integer id);
	
	

}
