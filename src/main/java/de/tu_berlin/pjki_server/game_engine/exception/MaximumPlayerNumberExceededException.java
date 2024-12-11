/**
 * 
 */
package de.tu_berlin.pjki_server.game_engine.exception;

/**
 * @author 
 *
 */
public class MaximumPlayerNumberExceededException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public MaximumPlayerNumberExceededException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public MaximumPlayerNumberExceededException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public MaximumPlayerNumberExceededException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MaximumPlayerNumberExceededException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public MaximumPlayerNumberExceededException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
