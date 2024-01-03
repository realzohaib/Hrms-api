package com.erp.hrms.joblevelandDesignationREPO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erp.hrms.joblevelandDesignationEntity.Duties;

@Repository
public interface TaskRepo extends JpaRepository<Duties, Integer>{
	
	Duties findByDutiesId(int dutiesId);

}
