package com.erp.hrms.api.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.erp.hrms.api.security.entity.UserEntity;



/**
 * @author TA Admin
 *
 * 
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

	Optional<UserEntity> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
	
    UserEntity findByActivationToken(String activationToken);
    
    UserEntity getByUsername(String name);


}

