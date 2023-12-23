package com.erp.hrms.helper.service;

import java.util.List;

import com.erp.hrms.helper.entity.monthCycle;

public interface IMonthCycleService {

	public void saveMonthCycle(monthCycle monthCycle);
	
	public List<monthCycle> getmonthlycycle();
}
