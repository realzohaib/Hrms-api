package com.erp.hrms.weekOff.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.erp.hrms.weekOffEntity.WeekOff;

@Repository
public interface IWeekoffRepo extends JpaRepository<WeekOff, Long> {

	@Query("SELECT wo FROM WeekOff wo WHERE wo.weekOffId = (SELECT MAX(wo2.weekOffId) FROM WeekOff wo2 WHERE wo2.employeeId = :employeeId)")
	WeekOff findMostRecentWeekOffByEmployeeId(@Param("employeeId") Long employeeId);

	List<WeekOff> findByEmployeeId(Long empid);

}
