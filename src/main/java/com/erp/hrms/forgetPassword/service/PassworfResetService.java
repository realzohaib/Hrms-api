package com.erp.hrms.forgetPassword.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.erp.hrms.api.security.response.MessageResponse;
import com.erp.hrms.exception.PersonalEmailExistsException;
import com.erp.hrms.forgetPassword.dao.IPasswordReset;
import com.erp.hrms.forgetPassword.entity.PasswordReset;

@Service
public class PassworfResetService {
	@Autowired
	private IPasswordReset dao;

	@Autowired
	private JavaMailSender javaMailSender;

	public void generateOTP(PasswordReset passwordreset) {
		String email = passwordreset.getEmail();
		if (dao.existsByEmail(email) == false) {
			throw new PersonalEmailExistsException(new MessageResponse("Email not registered"));
		}

		Integer otp = passwordreset.getOtp();
		dao.save(passwordreset);

		sendotp(email, otp);
	}

	public boolean verifyOTP(String email, int OTP) {
		PasswordReset passwordReset = dao.findByEmailAndOtp(email, OTP);
		return passwordReset != null && !passwordReset.isUsed()
				&& !passwordReset.getExpirationDateTime().isBefore(LocalDateTime.now());
	}

	public void sendotp(String email, int otp) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(email);
		mailMessage.setSubject("OTP for password reset");
		mailMessage.setText("OTP is  " + otp + ".OTP will expire in 10 minutes");
		javaMailSender.send(mailMessage);
	}
}
