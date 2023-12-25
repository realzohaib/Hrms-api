package com.erp.hrms.api.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erp.hrms.entity.CurrentDesignation;

@Repository
public interface CurrentDesignationRepository extends JpaRepository<CurrentDesignation, Long> {

	public CurrentDesignation findBycdId(Long cdid);

}
