package com.erp.hrms.api.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erp.hrms.entity.Designation;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, Long> {

	public Designation findBydId(Long dId);
}
