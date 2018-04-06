/**
 *
 */
package com.ef.parser.api;

import java.io.InputStream;
import java.util.List;

/**
 * A parser to extract log entries from logs source.
 *
 * @author goobar
 *
 */
public interface LogParser
{
	/**
	 * Parses the given source (most probably a file stream) and extracts
	 * all available
	 *
	 * @param source
	 *                an input stream containing log entries
	 * @return the list containing all extracted entries (in the same order)
	 * @throws NullPointerException
	 *                 if any argument is null
	 * @throws LogParserException
	 *                 if parser fails
	 */
	List<LogEntry> parse(InputStream source)
		throws NullPointerException, LogParserException;
}
