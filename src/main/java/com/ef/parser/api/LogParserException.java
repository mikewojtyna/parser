/**
 *
 */
package com.ef.parser.api;

/**
 * @author goobar
 *
 */
@SuppressWarnings("javadoc")
public class LogParserException extends RuntimeException
{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public LogParserException(String message)
	{
		super(message);
	}

	public LogParserException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
