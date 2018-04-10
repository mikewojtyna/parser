package com.ef;

import java.nio.charset.Charset;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.ef.parser.LogParser;
import com.ef.parser.impl.AccessLogParser;

/**
 * Application bootstrapper.
 *
 * @author goobar
 *
 */
@SpringBootApplication
public class Parser
{
	/**
	 * Executes the spring application.
	 *
	 * @param args
	 *                arguments
	 */
	public static void main(String[] args)
	{
		SpringApplication.run(Parser.class, args);
	}

	/**
	 * Configures and registers the {@link LogParser}. Creating a dedicated
	 * factory would be generally a better idea, but in such simple scenario
	 * this bean method will suffice for our needs.
	 *
	 * @return the configured log parser
	 */
	@Bean
	public LogParser logParser()
	{
		return new AccessLogParser("\\|", Charset.forName("UTF-8"),
			"yyyy-MM-dd HH:mm:ss.SSS");
	}
}
