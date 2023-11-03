package com.erp.hrms.api.service;

import java.util.ArrayList;
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

	public void saveInitialDepartments() {
		if(idepartmentRepository.findAll().isEmpty()) {
		List<Department> initialDepartments = new ArrayList<>();
		initialDepartments.add(new Department( "Procurement"));
		initialDepartments.add(new Department( "Warehousing"));
		initialDepartments.add(new Department( "Logistics"));
		initialDepartments.add(new Department( "Quality Control"));
		initialDepartments.add(new Department( "Production & Operations"));
		initialDepartments.add(new Department( "Sales"));
		initialDepartments.add(new Department( "Human Resource"));
		initialDepartments.add(new Department( "Finance & Accounts"));
		initialDepartments.add(new Department( "General Administration"));
 
		idepartmentRepository.saveAll(initialDepartments);
	}
	}

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
		List<Department> findAllDepartments = idepartmentRepository.findAllDepartments();
		findAllDepartments.sort((d1, d2) -> Long.compare(d1.getDepartmentId(), d2.getDepartmentId()));
		return findAllDepartments;
	}

	@Override
	public Optional<Department> getDepartmentByName(String departmentName) {
		Department department = idepartmentRepository.findByNameWithPersonalInfos(departmentName);
		return Optional.ofNullable(department);
	}

}
