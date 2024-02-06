package com.erp.hrms.api.service;

import java.util.List;
import java.util.Optional;

import com.erp.hrms.entity.Department;
import com.erp.hrms.entity.helper.PersonalInfoDTO;

public interface IDepartmentService {

	public void saveInitialDepartments();

	public void saveDepartment(Department department);

	public Optional<Department> getDepartmentById(Long departmentId);

	public List<Department> getAllDepartment();
	
	public Department updateDepartment(Long departmentId, Department updatedDepartment);

	public List<PersonalInfoDTO> getPersonalInfoByDepartmentName(String departmentName);
}
