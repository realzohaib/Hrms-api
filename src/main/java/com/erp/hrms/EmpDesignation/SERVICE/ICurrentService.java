package com.erp.hrms.EmpDesignation.SERVICE;

import java.awt.PrintGraphics;
import java.util.List;

import com.erp.hrms.EmpDesignation.ENTITY.CurrentDesignationAndTask;
import com.erp.hrms.EmpDesignation.REQandRES.CurrentReq;
import com.erp.hrms.EmpDesignation.REQandRES.CurrentRes;

public interface ICurrentService {
	
	public void saveCurrent(CurrentReq obj);
	
	public List<CurrentRes> loadAllDesignationAndTaskByEmpId(long empid);
	
	public List<CurrentRes> loadAllActiveDesignationAndTaskByEmpId(long empid);
	
	public CurrentDesignationAndTask endCurrentTask(CurrentReq obj);
	
	public CurrentRes loadByCurrentId(Integer currentId);

}
