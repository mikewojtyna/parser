/**
 *
 */
package com.ef.api;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * A top-level API service to find all connections above give threshold values.
 *
 * @author goobar
 *
 */
public interface ThresholdFinder
{
	/**
	 * Finds all IPs which made more requests than {@code threshold} value
	 * in given time period.
	 *
	 * @param threshold
	 *                only IPs which made more requests than this value
	 *                between {@code startDate} and
	 *                {@code startDate + duration} will be returned
	 * @param startDate
	 *                a start date
	 * @param duration
	 *                a duration
	 * @return the collection of IPs which made more connections than
	 *         threshold in given duration
	 * @throws NullPointerException
	 *                 if any argument is null
	 * @throws ThresholdFinderException
	 *                 if fails
	 */
	Collection<String> findAboveThreshold(int threshold,
		LocalDateTime startDate, Duration duration)
		throws NullPointerException, ThresholdFinderException;
}
