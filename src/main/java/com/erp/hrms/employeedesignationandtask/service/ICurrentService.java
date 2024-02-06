package com.erp.hrms.employeedesignationandtask.service;

import java.io.IOException;
import java.util.List;

import com.erp.hrms.employeedesignationandtask.entity.CurrentDesignationAndTask;
import com.erp.hrms.employeedesignationandtask.requestresponseentity.CurrentReq;
import com.erp.hrms.employeedesignationandtask.requestresponseentity.CurrentRes;
import com.erp.hrms.employeedesignationandtask.requestresponseentity.EmplopyeeByTaskEntity;

public interface ICurrentService {
	
	public void saveCurrent(CurrentReq obj)throws IOException;
	
	public List<CurrentRes> loadAllDesignationAndTaskByEmpId(long empid);
	
	public List<CurrentRes> loadAllActiveDesignationAndTaskByEmpId(long empid);
	
	public CurrentDesignationAndTask endCurrentTask(CurrentReq obj);
	
	public CurrentRes loadByCurrentId(Integer currentId);
	
	public List<CurrentDesignationAndTask>findAllEmpByLevelId(Integer levelId);
	
	public  List<EmplopyeeByTaskEntity> findAllEmpByTaskId(Integer taskId);
	
	

}
