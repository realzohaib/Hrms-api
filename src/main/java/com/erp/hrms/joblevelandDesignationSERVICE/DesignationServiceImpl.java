package com.erp.hrms.joblevelandDesignationSERVICE;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.joblevelandDesignationEntity.Designations;
import com.erp.hrms.joblevelandDesignationEntity.Duties;
import com.erp.hrms.joblevelandDesignationEntity.JobLevel;
import com.erp.hrms.joblevelandDesignationREPO.DesignationRepo;
import com.erp.hrms.joblevelandDesignationREPO.joblevelRepo;
import com.erp.hrms.joblevelandDesignationREQ_RES.DesignationRequest;
import com.erp.hrms.joblevelandDesignationREQ_RES.DesignationResponse;

@Service
public class DesignationServiceImpl implements IDesignationService {

	@Autowired
	private DesignationRepo repo;

	@Autowired
	private joblevelRepo levelrepo;

	@Override
	public List<DesignationResponse> loadAllDesignations() {
		// Initialize a list to hold the response data
		List<DesignationResponse> responseList = new ArrayList<>();

		List<Duties> dutyList = new ArrayList<Duties>();

		// Retrieve all designations from the service
		List<Designations> allDesignations = repo.findAll();

		// Iterate through each designation and assemble the response
		for (Designations designation : allDesignations) {
			DesignationResponse response = new DesignationResponse();

			Integer designationId = designation.getDesignationId();
			String designationName = designation.getDesignationName();
			List<Duties> dutiesList = designation.getDuties();
			JobLevel level = designation.getJoblevel();

			response.setDesignationId(designationId);
			response.setDesignationName(designationName);
			response.setJobLevel(level.getLevelName());
			response.setJobLevelID(level.getLevelId());

			for (Duties duty : dutiesList) {
				Duties duties = new Duties();

				duties.setDutiesId(duty.getDutiesId());
				duties.setDutyName(duty.getDutyName());

				dutyList.add(duties);

			}
			response.setDuties(dutiesList);

			responseList.add(response);

		}

		return responseList;

	}

	@Override
	public List<Designations> savedesigDesignations(DesignationRequest designation) {
		Integer joblevelId = designation.getJoblevelId();
		JobLevel joblevel = levelrepo.findByLevelId(joblevelId);
		List<String> designationNames = designation.getDesignationName();

		List<Designations> savedDesignations = designationNames.stream()
				.map(name -> new Designations(null, name, null, joblevel)).collect(Collectors.toList());

		repo.saveAll(savedDesignations);

		return savedDesignations;
	}

	@Override
	public Designations updateDesignation(DesignationResponse designations) {
		Integer designationId = designations.getDesignationId();

		Integer jobLevelID = designations.getJobLevelID();

		JobLevel level = levelrepo.findByLevelId(jobLevelID);

		Designations designation = repo.findByDesignationId(designationId);

		designation.setJoblevel(level);
		return repo.save(designation);
	}

	@Override
	public DesignationResponse loadDesignationById(Integer Id) {
		DesignationResponse response = new DesignationResponse();
		
		Designations designation = repo.findByDesignationId(Id);
		
		Integer levelId = designation.getJoblevel().getLevelId();
		String levelName = designation.getJoblevel().getLevelName();
		Integer designationId = designation.getDesignationId();
		String designationName = designation.getDesignationName();
		List<Duties> duties = designation.getDuties();
		
		response.setJobLevelID(levelId);
		response.setJobLevel(levelName);
		response.setDesignationId(designationId);
		response.setDesignationName(designationName);
		response.setDuties(duties);
		
		return response;
	}

}
