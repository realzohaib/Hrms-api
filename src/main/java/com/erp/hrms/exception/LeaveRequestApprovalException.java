package com.erp.hrms.exception;

import com.erp.hrms.api.security.response.MessageResponse;

public class LeaveRequestApprovalException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	MessageResponse messageResponse;

	public LeaveRequestApprovalException(MessageResponse messageResponse) {
		super(messageResponse.getMessage());
		this.messageResponse = messageResponse;
	}
}