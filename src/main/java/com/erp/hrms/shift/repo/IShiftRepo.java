package com.erp.hrms.shift.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erp.hrms.shift.entity.Shift;

@Repository
public interface IShiftRepo extends JpaRepository<Shift, Integer> {
	
}
