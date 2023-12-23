package com.erp.hrms.payments;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class leaveCutAmountService {
	
	@Autowired
	private IleaveCutAmount repo;
	
	public leaveCutAmount savedeductionAmount(leaveCutAmount cut) {
		leaveCutAmount leaveCutAmount = new leaveCutAmount();
		LocalDate localDate = LocalDate.now();
		
		leaveCutAmount.setDate(localDate);
		leaveCutAmount.setSalaryCutAmountPercentage(cut.getSalaryCutAmountPercentage());
		return repo.save(leaveCutAmount);
	}
	
	public List<leaveCutAmount> getall() {
		return repo.findAll();
	}
	
	public leaveCutAmount update(leaveCutAmount l2) {
		leaveCutAmount leavecutAmount = repo.findById(l2.getId());
		leavecutAmount.setSalaryCutAmountPercentage(l2.getSalaryCutAmountPercentage());
		leavecutAmount.setDate(LocalDate.now());
		return repo.save(leavecutAmount);
	}

}
