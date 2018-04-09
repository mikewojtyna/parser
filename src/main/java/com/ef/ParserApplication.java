package com.ef;

import java.nio.charset.Charset;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.ef.parser.LogParser;
import com.ef.parser.impl.AccessLogParser;

@SpringBootApplication
public class ParserApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(ParserApplication.class, args);
	}

	@Bean
	public LogParser parser()
	{
		return new AccessLogParser("\\|", Charset.forName("UTF-8"),
			"yyyy-MM-dd HH:mm:ss.SSS");
	}
}
