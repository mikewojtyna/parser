/**
 *
 */
package com.ef.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.ef.api.impl.BlockedIpRepository;
import com.ef.api.impl.LogEntryRepository;
import com.ef.api.impl.RepositoryThresholdIpFinder;
import com.google.common.testing.NullPointerTester;

/**
 * @author goobar
 *
 */
@SuppressWarnings("javadoc")
public class RepositoryThresholdIpFinderTest
{

	private LogEntryRepository repository;

	private ThresholdIpFinder thresholdIpFinder;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		repository = mock(LogEntryRepository.class);
		thresholdIpFinder = new RepositoryThresholdIpFinder(repository,
			mock(BlockedIpRepository.class));
	}

	@Test
	public void should_FindIpsAboveThreshold_When_DurationIsDaily()
		throws Exception
	{
		// given
		int threshold = randomThreshold();
		LocalDateTime startDate = LocalDateTime.of(2018, 10, 13, 20,
			25);
		Duration duration = Duration.DAILY;
		when(repository.findIpsAboveThreshold(threshold, startDate,
			LocalDateTime.of(2018, 10, 14, 20, 25))).thenReturn(
				Arrays.asList("192.168.1.1", "192.168.0.1"));

		// when
		Collection<String> ips = thresholdIpFinder
			.findAboveThreshold(threshold, startDate, duration);

		// then
		assertThat(ips).containsOnly("192.168.1.1", "192.168.0.1");
	}

	@Test
	public void should_FindIpsAboveThreshold_When_DurationIsHourly()
		throws Exception
	{
		// given
		int threshold = randomThreshold();
		LocalDateTime startDate = LocalDateTime.of(2018, 10, 13, 20,
			25);
		Duration duration = Duration.HOURLY;
		when(repository.findIpsAboveThreshold(threshold, startDate,
			LocalDateTime.of(2018, 10, 13, 21, 25))).thenReturn(
				Arrays.asList("192.168.1.1", "192.168.0.1"));

		// when
		Collection<String> ips = thresholdIpFinder
			.findAboveThreshold(threshold, startDate, duration);

		// then
		assertThat(ips).containsOnly("192.168.1.1", "192.168.0.1");
	}

	@Test
	public void should_Pass_NullTests() throws Exception
	{
		NullPointerTester tester = new NullPointerTester();
		tester.testAllPublicConstructors(
			RepositoryThresholdIpFinder.class);
		tester.testAllPublicInstanceMethods(thresholdIpFinder);
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
	private int randomThreshold()
	{
		return new Random().nextInt(300);
	}

}
