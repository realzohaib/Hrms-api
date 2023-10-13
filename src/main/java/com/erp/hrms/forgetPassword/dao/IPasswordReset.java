package com.erp.hrms.forgetPassword.dao;

import com.erp.hrms.forgetPassword.entity.PasswordReset;

public interface IPasswordReset {
	public PasswordReset getNewpassword(String email);
	public boolean existsByEmail(String email);
	public void save(PasswordReset passwordReset);
	public PasswordReset findByEmailAndOtp(String email, int otp);
}

