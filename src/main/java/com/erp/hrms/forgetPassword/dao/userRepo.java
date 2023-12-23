package com.erp.hrms.forgetPassword.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.hrms.api.security.entity.UserEntity;

public interface userRepo extends JpaRepository<UserEntity, Long> {

	UserEntity findByEmail(String Email);
}
