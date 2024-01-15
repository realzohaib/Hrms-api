package com.erp.hrms.joblevelandDesignationSERVICE;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.joblevelandDesignationEntity.Designations;
import com.erp.hrms.joblevelandDesignationEntity.Duties;
import com.erp.hrms.joblevelandDesignationEntity.SubDuties;
import com.erp.hrms.joblevelandDesignationREPO.DesignationRepo;
import com.erp.hrms.joblevelandDesignationREPO.DutiesRepo;
import com.erp.hrms.joblevelandDesignationREQ_RES.DutiesRequest;
import com.erp.hrms.joblevelandDesignationREQ_RES.DutiesResponse;

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
			String DutyName = dutyName.trim();
			if(repo.findByDutyName(DutyName) != null) {
				throw new IllegalStateException("DutyName: " + dutyName + " already exist!!");
			}
			Duties duty = new Duties();
			duty.setDutyName(DutyName);

			dutiesList.add(duty);
		}
		designations.setDuties(dutiesList);

		desigrepo.save(designations);

	}

	@Override
	public List<Duties> getallduties() {
		return repo.findAll();
	}

	@Override
	public List<DutiesResponse> loadDutiesByDesignationId(int designationId) {
		List<DutiesResponse> response = new ArrayList<DutiesResponse>();

		List<Duties> listOfDuties = repo.findByDesignationId(designationId);
		for (Duties duties : listOfDuties) {
			DutiesResponse obj = new DutiesResponse();

			obj.setDesignationId(designationId);
			obj.setDutiesId(duties.getDutiesId());
			obj.setDutyName(duties.getDutyName());

			List<SubDuties> subdutylist = new ArrayList<SubDuties>();
			List<SubDuties> subduties = duties.getSubduties();
			
			for (SubDuties subduty : subduties) {
				SubDuties sub = new SubDuties();
				sub.setSubDutiesId(subduty.getSubDutiesId());
				sub.setSubDutyName(subduty.getSubDutyName());

				subdutylist.add(sub);

			}
			response.add(obj);

		}
		return response;
	}

}
