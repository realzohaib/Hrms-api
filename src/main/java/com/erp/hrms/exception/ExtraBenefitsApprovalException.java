package com.erp.hrms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.erp.hrms.api.security.response.MessageResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ExtraBenefitsApprovalException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	MessageResponse messageResponse;

	public ExtraBenefitsApprovalException(MessageResponse messageResponse) {
		super(messageResponse.getMessage());
		this.messageResponse = messageResponse;
	}

}
