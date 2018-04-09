/**
 *
 */
package com.ef.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.ef.parser.LogEntry;

/**
 * A Spring repository to provide IP query methods.
 *
 * @author goobar
 *
 */
public interface LogEntryRepository extends CrudRepository<LogEntry, Long>
{
	/**
	 * Returns all ips above given threshold and date range.
	 *
	 * @param threshold
	 *                threshold
	 * @param startDate
	 *                start date (inclusive)
	 * @param endDate
	 *                end date (inclusive)
	 * @return the collections containing ips above threshold and in given
	 *         range
	 */
	@Query(
	// SELECT
	"SELECT entry.ip FROM LogEntry entry "
		// WHERE
		+ "WHERE entry.date BETWEEN :startDate AND :endDate "
		// GROUP
		+ "GROUP BY entry.ip "
		// HAVING
		+ "HAVING COUNT(entry) > :threshold")
	Collection<String> findIpsAboveThreshold(
		@Param("threshold") long threshold,
		@Param("startDate") LocalDateTime startDate,
		@Param("endDate") LocalDateTime endDate);
}
