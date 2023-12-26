package com.erp.hrms.api.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.erp.hrms.entity.Designation;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Long> {

	public Designation findBydId(Long dId);

	@Query("SELECT d FROM Designation d WHERE d.employeeId = :employeeId ORDER BY d.dId DESC")
	List<Designation> findDesignationWithMaxdIdByEmployeeId(@Param("employeeId") Long employeeId);

	List<Designation> findByEmployeeId(@Param("employeeId") Long employeeId);

}