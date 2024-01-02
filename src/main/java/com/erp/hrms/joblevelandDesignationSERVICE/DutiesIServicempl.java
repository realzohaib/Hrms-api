package com.erp.hrms.joblevelandDesignationSERVICE;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.joblevelandDesignationEntity.Designations;
import com.erp.hrms.joblevelandDesignationEntity.Duties;
import com.erp.hrms.joblevelandDesignationREPO.DesignationRepo;
import com.erp.hrms.joblevelandDesignationREPO.DutiesRepo;
import com.erp.hrms.joblevelandDesignationREQ_RES.DutiesRequest;

@Service
public class DutiesIServicempl implements IDutiesService {
	
	@Autowired
	private DesignationRepo desigrepo;
	
	@Autowired
	private DutiesRepo repo;
	
	@Override
	public void saveDuties(DutiesRequest duties) {
		Designations designations = desigrepo.findByDesignationId(duties.getDesignationid());
        List<Duties> dutiesList = new ArrayList<>();
        
        for (String dutyName : duties.getDuties()) {
        	Duties duty = new Duties();
        	duty.setDutyName(dutyName);
        	
            dutiesList.add(duty);
        }
        designations.setDuties(dutiesList);
        
        Designations save = desigrepo.save(designations);

	}


//	@Override
//	public List<Duties> saveDuties(DutiesRequest duties) {
//		
//		List<Duties> list = new ArrayList<Duties>();
//		
//		List<Designations> deslist = new ArrayList<Designations>();
//		
//		Integer designationid = duties.getDesignationid();
//		Designations designation = desigrepo.findByDesignationId(designationid);
//		
//		deslist.add(designation);
//		
//		List<String> dutieslist = duties.getDuties();
//		
//		for (String dutyname : dutieslist) {
//			Duties duty = new Duties();
//			
//			duty.setDutyName(dutyname);
//			duty.setDesignation(deslist);
//			
//			list.add(duty);
//			
//		}
//		
//		return repo.saveAll(list);
//	}

	@Override
	public List<Duties> getallduties() {
		return repo.findAll();
	}

}
