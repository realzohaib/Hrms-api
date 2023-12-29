package com.erp.hrms.joblevelandDesignationSERVICE;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.joblevelandDesignationEntity.Designations;
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

		// Retrieve all designations from the service
		List<Designations> allDesignations = repo.findAll();

		// Iterate through each designation and assemble the response
		for (Designations designation : allDesignations) {
			// Create a new Designation object for response
			Designations responseDesignation = new Designations();
			// Create a new DesignationResponse object
			DesignationResponse designationResponse = new DesignationResponse();

			// Assemble Data for Response
			responseDesignation.setDesignationId(designation.getDesignationId());
			responseDesignation.setDesignationName(designation.getDesignationName());

			// Fetch JobLevel data associated with the designation
			JobLevel jobLevel = designation.getJoblevel();

			// Finalize the response data
			designationResponse.setDesignation(responseDesignation);
			designationResponse.setJobLevel(jobLevel.getLevelName());
			designationResponse.setJobLevelID(jobLevel.getLevelId());

			// Add the response to the list
			responseList.add(designationResponse);
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
		Integer designationId = designations.getDesignation().getDesignationId();
		
		Integer jobLevelID = designations.getJobLevelID();
		
		JobLevel level = levelrepo.findByLevelId(jobLevelID);
		
		Designations designation = repo.findByDesignationId(designationId);
		
		designation.setJoblevel(level);
		return repo.save(designation);
	}

}
