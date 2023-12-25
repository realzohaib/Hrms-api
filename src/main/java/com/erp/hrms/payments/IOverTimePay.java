package com.erp.hrms.payments;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOverTimePay extends JpaRepository<OverTimePay, Long> {
	
	OverTimePay findTopByOrderByDateDesc();
	
    OverTimePay findById(int id);


}
