package com.erp.hrms.financialYear;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IFinancialYearRepo extends JpaRepository<FinancialYear, Integer>{
	

}
