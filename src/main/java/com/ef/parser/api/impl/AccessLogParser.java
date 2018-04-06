/**
 *
 */
package com.ef.parser.api.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;
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

	private static final String COMMON_EX_MSG = "Failed to parse log source.";

	private final DateTimeFormatter dateFormatter;

	private final String delimiter;

	private final Charset encoding;

	public AccessLogParser(String delimiter, Charset encoding,
		String dateFormat)
	{
		this.delimiter = delimiter;
		this.encoding = encoding;
		dateFormatter = DateTimeFormatter.ofPattern(dateFormat);
	}

	/* (non-Javadoc)
	 * @see com.ef.parser.api.LogParser#parse(java.io.InputStream)
	 */
	@Override
	public List<LogEntry> parse(InputStream source)
		throws NullPointerException, LogParserException
	{
		try
		{
			return IOUtils.readLines(source, encoding).stream()
				.map(this::createEntry)
				.collect(Collectors.toList());
		}
		catch (IOException e)
		{
			throw prepareEx(e);
		}
	}

	private LogEntry createEntry(String line)
	{
		String[] tokens = line.split(delimiter);
		if (tokens.length < 5)
		{
			throw prepareEx(MessageFormat.format(
				"Bad number of {0} delimiters.", delimiter));
		}
		return LogEntryBuilder.instance().withDate(parseDate(tokens[0]))
			.withIp(tokens[1]).withRequest(tokens[2])
			.withStatusCode(tokens[3]).withUserAgent(tokens[4])
			.build();
	}

	/**
	 * @param token
	 * @return
	 */
	private LocalDateTime parseDate(String token)
	{
		try
		{
			return LocalDateTime.parse(token, dateFormatter);
		}
		catch (DateTimeParseException e)
		{
			throw prepareEx(MessageFormat
				.format("Invalid date format: {0}.", token));
		}
	}

	/**
	 * @param commonExMsg
	 * @param reason
	 * @return
	 */
	private LogParserException prepareEx(Exception reason)
	{
		return new LogParserException(COMMON_EX_MSG, reason);
	}

	/**
	 * @param detailedMsg
	 * @return
	 */
	private LogParserException prepareEx(String detailedMsg)
	{
		return new LogParserException(
			COMMON_EX_MSG + " " + detailedMsg);
	}

}
