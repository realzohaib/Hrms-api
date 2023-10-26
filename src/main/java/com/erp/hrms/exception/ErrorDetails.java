package com.erp.hrms.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor

public class ErrorDetails {

	private LocalDateTime dateTime;
	private String message;
	private String details;
}
