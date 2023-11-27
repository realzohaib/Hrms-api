package com.erp.hrms.payments;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaxService {
	@Autowired
	private ITaxRepo repo;
	
	
	public Tax savetax(Tax tax) {
		return repo.save(tax);
	}
	
	public List<Tax> getTax() {
		return repo.findAll();
	}
	
	public void deleteTax(long id) {
		repo.deleteById(id);
	}

}
