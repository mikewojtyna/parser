/**
 *
 */
package com.ef.cli;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author goobar
 *
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@SuppressWarnings("javadoc")
public class CliRunnerIntegrationTest
{
	@Autowired
	private CliRunner cliRunner;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	@Transactional
	public void should_RunWithoutExceptions() throws Exception
	{
		cliRunner.run(new DefaultApplicationArguments(
			new String[] { "--startDate=2017-01-01.13:00:00",
				"--duration=hourly", "--threshold=100" }));
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}

}
