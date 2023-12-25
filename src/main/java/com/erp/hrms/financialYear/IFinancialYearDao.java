package com.erp.hrms.financialYear;

import java.util.List;

public interface IFinancialYearDao {
	
	public void saveYear(FinancialYear year);
	
	public List<FinancialYear> getFinancialYear();
	
	public void deleteYear(int id);

}
