/**
 *
 */
package com.ef.api.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import java.time.LocalDateTime;
import java.util.Collection;
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
public class RepositoryThresholdIpFinder implements ThresholdIpFinder
{

	private final LogEntryRepository repository;

	/**
	 * @param repository
	 *                a repository
	 * @throws NullPointerException
	 *                 if any argument is null
	 */
	public RepositoryThresholdIpFinder(LogEntryRepository repository)
		throws NullPointerException
	{
		validate(repository);
		this.repository = repository;
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
		return repository.findIpsAboveThreshold(threshold, startDate,
			calculateEndDate(startDate, duration));
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
	 */
	private void validate(int threshold, LocalDateTime startDate,
		Duration duration)
	{
		checkNotNull(startDate, "'startDate' cannot be null");
		checkNotNull(duration, "'duration' cannot be null");
	}

	/**
	 * @param repository
	 */
	private void validate(LogEntryRepository repository)
	{
		checkNotNull(repository, "'repository' cannot be null");
	}

}
