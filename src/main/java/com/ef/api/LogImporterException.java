/**
 *
 */
package com.ef.api;

/**
 * @author goobar
 *
 */
@SuppressWarnings("javadoc")
public class LogImporterException extends RuntimeException
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public LogImporterException(String message)
	{
		super(message);
	}

	public LogImporterException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
