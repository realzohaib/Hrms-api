package com.erp.hrms.joblevelandDesignationSERVICE;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.joblevelandDesignationEntity.Duties;
import com.erp.hrms.joblevelandDesignationEntity.SubDuties;
import com.erp.hrms.joblevelandDesignationREPO.TaskRepo;
import com.erp.hrms.joblevelandDesignationREPO.SubDutiesRepo;
import com.erp.hrms.joblevelandDesignationREQ_RES.SubDutiesRequest;

@Service
public class ISubdutyServiceImpl implements ISubdutyService {
	
	@Autowired
	private TaskRepo repo;
	
	@Autowired
	private SubDutiesRepo subrepo;

	@Override
	public void saveSubduty(SubDutiesRequest req) {
		
		ArrayList<SubDuties> list = new ArrayList();
		Duties duties = repo.findByDutiesId(req.getDutiesIdl());
		
		List<String> subDuties = req.getSubDuties();
		
		for (String dutyName : subDuties) {
			SubDuties obj = new SubDuties();
			
			obj.setSubDutyName(dutyName);
			
			list.add(obj);
		}
		
		duties.setSubduties(list);
		
		Duties save = repo.save(duties);
	}

	@Override
	public List<SubDuties> loadAllSubduties() {
		return subrepo.findAll();
	}

}
