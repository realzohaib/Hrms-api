package com.erp.hrms.payments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IleaveCutAmount extends JpaRepository<leaveCutAmount, Long>{
	
	leaveCutAmount findById(long id);

}
