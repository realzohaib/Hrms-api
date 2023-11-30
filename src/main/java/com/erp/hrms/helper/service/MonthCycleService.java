package com.erp.hrms.helper.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.helper.entity.monthCycle;
import com.erp.hrms.helper.repository.MonthCycleRepository;

@Service
public class MonthCycleService implements IMonthCycleService {

	@Autowired
	private MonthCycleRepository cycleRepository;

	@Override
	public void saveMonthCycle(monthCycle monthCycle) {
		cycleRepository.save(monthCycle);
	}

}
