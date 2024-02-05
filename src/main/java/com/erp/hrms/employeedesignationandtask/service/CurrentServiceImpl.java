package com.erp.hrms.employeedesignationandtask.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.erp.hrms.api.dao.IjobDetailsDAO;
import com.erp.hrms.api.dao.PersonalInfoDAO;
import com.erp.hrms.employeedesignationandtask.dao.ICurrentDsAndTskRepo;
import com.erp.hrms.employeedesignationandtask.entity.CurrentDesignationAndTask;
import com.erp.hrms.employeedesignationandtask.requestresponseentity.CurrentReq;
import com.erp.hrms.employeedesignationandtask.requestresponseentity.CurrentRes;
import com.erp.hrms.entity.JobDetails;
import com.erp.hrms.entity.PersonalInfo;
import com.erp.hrms.joblevelanddesignation.dao.DesignationRepo;
import com.erp.hrms.joblevelanddesignation.dao.TaskRepo;
import com.erp.hrms.joblevelanddesignation.entity.Designations;
import com.erp.hrms.joblevelanddesignation.entity.Duties;
import com.erp.hrms.joblevelanddesignation.entity.SubDuties;
import com.erp.hrms.joblevelanddesignation.entity.Task;
import com.erp.hrms.joblevelanddesignation.request_responseentity.DesignationResponse;
import com.erp.hrms.locationdao.LocationRepository;
import com.erp.hrms.locationentity.Location;

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

	@Autowired
	@Lazy
	private PersonalInfoDAO personalInfoservice;

	@Autowired
	private IjobDetailsDAO jobrepo;

	/*
	 * earlier we were accepting multiple designation , location and additional task
	 * data in a list as their respective ids to assign multiple task and other data
	 * in a single submit, but later we decided not to save all the details in one
	 * submit , but one by one to keep uniqueness of employee and his corresponding
	 * designation , additional task, and location. we did this because if there are
	 * multiple task assigned to the emplyoyee and we want to end one of those task
	 * then we can simply end that task by just its currentdesignationtaskId becasue
	 * it is an independent record.
	 */

	@Override
	@org.springframework.transaction.annotation.Transactional
	public void saveCurrent(CurrentReq current) throws IOException {
		CurrentDesignationAndTask currentDesignationAndTask = new CurrentDesignationAndTask();

		currentDesignationAndTask.setEmpId(current.getEmpId());
		currentDesignationAndTask.setEndDate(current.getEndDate());
		currentDesignationAndTask.setStartDate(current.getStartDate());
		currentDesignationAndTask.setLevelId(current.getLevelId());

		List<JobDetails> jobDetails = null;
		PersonalInfo employee = personalInfoservice.loadPersonalInfoByEmployeeId(current.getEmpId());
		if (employee != null) {
			jobDetails = employee.getJobDetails();
		}

		List<Designations> designationList = new ArrayList<>();
		for (Integer designationId : current.getDesignationId()) {
			designationList.add(desigrepo.findByDesignationId(designationId));
		}
		currentDesignationAndTask.setDesignation(designationList);

		List<Task> taskList = new ArrayList<>();
		for (Integer taskId : current.getTaskId()) {
			taskList.add(taskrepo.findByTaskId(taskId));
		}
		currentDesignationAndTask.setTask(taskList);

		List<Location> locationList = new ArrayList<>();
		for (Integer locationId : current.getLocationId()) {
			Location location = locationrepo.findByLocationId(locationId);

			if (employee != null) {
				for (JobDetails jd : jobDetails) {
					String postedLocation = jd.getPostedLocation();

					if (!locationId.toString().equals(postedLocation)) {
						jd.setPostedLocation(locationId.toString());
						jobrepo.save(jd);
					}
				}
			}
			locationList.add(location);
		}
		currentDesignationAndTask.setLocation(locationList);

		repo.save(currentDesignationAndTask);
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
							subduty.setTask(new ArrayList<>()); // Clear the task list, to reduce the server loading
																// time
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

	@Override
	public List<CurrentDesignationAndTask> findAllEmpByLevelId(Integer levelId) {
		// return repo.findByLevelId(levelId);
		List<CurrentDesignationAndTask> findByLevelId = repo.findByLevelId(levelId);
		for (CurrentDesignationAndTask obj : findByLevelId) {
			List<Designations> designationlist = obj.getDesignation();
			for (Designations designation : designationlist) {
				designation.setDuties(null);
			}
			obj.setTask(null);

		}
		return findByLevelId;
	}

}
