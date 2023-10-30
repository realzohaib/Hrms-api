package com.erp.hrms.form.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.hrms.api.repo.UserRepository;
import com.erp.hrms.api.security.entity.UserEntity;

@Service
public class UserServiceForLeaveNotification {

	@Autowired
	private UserRepository userRepository;

	public List<UserEntity> getUsers() {
		List<UserEntity> userList = userRepository.findAll();
		return userList;
	}
}
