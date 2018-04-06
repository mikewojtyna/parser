/**
 *
 */
package com.ef.parser.api.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;
import org.apache.commons.io.IOUtils;
import com.ef.parser.api.LogEntry;
import com.ef.parser.api.LogEntry.LogEntryBuilder;
import com.ef.parser.api.LogParser;
import com.ef.parser.api.LogParserException;

/**
 * This implementation is able to parse Apache "access.log"-like text logs.
 *
 * @author goobar
 *
 */
public class AccessLogParser implements LogParser
{

	private final String delimiter;

	private final Charset encoding;

	public AccessLogParser(String delimiter, Charset encoding)
	{
		this.delimiter = delimiter;
		this.encoding = encoding;
	}

	/* (non-Javadoc)
	 * @see com.ef.parser.api.LogParser#parse(java.io.InputStream)
	 */
	@Override
	public Stream<LogEntry> parse(InputStream source)
		throws NullPointerException, LogParserException
	{
		try
		{
			return IOUtils.readLines(source, encoding).stream()
				.map(this::createEntry);
		}
		catch (IOException e)
		{
			throw new LogParserException(
				"Failed to parse log source.", e);
		}
	}

	private LogEntry createEntry(String line)
	{
		String[] tokens = line.split(delimiter);
		return LogEntryBuilder.instance()
			.withDate(LocalDateTime.parse(tokens[0],
				DateTimeFormatter
					.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")))
			.withIp(tokens[1]).withRequest(tokens[2])
			.withStatusCode(tokens[3]).withUserAgent(tokens[4])
			.build();
	}

}
