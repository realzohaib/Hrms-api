package com.erp.hrms.joblevelanddesignation.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.erp.hrms.joblevelanddesignation.entity.Duties;

@Repository
public interface DutiesRepo extends JpaRepository<Duties, Integer>{
	
	Duties findByDutiesId(int dutiesId);
	
	Duties findByDutyName(String name);
	
	@Query("SELECT d FROM Duties d JOIN d.designation designations WHERE designations.id = :designationId")
	List<Duties> findByDesignationId(@Param("designationId") int designationId);


}
