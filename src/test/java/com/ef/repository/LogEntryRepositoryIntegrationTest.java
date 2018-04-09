/**
 *
 */
package com.ef.repository;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.ef.parser.LogEntry.LogEntryBuilder;

/**
 * @author goobar
 *
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@SuppressWarnings("javadoc")
public class LogEntryRepositoryIntegrationTest
{
	@Autowired
	private LogEntryRepository repository;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void should_FindIpAboveThreshold() throws Exception
	{
		// given
		LocalDateTime startDate = LocalDateTime.of(2010, 10, 13, 20,
			45);
		LocalDateTime endDate = LocalDateTime.of(2010, 10, 16, 20, 45);
		int threshold = 2;
		// only 89.75.165.209 and 89.75.165.211 appears more than 2
		// times in given date range
		// 89.75.165.210 appears 4 times, but in the wrong range
		repository.saveAll(Arrays.asList(

			// 2010-10-13 20:45
			LogEntryBuilder.instance()
				// 2010-10-13 20:45
				.withDate(
					LocalDateTime.of(2010, 10, 13, 20, 45))
				.withIp("89.75.165.209").build(),
			// 2010-10-14 10:30
			LogEntryBuilder.instance()
				.withDate(
					LocalDateTime.of(2010, 10, 14, 10, 30))
				.withIp("89.75.165.210").build(),
			// 2010-10-17 10:30
			LogEntryBuilder.instance()
				.withDate(
					LocalDateTime.of(2010, 10, 17, 10, 30))
				.withIp("89.75.165.210").build(),
			// 2010-10-12 10:30
			LogEntryBuilder.instance()
				.withDate(
					LocalDateTime.of(2010, 10, 12, 10, 30))
				.withIp("89.75.165.210").build(),
			// 2011-10-14 10:30
			LogEntryBuilder.instance()
				.withDate(
					LocalDateTime.of(2011, 10, 14, 10, 30))
				.withIp("89.75.165.210").build(),
			// 2010-10-15 20:10
			LogEntryBuilder.instance()
				.withDate(
					LocalDateTime.of(2010, 10, 15, 20, 10))
				.withIp("89.75.165.211").build(),
			// 2010-10-14 10:15
			LogEntryBuilder.instance()
				.withDate(
					LocalDateTime.of(2010, 10, 14, 10, 15))
				.withIp("89.75.165.211").build(),
			// 2010-10-16 08:30
			LogEntryBuilder.instance()
				.withDate(LocalDateTime.of(2010, 10, 16, 8, 30))
				.withIp("89.75.165.212").build(),
			// 2010-10-16 20:45
			LogEntryBuilder.instance()
				.withDate(
					LocalDateTime.of(2010, 10, 16, 20, 45))
				.withIp("89.75.165.213").build(),
			// 2010-10-13 21:45
			LogEntryBuilder.instance()
				.withDate(
					LocalDateTime.of(2010, 10, 13, 21, 45))
				.withIp("89.75.165.209").build(),
			// 2010-10-15 22:45
			LogEntryBuilder.instance()
				.withDate(
					LocalDateTime.of(2010, 10, 15, 22, 45))
				.withIp("89.75.165.209").build(),
			// 2010-10-14 20:10
			LogEntryBuilder.instance()
				.withDate(
					LocalDateTime.of(2010, 10, 14, 20, 10))
				.withIp("89.75.165.211").build()

		));

		// when
		Collection<String> foundIps = repository
			.findIpsAboveThreshold(threshold, startDate, endDate);

		// then
		assertThat(foundIps).containsOnly("89.75.165.209",
			"89.75.165.211");
	}

	@Test
	public void should_FindIpBetweenDates_When_EndDateIs3DaysLater()
		throws Exception
	{
		// given
		LocalDateTime startDate = LocalDateTime.of(2010, 10, 13, 20,
			45);
		LocalDateTime endDate = LocalDateTime.of(2010, 10, 16, 20, 45);
		repository.saveAll(Arrays.asList(

			// all of these should classify (are within the range)
			// 2010-10-13 20:45
			LogEntryBuilder.instance()
				// 2010-10-13 20:45
				.withDate(
					LocalDateTime.of(2010, 10, 13, 20, 45))
				.withIp("89.75.165.209").build(),
			LogEntryBuilder.instance()
				// 2010-10-14 10:30
				.withDate(
					LocalDateTime.of(2010, 10, 14, 10, 30))
				.withIp("89.75.165.210").build(),
			// 2010-10-15 20:10
			LogEntryBuilder.instance()
				.withDate(
					LocalDateTime.of(2010, 10, 15, 20, 10))
				.withIp("89.75.165.211").build(),
			// 2010-10-16 08:30
			LogEntryBuilder.instance()
				.withDate(LocalDateTime.of(2010, 10, 16, 8, 30))
				.withIp("89.75.165.212").build(),
			// 2010-10-16 20:45
			LogEntryBuilder.instance()
				.withDate(
					LocalDateTime.of(2010, 10, 16, 20, 45))
				.withIp("89.75.165.213").build(),

			// all of these should not classify (are outside the
			// range)
			// 2010-10-13 20:44
			LogEntryBuilder.instance()
				.withDate(
					LocalDateTime.of(2010, 10, 13, 20, 44))
				.withIp("89.75.165.214").build(),
			// 2010-10-12 10:30
			LogEntryBuilder.instance()
				.withDate(
					LocalDateTime.of(2010, 10, 12, 10, 30))
				.withIp("89.75.165.215").build(),
			// 2010-10-16 20:46
			LogEntryBuilder.instance()
				.withDate(
					LocalDateTime.of(2010, 10, 16, 20, 46))
				.withIp("89.75.165.216").build(),
			// 2010-10-17 10:30
			LogEntryBuilder.instance()
				.withDate(
					LocalDateTime.of(2010, 10, 17, 10, 30))
				.withIp("89.75.165.217").build(),
			// 2010-09-13 20:45
			LogEntryBuilder.instance()
				.withDate(LocalDateTime.of(2010, 9, 13, 20, 45))
				.withIp("89.75.165.218").build(),
			// 2010-11-16 20:45
			LogEntryBuilder.instance()
				.withDate(
					LocalDateTime.of(2010, 11, 16, 20, 45))
				.withIp("89.75.165.219").build(),
			// 2009-10-13 20:45
			LogEntryBuilder.instance()
				.withDate(
					LocalDateTime.of(2009, 10, 13, 20, 45))
				.withIp("89.75.165.219").build(),
			// 2011-10-16 20:45
			LogEntryBuilder.instance()
				.withDate(
					LocalDateTime.of(2011, 10, 16, 20, 45))
				.withIp("89.75.165.220").build()

		));

		// when
		Collection<String> foundIps = repository
			.findIpsAboveThreshold(0, startDate, endDate);

		// then
		assertThat(foundIps).containsOnly("89.75.165.209",
			"89.75.165.210", "89.75.165.211", "89.75.165.212",
			"89.75.165.213");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}

}
