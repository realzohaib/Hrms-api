package com.erp.hrms.joblevelanddesignation.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erp.hrms.joblevelanddesignation.entity.Task;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long>{
	public Task findByTaskId(int id);
	
	public Task findByTaskName(String taskName);

	

}
