package com.erp.hrms.EmpDesignation.SERVICE;

import java.io.IOException;
import java.util.List;

import com.erp.hrms.EmpDesignation.ENTITY.CurrentDesignationAndTask;
import com.erp.hrms.EmpDesignation.REQandRES.CurrentReq;
import com.erp.hrms.EmpDesignation.REQandRES.CurrentRes;

public interface ICurrentService {

	public void saveCurrent(CurrentReq obj) throws IOException;

	public List<CurrentRes> loadAllDesignationAndTaskByEmpId(long empid);

	public List<CurrentRes> loadAllActiveDesignationAndTaskByEmpId(long empid);

	public CurrentDesignationAndTask endCurrentTask(CurrentReq obj);

	public CurrentRes loadByCurrentId(Integer currentId);

	public List<CurrentDesignationAndTask> findAllEmpByLevelId(Integer levelId);

}
