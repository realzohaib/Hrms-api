package com.erp.hrms.financialYear;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IFinancialYearDaoImpl implements IFinancialYearDao {

	@Autowired
	private IFinancialYearRepo repo;

	@Override
	public void saveYear(FinancialYear year) {
		repo.save(year);
	}

	@Override
	public List<FinancialYear> getFinancialYear() {
		List<FinancialYear> list = repo.findAll();
		return list;
	}

	@Override
	public void deleteYear(int id) {
		repo.deleteById(id);
	}

}
