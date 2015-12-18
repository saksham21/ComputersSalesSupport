package com.worksap.stm_s173.exception;

public class ServiceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2304180182724139360L;

	public ServiceException(Exception e) {
		super(e);
	}

	public ServiceException(String message, Exception e) {
		super(message, e);
	}

	public ServiceException(String string) {
		super(string);
	}
}
