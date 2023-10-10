package com.erp.hrms.api.security.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Boolean status;

	public StatusResponse(Boolean status) {
		this.status = status;
	}
}
