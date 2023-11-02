package com.erp.hrms.api.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.erp.hrms.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

	public Department findByDepartmentName(String departmentName);

	@Query("SELECT d FROM Department d LEFT JOIN FETCH d.personalInfos WHERE d.departmentName = :departmentName")
	public Department findByNameWithPersonalInfos(@Param("departmentName") String departmentName);
	

	@Query("SELECT new com.erp.hrms.entity.Department(d.departmentId, d.departmentName) FROM Department d")
	public List<Department> findAllDepartments();
	
}
