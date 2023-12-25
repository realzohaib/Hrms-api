package com.erp.hrms.api.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erp.hrms.entity.Level;

@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {

	public Level findBylId(Long id);
}
