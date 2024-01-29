package com.erp.hrms.EmpDesignation.SERVICE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.erp.hrms.EmpDesignation.ENTITY.CurrentDesignationAndTask;
import com.erp.hrms.EmpDesignation.REPO.ICurrentDsAndTskRepo;
import com.erp.hrms.EmpDesignation.REQandRES.CurrentReq;
import com.erp.hrms.EmpDesignation.REQandRES.CurrentRes;
import com.erp.hrms.Location.entity.Location;
import com.erp.hrms.Location.repository.LocationRepository;
import com.erp.hrms.api.dao.IjobDetailsDAO;
import com.erp.hrms.api.service.PersonalInfoServiceImpl;
import com.erp.hrms.entity.JobDetails;
import com.erp.hrms.entity.PersonalInfo;
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

	@Autowired
	@Lazy
	private PersonalInfoServiceImpl personalInfoservice;

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
		CurrentDesignationAndTask obj = new CurrentDesignationAndTask();

		obj.setEmpId(current.getEmpId());
		obj.setEndDate(current.getEndDate());
		obj.setStartDate(current.getStartDate());
		obj.setLevelId(current.getLevelId());

//		PersonalInfo employee = personalInfoservice.getPersonalInfoByEmployeeId(current.getEmpId());
//		List<JobDetails> jobDetails = employee.getJobDetails();

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

//			for (JobDetails jd : jobDetails) {
//				String postedLocation = jd.getPostedLocation();
//
//				if (locationId != Integer.valueOf(postedLocation)) {
//					jd.setPostedLocation(String.valueOf(locationId));
//					jobrepo.save(jd);
//				}
//			}
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
