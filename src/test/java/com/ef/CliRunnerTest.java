/**
 *
 */
package com.ef;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import java.time.LocalDateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.DefaultApplicationArguments;
import com.ef.api.Duration;
import com.ef.api.ThresholdIpFinder;
import com.ef.cli.CliRunner;

/**
 * @author goobar
 *
 */
@SuppressWarnings("javadoc")
public class CliRunnerTest
{

	private ThresholdIpFinder ipFinder;

	private CliRunner runner;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		ipFinder = mock(ThresholdIpFinder.class);
		runner = new CliRunner(ipFinder);
	}

	@Test
	public void should_NotInteractWihtIpFinder_When_DurationIsNotGiven()
		throws Exception
	{
		// given
		DefaultApplicationArguments args = new DefaultApplicationArguments(
			new String[] { "--startDate=2017-01-01.13:00:00",
				"--threshold=250" });

		// when
		runner.run(args);

		// then
		verifyZeroInteractions(ipFinder);
	}

	@Test
	public void should_NotInteractWihtIpFinder_When_DurationIsWithoutValue()
		throws Exception
	{
		// given
		DefaultApplicationArguments args = new DefaultApplicationArguments(
			new String[] { "--startDate=2017-01-01.13:00:00",
				"--duration", "--threshold=250" });

		// when
		runner.run(args);

		// then
		verifyZeroInteractions(ipFinder);
	}

	@Test
	public void should_NotInteractWihtIpFinder_When_StartDateArgIsNotGiven()
		throws Exception
	{
		// given
		DefaultApplicationArguments args = new DefaultApplicationArguments(
			new String[] { "--duration=daily", "--threshold=250" });

		// when
		runner.run(args);

		// then
		verifyZeroInteractions(ipFinder);
	}

	@Test
	public void should_NotInteractWihtIpFinder_When_StartDateIsWithoutValue()
		throws Exception
	{
		// given
		DefaultApplicationArguments args = new DefaultApplicationArguments(
			new String[] { "--startDate", "--duration=daily",
				"--threshold=250" });

		// when
		runner.run(args);

		// then
		verifyZeroInteractions(ipFinder);
	}

	@Test
	public void should_NotInteractWihtIpFinder_When_ThresholdIsNotGiven()
		throws Exception
	{
		// given
		DefaultApplicationArguments args = new DefaultApplicationArguments(
			new String[] { "--startDate=2017-01-01.13:00:00",
				"--duration=daily" });

		// when
		runner.run(args);

		// then
		verifyZeroInteractions(ipFinder);
	}

	@Test
	public void should_NotInteractWihtIpFinder_When_ThresholdIsWithoutValue()
		throws Exception
	{
		// given
		DefaultApplicationArguments args = new DefaultApplicationArguments(
			new String[] { "--startDate=2017-01-01.13:00:00",
				"--duration=daily", "--threshold" });

		// when
		runner.run(args);

		// then
		verifyZeroInteractions(ipFinder);
	}

	@Test
	public void should_ParseArgs_When_DurationIsDaily() throws Exception
	{
		// given
		DefaultApplicationArguments args = new DefaultApplicationArguments(
			new String[] { "--startDate=2017-01-01.13:00:00",
				"--duration=daily", "--threshold=250" });

		// when
		runner.run(args);

		// then
		verify(ipFinder, times(1)).findAboveThreshold(250,
			LocalDateTime.of(2017, 1, 1, 13, 0, 0), Duration.DAILY);
	}

	@Test
	public void should_ParseArgs_When_DurationIsHourly() throws Exception
	{
		// given
		DefaultApplicationArguments args = new DefaultApplicationArguments(
			new String[] { "--startDate=2017-01-01.13:00:00",
				"--duration=hourly", "--threshold=100" });

		// when
		runner.run(args);

		// then
		verify(ipFinder, times(1)).findAboveThreshold(100,
			LocalDateTime.of(2017, 1, 1, 13, 0, 0),
			Duration.HOURLY);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}

}
