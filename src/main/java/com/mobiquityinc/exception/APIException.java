package com.mobiquityinc.exception;

/**
 * Exception class used when an API error is occurred.
 * 
 * @author dfjmax
 *
 */
public class APIException extends RuntimeException {

	/**
	 * Constructor with message.
	 * 
	 * @param message the exception message
	 */
	public APIException(String message) {
		super(message);
	}

	/**
	 * Constructor with message and cause.
	 * 
	 * @param message the exception message
	 * @param cause   the cause
	 */
	public APIException(String message, Throwable cause) {
		super(message, cause);
	}

}
