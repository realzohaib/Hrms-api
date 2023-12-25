package com.erp.hrms.weekOff.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.api.dao.IPersonalInfoDAO;
import com.erp.hrms.weekOff.repo.IWeekoffRepo;
import com.erp.hrms.weekOffEntity.WeekOff;

@Service
public class weekOffserviceImpl implements IweekOffService{
	
	@Autowired
	private IWeekoffRepo repo;
	
	@Autowired
	private IPersonalInfoDAO dao;

	@Override
	public WeekOff changeweekOff(WeekOff off) {
		return repo.save(off);
	}

	@Override
	public WeekOff getweekOff(long empid) {
		WeekOff weekoff = repo.findMostRecentWeekOffByEmployeeId(empid);
		if(weekoff == null) {
			WeekOff off = new WeekOff();
			off.setIndividualDayOff("FRIDAY");//ye abhi static hai , isse dynamic karna padega jab furqaan data bhejega
			return off;
		}
		
		return weekoff;
	}

	@Override
	public List<WeekOff> getAllWeekOff(long id) {
		return repo.findByEmployeeId(id);
		
	}

}
