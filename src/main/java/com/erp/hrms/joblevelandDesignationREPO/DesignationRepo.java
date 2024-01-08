package com.erp.hrms.joblevelandDesignationREPO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erp.hrms.joblevelandDesignationEntity.Designations;

@Repository
public interface DesignationRepo extends JpaRepository<Designations, Integer>{
	
	Designations findByDesignationId(int id);
	
    List<Designations> findByJoblevelLevelId(Integer levelId);


}
