package me.max.exceptions;

//To be thrown if username/password do not match
public class UserPasswordException extends Exception {

	public UserPasswordException() {
		super();
	}

	public UserPasswordException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UserPasswordException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserPasswordException(String message) {
		super(message);
	}

	public UserPasswordException(Throwable cause) {
		super(cause);
	}

	private static final long serialVersionUID = -4234144356710199711L;

}
