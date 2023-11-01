package com.erp.hrms.attendence.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.erp.hrms.attendence.entity.Attendence;

@Repository
public interface IAttendencerepo extends JpaRepository<Attendence, Long> {

	@Query("SELECT a FROM Attendence a WHERE a.employeeId = :employeeId")
	List<Attendence> findByEmployeeId(@Param("employeeId") Long employeeId);
	
    List<Attendence> findByEmployeeIdAndDateBetween(@Param("employeeId") Long employeeId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    boolean existsByEmployeeIdAndDate(Long employeeId, LocalDate date);
    
    List<Attendence> findByOvertimeStatus(String overtimestatus);

}
