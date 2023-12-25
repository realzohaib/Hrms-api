package com.erp.hrms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.erp.hrms.api.security.response.MessageResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class PersonalInfoNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	MessageResponse messageResponse;

	public PersonalInfoNotFoundException(MessageResponse messageResponse) {
		super(messageResponse.getMessage());
		this.messageResponse = messageResponse;
	}

}
