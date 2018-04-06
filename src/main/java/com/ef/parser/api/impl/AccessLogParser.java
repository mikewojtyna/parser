/**
 *
 */
package com.ef.parser.api.impl;

import static com.google.common.base.Preconditions.checkNotNull;
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

	/**
	 * @param delimiter
	 *                a delimiter to separate tokens in log entry
	 * @param encoding
	 *                an encoding used in source stream
	 * @param dateFormat
	 *                a date format to be used to parse log entry date
	 */
	public AccessLogParser(String delimiter, Charset encoding,
		String dateFormat)
	{
		validate(delimiter, encoding, dateFormat);
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
		validate(source);
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

	/**
	 * @param source
	 */
	private void validate(InputStream source)
	{
		checkNotNull(source, "'source' cannot be null");
	}

	/**
	 * @param delimiter
	 * @param encoding
	 * @param dateFormat
	 */
	private void validate(String delimiter, Charset encoding,
		String dateFormat)
	{
		checkNotNull(delimiter, "'delimiter' cannot be null");
		checkNotNull(encoding, "'encoding' cannot be null");
		checkNotNull(dateFormat, "'dateFormat' cannot be null");
	}

}
