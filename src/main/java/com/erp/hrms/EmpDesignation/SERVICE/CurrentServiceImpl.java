package com.erp.hrms.EmpDesignation.SERVICE;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.EmpDesignation.ENTITY.CurrentDesignationAndTask;
import com.erp.hrms.EmpDesignation.REPO.ICurrentDsAndTskRepo;
import com.erp.hrms.EmpDesignation.REQandRES.CurrentReq;
import com.erp.hrms.EmpDesignation.REQandRES.CurrentRes;
import com.erp.hrms.Location.entity.Location;
import com.erp.hrms.Location.repository.LocationRepository;
import com.erp.hrms.joblevelandDesignationEntity.Designations;
import com.erp.hrms.joblevelandDesignationEntity.Duties;
import com.erp.hrms.joblevelandDesignationEntity.SubDuties;
import com.erp.hrms.joblevelandDesignationEntity.Task;
import com.erp.hrms.joblevelandDesignationREPO.DesignationRepo;
import com.erp.hrms.joblevelandDesignationREPO.TaskRepo;
import com.erp.hrms.joblevelandDesignationREQ_RES.DesignationResponse;

@Service
public class CurrentServiceImpl implements ICurrentService {

	@Autowired
	private ICurrentDsAndTskRepo repo;

	@Autowired
	private DesignationRepo desigrepo;

	@Autowired
	private TaskRepo taskrepo;

	@Autowired
	private LocationRepository locationrepo;

	@Override
	@org.springframework.transaction.annotation.Transactional
	public void saveCurrent(CurrentReq current) {
		CurrentDesignationAndTask obj = new CurrentDesignationAndTask();

		obj.setEmpId(current.getEmpId());
		obj.setEndDate(current.getEndDate());
		obj.setStartDate(current.getStartDate());

		List<Integer> designationIdList = current.getDesignationId();
		List<Designations> designationList = new ArrayList<Designations>();
		for (Integer designationId : designationIdList) {
			Designations designations = desigrepo.findByDesignationId(designationId);
			designationList.add(designations);
		}
		obj.setDesignation(designationList);

		List<Integer> taskIdList = current.getTaskId();
		List<Task> taskList = new ArrayList<Task>();
		for (Integer taskId : taskIdList) {
			Task task = taskrepo.findByTaskId(taskId);
			taskList.add(task);
		}
		obj.setTask(taskList);

		List<Location> locationList = new ArrayList<Location>();
		List<Integer> locationIdList = current.getLocationId();
		for (Integer locationId : locationIdList) {
			Location location = locationrepo.findByLocationId(locationId);
			locationList.add(location);
		}
		obj.setLocation(locationList);

		repo.save(obj);

	}

	// it fetches all the data of an employee , means current AND ended task also;
	@Override
	public List<CurrentRes> loadAllDesignationAndTaskByEmpId(long empid) {
		List<CurrentRes> responseList = new ArrayList<CurrentRes>();

		List<CurrentDesignationAndTask> obj = repo.findByEmpId(empid);
		for (CurrentDesignationAndTask current : obj) {
			CurrentRes res = new CurrentRes();
			res.setCurrentDesAndTaskId(current.getCurrentDsAndTskId());
			res.setEmpId(empid);
			res.setStartDate(current.getStartDate());
			res.setEndDate(current.getEndDate());

			List<DesignationResponse> designationList = new ArrayList<DesignationResponse>();

			// Designation
			// using DesignationResponse to minimize data in response ,if we directly use
			// designation it will give all data that is irrevalent
			List<Designations> designations = current.getDesignation();
			for (Designations desig : designations) {
				DesignationResponse designationResponse = new DesignationResponse();
				designationResponse.setDesignationId(desig.getDesignationId());
				designationResponse.setDesignationName(desig.getDesignationName());
				designationResponse.setJobLevel(desig.getJoblevel().getLevelName());
				designationResponse.setJobLevelID(desig.getJoblevel().getLevelId());

				designationList.add(designationResponse);
			}
			res.setDesignation(designationList);

			// Task
			List<Task> taskList = current.getTask();
			res.setAdditionaltask(taskList);

			// location
			List<Location> locationList = current.getLocation();
			res.setLocation(locationList);

			responseList.add(res);
		}
		return responseList;
	}

