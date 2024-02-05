package com.erp.hrms.joblevelanddesignation.service;

import java.util.List;

import com.erp.hrms.joblevelanddesignation.entity.Task;
import com.erp.hrms.joblevelanddesignation.request_responseentity.TaskRequest;

public interface ITaskService {
	
	public void saveTask(TaskRequest req);
	
	public List<Task> loadAllTask();
	
	public Task laodTaskById(int id);

}
