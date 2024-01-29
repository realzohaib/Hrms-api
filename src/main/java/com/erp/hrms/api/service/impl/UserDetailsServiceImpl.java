/**
 * 
 */
package com.erp.hrms.api.service.impl;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.erp.hrms.api.repo.UserRepository;
import com.erp.hrms.api.security.entity.UserEntity;




/**
 * @author TA Admin
 *
 * 
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	  @Autowired
	  UserRepository userRepository;

	  @Override
	  @Transactional
//	  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//	    UserEntity user = userRepository.findByUsername(username)
//	        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
//
//	    return UserDetailsImpl.build(user);
//	  }
	  public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
	        UserEntity user = userRepository.findByUsernameOrEmailOrMobileNo(identifier, identifier, identifier)
	                .orElseThrow(() -> new UsernameNotFoundException("User not found with identifier: " + identifier));
	        return UserDetailsImpl.build(user);
	    }

}
