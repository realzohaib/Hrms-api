package com.erp.hrms.api.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.erp.hrms.entity.Department;
import com.erp.hrms.entity.helper.PersonalInfoDTO;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

	public Department findByDepartmentName(String departmentName);

	@Query("SELECT new com.erp.hrms.entity.Department(d.departmentId, d.departmentName) FROM Department d")
	public List<Department> findAllDepartments();

//	@Query("SELECT new com.erp.hrms.entity.helper.PersonalInfoDTO(d.departmentId, d.departmentName, "
//			+ "pi.employeeId, pi.namePrefix, pi.firstName, pi.middleName, pi.lastName, pi.dateOfBirth, "
//			+ "pi.phoneCode, pi.personalContactNo, pi.email, di.designationName, " + "di.level, jd.postedLocation) "
//			+ "FROM Department d " + "JOIN d.personalInfos pi "
//			+ "LEFT JOIN pi.jobDetails jd LEFT JOIN pi.designations di " + "WHERE d.departmentName = :departmentName")
//	List<PersonalInfoDTO> findFirstAndLastNameByDepartmentName(@Param("departmentName") String departmentName);

}
