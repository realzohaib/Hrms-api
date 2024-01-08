package com.erp.hrms.joblevelandDesignationREPO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erp.hrms.joblevelandDesignationEntity.Task;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long>{
	public Task findByTaskId(int id);

}
