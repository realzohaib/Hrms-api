package com.erp.hrms.joblevelanddesignation.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erp.hrms.joblevelanddesignation.entity.SubDuties;

@Repository
public interface SubDutiesRepo extends JpaRepository<SubDuties, Integer>{
	
	public SubDuties findBySubDutiesId(Integer id);
	
	public SubDuties findBySubDutyName(String Name);
	
	List<SubDuties> findAll();

	
	
	
	

}
