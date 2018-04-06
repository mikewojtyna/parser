/**
 *
 */
package com.ef.parser.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.ef.parser.api.LogEntry.LogEntryBuilder;
import com.ef.parser.api.impl.AccessLogParser;

/**
 * @author goobar
 *
 */
@SuppressWarnings("javadoc")
public class AccessLogParserTest
{

	private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

	private static final Charset DEFAULT_ENCODING = Charset
		.forName("UTF-8");

	private static final String PIPE_DELIMITER_LINE = "2017-01-01 00:00:11.763|192.168.234.82|\"GET / HTTP/1.1\"|200|\"swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void should_ParseAllLines() throws Exception
	{
		// given
		LogParser parser = parserWithPipeDelimiter();
		// source with 3 exactly the same lines
		InputStream source = multiLineSource(PIPE_DELIMITER_LINE,
			PIPE_DELIMITER_LINE, PIPE_DELIMITER_LINE);
		// each entry is the same
		LogEntry expectedEntry = LogEntryBuilder.instance()
			.withDate(LocalDateTime.of(2017, 1, 1, 0, 0, 11,
				763_000_000))
			.withIp("192.168.234.82")
			.withRequest("\"GET / HTTP/1.1\"").withStatusCode("200")
			.withUserAgent(
				"\"swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"")
			.build();

		// when
		List<LogEntry> entries = parser.parse(source);

		// then
		assertThat(entries).containsExactly(expectedEntry,
			expectedEntry, expectedEntry);
	}

	@Test
	public void should_ParseAllProperties_When_UsingHashDelimiter()
		throws Exception
	{
		// given
		LogParser logParser = parserWithDelimiter("#");
		InputStream source = singleLineSource(
			"2017-01-01 00:00:11.763#192.168.234.82#\"GET / HTTP/1.1\"#200#\"fake user agent\"");
		LogEntry expectedEntry = LogEntryBuilder.instance()
			.withDate(LocalDateTime.of(2017, 1, 1, 0, 0, 11,
				763_000_000))
			.withIp("192.168.234.82")
			.withRequest("\"GET / HTTP/1.1\"").withStatusCode("200")
			.withUserAgent("\"fake user agent\"").build();

		// when
		List<LogEntry> entries = logParser.parse(source);

		// then
		assertThat(entries.get(0)).isEqualTo(expectedEntry);
	}

	@Test
	public void should_ParseDateProperty() throws Exception
	{
		// given
		LogParser logParser = parserWithPipeDelimiter();
		InputStream source = singleLineSource(PIPE_DELIMITER_LINE);
		LocalDateTime expectedDate = LocalDateTime.of(2017, 1, 1, 0, 0,
			11, 763_000_000);

		// when
		List<LogEntry> entries = logParser.parse(source);

		// then
		assertThat(entries.get(0).getDate()).isEqualTo(expectedDate);
	}

	@Test
	public void should_ParseDateProperty_When_UsingCustomDateFormat()
		throws Exception
	{
		// given
		// date is given in format "dd-MM-yyyy HH:mm:ss.SSS"
		String dateFormat = "dd-MM-yyyy HH:mm:ss.SSS";
		LogParser logParser = parserWithPipeDelimiterAndDateFormat(
			dateFormat);
		InputStream source = singleLineSource(
			"13-10-2017 00:00:10.123|192.168.234.82|\"GET / HTTP/1.1\"|200|\"swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"");
		LocalDateTime expectedDate = LocalDateTime.of(2017, 10, 13, 0,
			0, 10, 123_000_000);

		// when
		List<LogEntry> entries = logParser.parse(source);

		// then
		assertThat(entries.get(0).getDate()).isEqualTo(expectedDate);
	}

	@Test
	public void should_ParseIpProperty() throws Exception
	{
		// given
		LogParser logParser = parserWithPipeDelimiter();
		InputStream source = singleLineSource(PIPE_DELIMITER_LINE);
		String expectedIp = "192.168.234.82";

		// when
		List<LogEntry> entries = logParser.parse(source);

		// then
		assertThat(entries.get(0).getIp()).isEqualTo(expectedIp);
	}

	@Test
	public void should_ParseRequestProperty() throws Exception
	{
		// given
		LogParser logParser = parserWithPipeDelimiter();
		InputStream source = singleLineSource(PIPE_DELIMITER_LINE);
		String expectedRequest = "\"GET / HTTP/1.1\"";

		// when
		List<LogEntry> entries = logParser.parse(source);

		// then
		assertThat(entries.get(0).getRequest())
			.isEqualTo(expectedRequest);
	}

	@Test
	public void should_ParseSingleEntry() throws Exception
	{
		// given
		InputStream source = singleLineSource();
		LogParser logParser = parser();

		// when
		List<LogEntry> entries = logParser.parse(source);

		// then
		assertThat(entries).hasSize(1);
	}

	@Test
	public void should_ParseStatusProperty() throws Exception
	{
		// given
		LogParser logParser = parserWithPipeDelimiter();
		InputStream source = singleLineSource(PIPE_DELIMITER_LINE);
		String expectedStatusCode = "200";

		// when
		List<LogEntry> entries = logParser.parse(source);

		// then
		assertThat(entries.get(0).getStatusCode())
			.isEqualTo(expectedStatusCode);
	}

	@Test
	public void should_ThrowException_When_LogEntryHasBadDateFormat()
		throws Exception
	{
		// given
		LogParser logParser = parserWithPipeDelimiter();
		// log entry has bad date format - only date (without time) is
		// given
		InputStream source = singleLineSource(
			"2017-01-01|192.168.234.82|\"GET / HTTP/1.1\"|200|\"swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"");

		// when
		assertThatThrownBy(() -> logParser.parse(source))

			// then
			.isInstanceOf(LogParserException.class);
	}

	@Test
	public void should_ThrowException_When_LogEntryHasNoRightDelimiter()
		throws Exception
	{
		// given
		LogParser logParser = parserWithPipeDelimiter();
		// source is missing the "|" delimiter - instead, # is used
		InputStream source = singleLineSource(
			"2017-01-01 00:00:11.763#192.168.234.82#\"GET / HTTP/1.1\"#200#\"fake user agent\"");

		// when
		assertThatThrownBy(() -> logParser.parse(source))

			// then
			.isInstanceOf(LogParserException.class);
	}

	@Test
	public void should_ThrowException_When_LogEntryHasNotEnoughFields()
		throws Exception
	{
		// given
		LogParser logParser = parserWithPipeDelimiter();
		// log entry has not enough "|" delimiters - we expect 5, while
		// there are only 2
		InputStream source = singleLineSource(
			"2017-01-01 00:00:11.763|192.168.234.82");

		// when
		assertThatThrownBy(() -> logParser.parse(source))

			// then
			.isInstanceOf(LogParserException.class);
	}

	@Test
	public void should_ThrowException_When_LogSourceIsInvalid()
		throws Exception
	{
		// given
		LogParser parser = parser();
		InputStream source = invalidInputStream();

		// when
		assertThatThrownBy(() -> parser.parse(source))

			// then
			.isInstanceOf(LogParserException.class);
	}

	@Test
	public void should_UserAgentProperty() throws Exception
	{
		// given
		LogParser logParser = parserWithPipeDelimiter();
		InputStream source = singleLineSource(PIPE_DELIMITER_LINE);
		String expectedUserAgent = "\"swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"";

		// when
		List<LogEntry> entries = logParser.parse(source);

		// then
		assertThat(entries.get(0).getUserAgent())
			.isEqualTo(expectedUserAgent);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}

	/**
	 * @return
	 */
	private InputStream invalidInputStream()
	{
		return new InputStream()
		{

			@Override
			public int read() throws IOException
			{
				throw new IOException();
			}
		};
	}

	/**
	 * @param string
	 * @return
	 */
	private InputStream multiLineSource(String... string)
	{
		return new ByteArrayInputStream(Stream.of(string)
			.collect(Collectors.joining(System.lineSeparator()))
			.getBytes(DEFAULT_ENCODING));
	}

	/**
	 * @return
	 */
	private LogParser parser()
	{
		return parserWithPipeDelimiter();
	}

	/**
	 * @param delimiter
	 * @return
	 */
	private LogParser parserWithDelimiter(String delimiter)
	{
		return new AccessLogParser(delimiter, DEFAULT_ENCODING,
			DEFAULT_DATE_FORMAT);
	}

	/**
	 * @return
	 */
	private LogParser parserWithPipeDelimiter()
	{
		return parserWithDelimiter("\\|");
	}

	/**
	 * @param dateFormat
	 * @return
	 */
	private LogParser parserWithPipeDelimiterAndDateFormat(
		String dateFormat)
	{
		return new AccessLogParser("\\|", DEFAULT_ENCODING, dateFormat);
	}

	/**
	 * @return
	 */
	private InputStream singleLineSource()
	{
		return singleLineSource(PIPE_DELIMITER_LINE);
	}

	/**
	 * @param line
	 * @return
	 */
	private InputStream singleLineSource(String line)
	{
		return new ByteArrayInputStream(
			line.getBytes(DEFAULT_ENCODING));
	}

}
