package com.erp.hrms.api.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erp.hrms.api.security.entity.RoleEntity;
import com.erp.hrms.api.utill.ERole;

@Repository
public interface IRoleRepository extends JpaRepository<RoleEntity, Long> {
	Optional<RoleEntity> findByName(ERole name);
}
