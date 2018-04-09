/**
 *
 */
package com.ef.cli;

import java.nio.file.Paths;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import com.ef.api.Duration;
import com.ef.api.LogImporter;
import com.ef.api.ThresholdIpFinder;

/**
 * A CLI runner.
 *
 * @author goobar
 *
 */
public class CliRunner implements ApplicationRunner
{

	private static final Logger LOGGER = LoggerFactory
		.getLogger(CliRunner.class);

	private final ThresholdIpFinder ipFinder;

	private final LogImporter logImporter;

	/**
	 * @param ipFinder
	 *                ip finder strategy
	 * @param logImporter
	 *                log importer strategy
	 */
	public CliRunner(ThresholdIpFinder ipFinder, LogImporter logImporter)
	{
		this.ipFinder = ipFinder;
		this.logImporter = logImporter;
	}

	/* (non-Javadoc)
	 * @see org.springframework.boot.ApplicationRunner#run(org.springframework.boot.ApplicationArguments)
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception
	{
		try
		{
			handleLogImporterArgs(args);
			handleIpFinderArgs(args);
		}
		catch (IllegalArgumentException e)
		{
			LOGGER.info(e.getMessage());
		}
	}

	/**
	 * @param string
	 * @return
	 */
	private String argVal(String name, ApplicationArguments args)
	{
		List<String> values = args.getOptionValues(name);
		if (values == null || values.isEmpty())
		{
			throw new IllegalArgumentException(MessageFormat
				.format("Argument --{0} is required.", name));
		}
		return values.get(0);
	}

	/**
	 * @param args
	 * @return
	 */
	private Duration duration(ApplicationArguments args)
	{
		return Duration.valueOf(argVal("duration", args).toUpperCase());
	}

	/**
	 * @param args
	 */
	private void handleIpFinderArgs(ApplicationArguments args)
	{
		ipFinder.findAboveThreshold(threshold(args), startDate(args),
			duration(args));
	}

	/**
	 * @param args
	 */
	private void handleLogImporterArgs(ApplicationArguments args)
	{
		String accesLogArgName = "accesslog";
		if (args.containsOption(accesLogArgName))
		{
			logImporter.importLogFile(
				Paths.get(argVal(accesLogArgName, args)));
		}
	}

	/**
	 * @param args
	 * @return
	 */
	private LocalDateTime startDate(ApplicationArguments args)
	{
		return LocalDateTime.parse(argVal("startDate", args),
			DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss"));
	}

	/**
	 * @param args
	 * @return
	 */
	private int threshold(ApplicationArguments args)
	{
		return Integer.valueOf(argVal("threshold", args));
	}

}
