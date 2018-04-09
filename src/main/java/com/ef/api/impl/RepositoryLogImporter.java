/**
 *
 */
package com.ef.api.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import org.springframework.stereotype.Service;
import com.ef.api.LogImporter;
import com.ef.api.LogImporterException;
import com.ef.parser.LogParser;
import com.ef.repository.LogEntryRepository;

/**
 * A Log importer that uses {@link LogEntryRepository} to stored results parsed
 * by {@link LogParser}.
 *
 * @author goobar
 *
 */
@Service
public class RepositoryLogImporter implements LogImporter
{

	private final LogParser parser;

	private final LogEntryRepository repository;

	/**
	 * @param parser
	 *                a parser
	 * @param repository
	 *                a repository
	 * @throws NullPointerException
	 *                 if any argument is null
	 */
	public RepositoryLogImporter(LogParser parser,
		LogEntryRepository repository) throws NullPointerException
	{
		validate(parser, repository);
		this.parser = parser;
		this.repository = repository;
	}

	/* (non-Javadoc)
	 * @see com.ef.api.LogImporter#importLogFile(java.nio.file.Path)
	 */
	@Override
	public void importLogFile(Path file)
		throws NullPointerException, LogImporterException
	{
		validate(file);
		try (InputStream stream = Files.newInputStream(file))
		{
			repository.saveAll(parser.parse(stream));
		}
		catch (IOException e)
		{
			throw new LogImporterException(MessageFormat
				.format("Failed to import file {0}.", file), e);
		}

	}

	/**
	 * @param parser
	 * @param repository
	 */
	private void validate(LogParser parser, LogEntryRepository repository)
	{
		checkNotNull(parser, "'parser' cannot be null");
		checkNotNull(repository, "'repository' cannot be null");
	}

	/**
	 * @param file
	 */
	private void validate(Path file)
	{
		checkNotNull(file, "'file' cannot be null");
	}

}
