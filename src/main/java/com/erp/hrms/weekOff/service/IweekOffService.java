package com.erp.hrms.weekOff.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.erp.hrms.weekOffEntity.WeekOff;

@Service
public interface IweekOffService {
	
	public WeekOff changeweekOff(WeekOff weekoff);
	
	public WeekOff getweekOff(long id);
	
	public List<WeekOff> getAllWeekOff(long id);

}
