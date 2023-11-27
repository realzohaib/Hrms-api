package com.erp.hrms.payments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OvertimePayService {
	
	@Autowired
	private IOverTimePay repo;
	
	public OverTimePay saveOverTimeAmount(OverTimePay overtime) {
		return repo.save(overtime);
	}
	
	public OverTimePay getovertimeAmount() {
		return repo.findTopByOrderByDateDesc();
	}
	
	public OverTimePay updateovertimepay(int id , OverTimePay pay) {
		
		OverTimePay pay2 = repo.findById(id);
		
		pay2.setOvertimeAmount(pay.getOvertimeAmount());
		pay2.setDate(pay.getDate());
		
		OverTimePay save = repo.save(pay2);
		return save;
	}


}
