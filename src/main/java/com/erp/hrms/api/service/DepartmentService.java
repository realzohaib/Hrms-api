package com.erp.hrms.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.api.dao.DepartmentRepository;
import com.erp.hrms.entity.Department;

@Service
public class DepartmentService implements IDepartmentService {

	
	@Autowired
	private DepartmentRepository idepartmentRepository;
	
	@Override
	public void saveDepartment(Department department) {
		idepartmentRepository.save(department);
	}

	@Override
	public Optional<Department> getDepartmentById(Long departmentId) {
	
		return idepartmentRepository.findById(departmentId);
	}

	@Override
	public List<Department> getAllDepartment() {
		
		return idepartmentRepository.findAll();
	}

}
