package com.erp.hrms.exception;

public class MailServerConnectionException extends RuntimeException {

    public MailServerConnectionException(String message) {
        super(message);
    }

    public MailServerConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}