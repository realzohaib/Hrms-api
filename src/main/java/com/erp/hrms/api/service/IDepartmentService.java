package com.erp.hrms.api.service;

import java.util.List;
import java.util.Optional;

import com.erp.hrms.entity.Department;

public interface IDepartmentService {
	
	public void saveDepartment(Department department);
	
	public Optional<Department> getDepartmentById(Long departmentId);
	
	public List<Department> getAllDepartment();
}

