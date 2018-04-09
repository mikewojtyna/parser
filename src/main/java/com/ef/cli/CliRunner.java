/**
 *
 */
package com.ef.cli;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import com.ef.api.Duration;
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

	/**
	 * @param ipFinder
	 *                ip finder strategy
	 */
	public CliRunner(ThresholdIpFinder ipFinder)
	{
		this.ipFinder = ipFinder;
	}

	/* (non-Javadoc)
	 * @see org.springframework.boot.ApplicationRunner#run(org.springframework.boot.ApplicationArguments)
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception
	{
		try
		{
			ipFinder.findAboveThreshold(threshold(args),
				startDate(args), duration(args));
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
