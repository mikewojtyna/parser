/**
 *
 */
package com.ef.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.ef.parser.LogEntry.LogEntryBuilder;
import nl.jqno.equalsverifier.EqualsVerifier;

/**
 * @author goobar
 *
 */
@SuppressWarnings("javadoc")
public class LogEntryTest
{

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void should_CreateInstanceWithoutAnyProperties() throws Exception
	{
		// when
		LogEntry entry = instance();

		// then
		assertThat(entry.getDate().isPresent()).isFalse();
		assertThat(entry.getIp().isPresent()).isFalse();
		assertThat(entry.getRequest().isPresent()).isFalse();
		assertThat(entry.getStatusCode().isPresent()).isFalse();
		assertThat(entry.getUserAgent().isPresent()).isFalse();
	}

	@Test
	public void should_Pass_EqualsTests() throws Exception
	{
		EqualsVerifier.forClass(LogEntry.class).usingGetClass()
			.verify();
	}

	@Test
	public void should_ToString_ReturnNonEmptyString() throws Exception
	{
		assertFalse(instance().toString().isEmpty());
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
	private LogEntry instance()
	{
		return LogEntryBuilder.instance().build();
	}

}
