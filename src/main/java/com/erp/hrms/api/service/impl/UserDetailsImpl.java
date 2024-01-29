/**
 * 
 */
package com.erp.hrms.api.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.erp.hrms.api.security.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author TA Admin
 *
 * 
 */

public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String username;

	private String email;
	
	private String mobileNo;

	@JsonIgnore
	private String password;
	
	private boolean isEnabled;

	private Collection<? extends GrantedAuthority> authorities;

	public UserDetailsImpl(Long id, String username, String email,String mobileNo, String password,
			Collection<? extends GrantedAuthority> authorities , boolean isEnabled) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.mobileNo= mobileNo;
		this.password = password;
		this.authorities = authorities;
		this.isEnabled = isEnabled;
	}

	public static UserDetailsImpl build(UserEntity user) {
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());

		return new UserDetailsImpl(user.getId(), user.getUsername(), user.getEmail(),user.getMobileNo(), user.getPassword(), authorities , user.isEnabled() );
	}
	
//	public static UserDetailsImpl build(UserEntity user) {
//		List<GrantedAuthority> authorities = user.getJoblevel().stream()
//				.map(joblevel -> new SimpleGrantedAuthority(joblevel.getJoblevel())).collect(Collectors.toList());
//
//		return new UserDetailsImpl(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), authorities , user.isEnabled() );
//	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return isEnabled;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		UserDetailsImpl user = (UserDetailsImpl) o;
		return Objects.equals(id, user.id);
	}

}
