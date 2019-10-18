package com.bim.commons.exceptions;

public class BimException extends RuntimeException {

	private static final long serialVersionUID = 997079086952306927L;

	public BimException() {
		super();
	}

	public BimException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public BimException(String message, Throwable cause) {
		super(message, cause);
	}

	public BimException(String message) {
		super(message);
	}

	public BimException(Throwable cause) {
		super(cause);
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}

}