	// this method only load current data or duties that employee has to do
	@Override
	public List<CurrentRes> loadAllActiveDesignationAndTaskByEmpId(long empid) {
		List<CurrentRes> responseList = new ArrayList<CurrentRes>();

		List<CurrentDesignationAndTask> obj = repo.findByEmpId(empid);
		for (CurrentDesignationAndTask current : obj) {
			CurrentRes res = new CurrentRes();
			res.setCurrentDesAndTaskId(current.getCurrentDsAndTskId());
			res.setEmpId(empid);
			res.setStartDate(current.getStartDate());
			res.setEndDate(current.getEndDate());

			if (current.getEndDate() == null) {

				List<DesignationResponse> designationList = new ArrayList<DesignationResponse>();

				// Designation
				// using DesignationResponse to minimize data in response ,if we directly use
				// designation it will give all data that is irrevalent
				List<Designations> designations = current.getDesignation();
				for (Designations desig : designations) {
					DesignationResponse designationResponse = new DesignationResponse();
					designationResponse.setDesignationId(desig.getDesignationId());
					designationResponse.setDesignationName(desig.getDesignationName());
					designationResponse.setJobLevel(desig.getJoblevel().getLevelName());
					designationResponse.setJobLevelID(desig.getJoblevel().getLevelId());

					List<Duties> duties = desig.getDuties();
					for (Duties duty : duties) {
						for (SubDuties subduty : duty.getSubduties()) {
							subduty.setTask(new ArrayList<>()); // Clear the task list, to reduce the server loading time
						}
					}

					designationResponse.setDuties(duties);
					designationList.add(designationResponse);
				}
				res.setDesignation(designationList);

				// Task
				List<Task> taskList = current.getTask();
				res.setAdditionaltask(taskList);

				// location
				List<Location> locationList = current.getLocation();
				res.setLocation(locationList);

				responseList.add(res);

			}
		}
		return responseList;
	}

	@Override
	public CurrentDesignationAndTask endCurrentTask(CurrentReq obj) {
		Integer currentDsAndTskId = obj.getCurrentId();
		CurrentDesignationAndTask current = repo.findByCurrentDsAndTskId(currentDsAndTskId);
		current.setEndDate(obj.getEndDate());
		return repo.save(current);
	}

	@Override
	public CurrentRes loadByCurrentId(Integer currentId) {
		CurrentDesignationAndTask current = repo.findByCurrentDsAndTskId(currentId);

		CurrentRes res = new CurrentRes();
		res.setCurrentDesAndTaskId(current.getCurrentDsAndTskId());
		res.setEmpId(current.getEmpId());
		res.setStartDate(current.getStartDate());
		res.setEndDate(current.getEndDate());

		List<DesignationResponse> designationList = new ArrayList<DesignationResponse>();

		// Designation
		// using DesignationResponse to minimize data in response ,if we directly use
		// designation it will give all data that is irrevalent
		List<Designations> designations = current.getDesignation();
		for (Designations desig : designations) {
			DesignationResponse designationResponse = new DesignationResponse();
			designationResponse.setDesignationId(desig.getDesignationId());
			designationResponse.setDesignationName(desig.getDesignationName());
			designationResponse.setJobLevel(desig.getJoblevel().getLevelName());
			designationResponse.setJobLevelID(desig.getJoblevel().getLevelId());

			designationList.add(designationResponse);
		}
		res.setDesignation(designationList);

		// Task
		List<Task> taskList = current.getTask();
		res.setAdditionaltask(taskList);

		// location
		List<Location> locationList = current.getLocation();
		res.setLocation(locationList);

		return res;
	}

}
