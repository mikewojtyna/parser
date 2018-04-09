/**
 *
 */
package com.ef.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.ef.api.impl.RepositoryLogImporter;
import com.ef.parser.LogEntry;
import com.ef.repository.LogEntryRepository;
import com.google.common.testing.NullPointerTester;

/**
 * @author goobar
 *
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@SuppressWarnings("javadoc")
public class RepositoryLogImporterIntegrationTest
{
	@Rule
	public final TemporaryFolder tempFolder = new TemporaryFolder();

	@Autowired
	private LogImporter importer;

	private Path logFile;

	@Autowired
	private LogEntryRepository repository;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
		File logFileCopy = tempFolder.newFile();
		try (InputStream in = getClass()
			.getResourceAsStream("/test-access.log");
			OutputStream out = new FileOutputStream(logFileCopy))
		{
			IOUtils.copy(in, out);
			logFile = Paths.get(logFileCopy.toURI());
		}
	}

	@Test
	public void should_ImportLogFile() throws Exception
	{
		// when
		importer.importLogFile(logFile);

		// then
		// file has 3 entries
		Iterable<LogEntry> allEntries = repository.findAll();
		assertThat(allEntries).hasSize(3);
		// with following values
		assertThat(allEntries).containsOnly(

			// 2017-01-01 00:00:11.763|192.168.234.82|"GET /
			// HTTP/1.1"|200|"swcd (unknown version)
			// CFNetwork/808.2.16 Darwin/15.6.0"
			LogEntry.LogEntryBuilder.instance()
				.withDate(LocalDateTime.of(2017, 1, 1, 0, 0, 11,
					763_000_000))
				.withIp("192.168.234.82")
				.withRequest("\"GET / HTTP/1.1\"")
				.withStatusCode("200")
				.withUserAgent(
					"\"swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"")
				.build(),
			// 2017-01-01 00:00:21.164|192.168.234.82|"GET /
			// HTTP/1.1"|200|"swcd (unknown version)
			// CFNetwork/808.2.16 Darwin/15.6.0"
			LogEntry.LogEntryBuilder.instance()
				.withDate(LocalDateTime.of(2017, 1, 1, 0, 0, 21,
					164_000_000))
				.withIp("192.168.234.82")
				.withRequest("\"GET / HTTP/1.1\"")
				.withStatusCode("200")
				.withUserAgent(
					"\"swcd (unknown version) CFNetwork/808.2.16 Darwin/15.6.0\"")
				.build(),
			// 2017-01-01 00:00:23.003|192.168.169.194|"GET /
			// HTTP/1.1"|200|"Mozilla/5.0 (Windows NT 10.0; Win64;
			// x64) AppleWebKit/537.36 (KHTML, like Gecko)
			// Chrome/51.0.2704.79 Safari/537.36 Edge/14.14393"
			LogEntry.LogEntryBuilder.instance()
				.withDate(LocalDateTime.of(2017, 1, 1, 0, 0, 23,
					3_000_000))
				.withIp("192.168.169.194")
				.withRequest("\"GET / HTTP/1.1\"")
				.withStatusCode("200")
				.withUserAgent(
					"\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.79 Safari/537.36 Edge/14.14393\"")
				.build());

	}

	@Test
	public void should_Pass_NullTests() throws Exception
	{
		NullPointerTester tester = new NullPointerTester();
		tester.testAllPublicConstructors(RepositoryLogImporter.class);
		tester.testAllPublicInstanceMethods(importer);
	}

	@Test
	public void should_ThrowException_When_FileDoesntExist()
		throws Exception
	{
		// when
		assertThatThrownBy(() -> importer
			.importLogFile(Paths.get("doesnt", "exist.log")))
				// then
				.isInstanceOf(LogImporterException.class);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}

}
