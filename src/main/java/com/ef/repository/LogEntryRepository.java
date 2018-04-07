/**
 *
 */
package com.ef.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import org.springframework.data.repository.CrudRepository;
import com.ef.parser.LogEntry;

/**
 * A Spring repository to provide IP query methods.
 *
 * @author goobar
 *
 */
public interface LogEntryRepository extends CrudRepository<LogEntry, Long>
{
	@SuppressWarnings("javadoc")
	Collection<String> findIpsAboveThreshold(int threshold,
		LocalDateTime startDate, LocalDateTime endDate);
}
