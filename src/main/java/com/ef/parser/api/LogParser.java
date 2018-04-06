/**
 *
 */
package com.ef.parser.api;

import java.io.InputStream;
import java.util.stream.Stream;

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
	 * @return the stream containing all extracted entries
	 * @throws NullPointerException
	 *                 if any argument is null
	 * @throws LogParserException
	 *                 if parser fails
	 */
	Stream<LogEntry> parse(InputStream source)
		throws NullPointerException, LogParserException;
}
