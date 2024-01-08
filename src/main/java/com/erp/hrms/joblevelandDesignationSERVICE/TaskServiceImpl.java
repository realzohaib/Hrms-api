package com.erp.hrms.joblevelandDesignationSERVICE;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.joblevelandDesignationEntity.Task;
import com.erp.hrms.joblevelandDesignationEntity.SubDuties;
import com.erp.hrms.joblevelandDesignationREPO.SubDutiesRepo;
import com.erp.hrms.joblevelandDesignationREPO.TaskRepo;
import com.erp.hrms.joblevelandDesignationREQ_RES.TaskRequest;

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
