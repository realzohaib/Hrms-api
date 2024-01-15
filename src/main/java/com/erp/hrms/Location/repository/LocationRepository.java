package com.erp.hrms.Location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erp.hrms.Location.entity.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
	
	Location findByLocationId(long id);

}