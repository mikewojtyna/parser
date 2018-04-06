/**
 *
 */
package com.ef.parser.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.stream.Stream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.ef.parser.api.LogEntry;

/**
 * @author goobar
 *
 */
public class ParserTest
{

	private LogParser logParser;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void should_ParseSingleEntry() throws Exception
	{
		// given
		InputStream source = new ByteArrayInputStream(
			"hello".getBytes(utf8Charset()));

		// when
		Stream<LogEntry> entries = logParser.parse(source);

		// then
		assertThat(entries).hasSize(1);
		fail("Not yet implemented");
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
	private Charset utf8Charset()
	{
		return Charset.forName("UTF-8");
	}

}
