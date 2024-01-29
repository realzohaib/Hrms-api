package com.erp.hrms.joblevelandDesignationREPO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erp.hrms.joblevelandDesignationEntity.SubDuties;

@Repository
public interface SubDutiesRepo extends JpaRepository<SubDuties, Integer>{
	
	public SubDuties findBySubDutiesId(Integer id);
	
	public SubDuties findBySubDutyName(String Name);
	
	List<SubDuties> findAll();

	
	
	
	

}
