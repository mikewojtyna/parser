/**
 *
 */
package com.ef.api;

import java.nio.file.Path;

/**
 * A top-level interface to import log file into the database.
 *
 * @author goobar
 *
 */
public interface LogImporter
{
	/**
	 * Import a file into the database.
	 * 
	 * @param file
	 *                a file
	 * @throws NullPointerException
	 *                 if any argument is null
	 * @throws LogImporterException
	 *                 if fails
	 */
	void importLogFile(Path file)
		throws NullPointerException, LogImporterException;
}
