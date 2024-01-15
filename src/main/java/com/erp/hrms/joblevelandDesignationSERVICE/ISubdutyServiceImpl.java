package com.erp.hrms.joblevelandDesignationSERVICE;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.joblevelandDesignationEntity.Duties;
import com.erp.hrms.joblevelandDesignationEntity.SubDuties;
import com.erp.hrms.joblevelandDesignationREPO.DutiesRepo;
import com.erp.hrms.joblevelandDesignationREPO.SubDutiesRepo;
import com.erp.hrms.joblevelandDesignationREQ_RES.SubDutiesRequest;

@Service
public class ISubdutyServiceImpl implements ISubdutyService {
	
	@Autowired
	private DutiesRepo repo;
	
	@Autowired
	private SubDutiesRepo subrepo;

	@Override
	public void saveSubduty(SubDutiesRequest req) {
		
		ArrayList<SubDuties> list = new ArrayList();
		Duties duties = repo.findByDutiesId(req.getDutiesIdl());
		
		List<String> subDuties = req.getSubDuties();
		
		for (String dutyName : subDuties) {
			String SubDutyName = dutyName.trim();
			
			if(subrepo.findBySubDutyName(SubDutyName) != null) {
				throw new IllegalStateException("SubDuty:" + SubDutyName+"already exist");
			}
			
			SubDuties obj = new SubDuties();
			
			obj.setSubDutyName(SubDutyName);
			
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
