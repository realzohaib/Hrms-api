package com.erp.hrms.joblevelandDesignationSERVICE;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.joblevelandDesignationEntity.Designations;
import com.erp.hrms.joblevelandDesignationEntity.Duties;
import com.erp.hrms.joblevelandDesignationREPO.DesignationRepo;
import com.erp.hrms.joblevelandDesignationREPO.TaskRepo;
import com.erp.hrms.joblevelandDesignationREQ_RES.DutiesRequest;

@Service
public class DutiesIServicempl implements IDutiesService {

	@Autowired
	private DesignationRepo desigrepo;

	@Autowired
	private TaskRepo repo;

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

		desigrepo.save(designations);

	}

	@Override
	public List<Duties> getallduties() {
		return repo.findAll();
	}


}
