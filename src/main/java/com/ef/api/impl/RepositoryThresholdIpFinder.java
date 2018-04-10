/**
 *
 */
package com.ef.api.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.ef.api.Duration;
import com.ef.api.ThresholdFinderException;
import com.ef.api.ThresholdIpFinder;
import com.ef.repository.LogEntryRepository;

/**
 * Implementation using repository to perform queries.
 *
 * @author goobar
 *
 */
@Service
public class RepositoryThresholdIpFinder implements ThresholdIpFinder
{

	private final BlockedIpRepository blockedIpRepository;

	private final LogEntryRepository repository;

	/**
	 * @param repository
	 *                a repository
	 * @param blockedIpRepository
	 *                a repository to store blocked ips
	 * @throws NullPointerException
	 *                 if any argument is null
	 */
	public RepositoryThresholdIpFinder(LogEntryRepository repository,
		BlockedIpRepository blockedIpRepository)
		throws NullPointerException
	{
		validate(repository, blockedIpRepository);
		this.repository = repository;
		this.blockedIpRepository = blockedIpRepository;
	}

	/* (non-Javadoc)
	 * @see com.ef.api.ThresholdIpFinder#findAboveThreshold(int, java.time.LocalDateTime, com.ef.api.Duration)
	 */
	@Override
	public Collection<String> findAboveThreshold(int threshold,
		LocalDateTime startDate, Duration duration)
		throws NullPointerException, ThresholdFinderException
	{
		validate(threshold, startDate, duration);
		Collection<String> ips = repository.findIpsAboveThreshold(
			threshold, startDate,
			calculateEndDate(startDate, duration));
		blockedIpRepository.saveAll(ips.stream()
			.map(ip -> new BlockedIp(ip,
				comment(ip, threshold, startDate, duration)))
			.collect(Collectors.toList()));
		return ips;
	}

	/**
	 * @param startDate
	 * @param duration
	 * @return
	 */
	private LocalDateTime calculateEndDate(LocalDateTime startDate,
		Duration duration)
	{
		switch (duration)
		{
			case DAILY:
				return startDate.plusDays(1);

			default:
				return startDate.plusHours(1);
		}
	}

	/**
	 * @param threshold
	 * @param startDate
	 * @param duration
	 * @return
	 */
	private String comment(String ip, int threshold,
		LocalDateTime startDate, Duration duration)
	{
		// TODO: should use i18n MessageSource instead
		return MessageFormat.format(
			"IP {0} blocked. Too many connections (> {1}) made {2} starting from {3} date.",
			ip, threshold, duration, startDate);
	}

	/**
	 * @param threshold
	 * @param startDate
	 * @param duration
	 */
	private void validate(int threshold, LocalDateTime startDate,
		Duration duration)
	{
		checkNotNull(startDate, "'startDate' cannot be null");
		checkNotNull(duration, "'duration' cannot be null");
	}

	/**
	 * @param repository
	 * @param blockedIpRepository
	 */
	private void validate(LogEntryRepository repository,
		BlockedIpRepository blockedIpRepository)
	{
		checkNotNull(repository, "'repository' cannot be null");
		checkNotNull(blockedIpRepository,
			"'blockedIpRepository' cannot be null");
	}

}
