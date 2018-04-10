/**
 *
 */
package com.ef.api;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDateTime;
import java.util.Collection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import com.ef.api.impl.BlockedIp;
import com.ef.api.impl.BlockedIpRepository;
import com.ef.api.impl.LogEntryRepository;
import com.ef.parser.LogEntry;

/**
 * @author goobar
 *
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@SuppressWarnings("javadoc")
public class RepositoryThresholdIpFinderIntegrationTest
{
	@Autowired
	private BlockedIpRepository blockedIpRepository;

	@Autowired
	private ThresholdIpFinder finder;

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
	@Transactional
	public void should_FindIps() throws Exception
	{
		// given
		repository.save(LogEntry.LogEntryBuilder.instance()
			.withIp("89.75.165.210")
			.withDate(LocalDateTime.of(2010, 10, 20, 20, 45))
			.build());

		// when
		Collection<String> ips = finder.findAboveThreshold(0,
			LocalDateTime.of(2010, 10, 20, 19, 50),
			Duration.HOURLY);

		// then
		assertThat(ips).containsOnly("89.75.165.210");

		Iterable<BlockedIp> blockedIps = blockedIpRepository.findAll();
		assertThat(blockedIps).hasSize(1);
		assertThat(blockedIps.iterator().next().getIp())
			.isEqualTo("89.75.165.210");
	}

	@Test
	@Transactional
	public void should_SaveBlockedIps() throws Exception
	{
		// given
		repository.save(LogEntry.LogEntryBuilder.instance()
			.withIp("89.75.165.210")
			.withDate(LocalDateTime.of(2010, 10, 20, 20, 45))
			.build());

		// when
		finder.findAboveThreshold(0,
			LocalDateTime.of(2010, 10, 20, 19, 50),
			Duration.HOURLY);

		// then
		Iterable<BlockedIp> blockedIps = blockedIpRepository.findAll();
		assertThat(blockedIps).hasSize(1);
		BlockedIp blockedIp = blockedIps.iterator().next();
		assertThat(blockedIp.getIp()).isEqualTo("89.75.165.210");
		assertThat(blockedIp.getComment()).isNotEmpty();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}

}
