package com.erp.hrms.Location.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.hrms.Location.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

	Location findByLocationId(Long locationId);

}
