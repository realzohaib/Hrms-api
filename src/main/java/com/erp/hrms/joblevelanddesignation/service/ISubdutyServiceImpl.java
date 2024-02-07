package com.erp.hrms.joblevelanddesignation.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.joblevelanddesignation.dao.DutiesRepo;
import com.erp.hrms.joblevelanddesignation.dao.SubDutiesRepo;
import com.erp.hrms.joblevelanddesignation.entity.Duties;
import com.erp.hrms.joblevelanddesignation.entity.SubDuties;
import com.erp.hrms.joblevelanddesignation.request_responseentity.SubDutiesRequest;

@Service
public class ISubdutyServiceImpl implements ISubdutyService {

	@Autowired
	private DutiesRepo repo;

	@Autowired
	private SubDutiesRepo subrepo;

//	@Override
//	public void saveSubduty(SubDutiesRequest req) {
//		
//		ArrayList<SubDuties> list = new ArrayList();
//		Duties duties = repo.findByDutiesId(req.getDutiesIdl());	
//		List<String> subDuties = req.getSubDuties();
//		
//		for (String dutyName : subDuties) {
//			String SubDutyName = dutyName.trim();
//			
//			if(subrepo.findBySubDutyName(SubDutyName) != null) {
//				throw new IllegalStateException("SubDuty:" + SubDutyName+"already exist");
//			}
//			
//			SubDuties obj = new SubDuties();		
//			obj.setSubDutyName(SubDutyName);
//			
//			list.add(obj);
//		}	
//		duties.setSubduties(list);	
//		Duties save = repo.save(duties);
//	}

	@Override
	public void saveSubduty(SubDutiesRequest req) {
		Duties duties = repo.findByDutiesId(req.getDutiesIdl());
		if (duties == null) {
			throw new IllegalArgumentException("Duties with ID " + req.getDutiesIdl() + " not found!");
		}
		List<String> subDuties = req.getSubDuties();
		List<SubDuties> subDutiesList = new ArrayList<>();
		for (String subDutyName : subDuties) {
			String SubDutyName = subDutyName.trim();
			if (subrepo.findBySubDutyName(SubDutyName) != null) {
				throw new IllegalStateException("SubDuty: " + SubDutyName + " already exists!");
			}
			SubDuties subDuty = new SubDuties();
			subDuty.setSubDutyName(SubDutyName);
			subDuty.getDuties().add(duties);
			subDutiesList.add(subDuty);
		}
		subrepo.saveAll(subDutiesList);
		duties.getSubduties().addAll(subDutiesList);
		repo.save(duties);
	}

	@Override
	public List<SubDuties> loadAllSubduties() {
		List<SubDuties> findAll = subrepo.findAll();
		for (SubDuties subduty : findAll) {
			subduty.setTask(null);
		}
		return findAll;
	}

}
