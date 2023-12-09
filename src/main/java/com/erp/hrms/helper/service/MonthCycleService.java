package com.erp.hrms.helper.service;

<<<<<<< HEAD
import java.util.List;

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

	@Override
	public List<monthCycle> getmonthcycle() {
		return cycleRepository.findAll();

	}

}
=======
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
>>>>>>> branch 'test' of https://github.com/realzohaib/Hrms-api.git
