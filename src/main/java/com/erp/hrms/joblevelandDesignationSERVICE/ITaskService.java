package com.erp.hrms.joblevelandDesignationSERVICE;

import java.util.List;

import com.erp.hrms.joblevelandDesignationEntity.Task;
import com.erp.hrms.joblevelandDesignationREQ_RES.TaskRequest;

public interface ITaskService {
	
	public void saveTask(TaskRequest req);
	
	public List<Task> loadAllTask();
	
	public Task laodTaskById(int id);

}
