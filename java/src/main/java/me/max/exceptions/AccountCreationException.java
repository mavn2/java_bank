package me.max.exceptions;

public class AccountCreationException extends Exception{

	private static final long serialVersionUID = 3397151042723206707L;

	public AccountCreationException() {
		super();
	}

	public AccountCreationException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AccountCreationException(String message, Throwable cause) {
		super(message, cause);
	}

	public AccountCreationException(String message) {
		super(message);
	}

	public AccountCreationException(Throwable cause) {
		super(cause);
	}
	
	

}
