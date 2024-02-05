package com.erp.hrms.joblevelanddesignation.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erp.hrms.joblevelanddesignation.entity.Designations;

@Repository
public interface DesignationRepo extends JpaRepository<Designations, Integer>{
	
	Designations findByDesignationId(int id);
	
	Designations findByDesignationName(String name);
		
    List<Designations> findByJoblevelLevelId(Integer levelId);


}
