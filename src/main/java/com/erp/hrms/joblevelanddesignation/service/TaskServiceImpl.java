package com.erp.hrms.joblevelanddesignation.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.joblevelanddesignation.dao.SubDutiesRepo;
import com.erp.hrms.joblevelanddesignation.dao.TaskRepo;
import com.erp.hrms.joblevelanddesignation.entity.SubDuties;
import com.erp.hrms.joblevelanddesignation.entity.Task;
import com.erp.hrms.joblevelanddesignation.request_responseentity.TaskRequest;

@Service
public class TaskServiceImpl implements ITaskService {

	@Autowired
	private SubDutiesRepo repo;
	
	@Autowired
	private TaskRepo taskrepo;

	@Override
	public void saveTask(TaskRequest req) {

		List<Task> list = new ArrayList();

		List<String> taskName = req.getTaskName();
		Integer subDutiesId = req.getSubDutiesId();

		SubDuties subDuties = repo.findBySubDutiesId(subDutiesId);

		for (String name : taskName) {
			
			if(taskrepo.findByTaskName(name) != null) {
				throw new IllegalStateException("Task: "+
			name +" Already exist");	
			}

			Task obj = new Task();

			obj.setTaskName(name);

			list.add(obj);
		}
		
		subDuties.setTask(list);
		repo.save(subDuties);
	}

	@Override
	public List<Task> loadAllTask() {
		return taskrepo.findAll();
	}

	@Override
	public Task laodTaskById(int id) {
		return taskrepo.findByTaskId(id);
	}

}
