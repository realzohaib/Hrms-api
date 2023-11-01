package com.erp.hrms.api.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erp.hrms.entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

}
