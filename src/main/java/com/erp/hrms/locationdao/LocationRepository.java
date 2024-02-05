package com.erp.hrms.locationdao;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.erp.hrms.locationentity.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

	Location findByLocationId(long id);

}
