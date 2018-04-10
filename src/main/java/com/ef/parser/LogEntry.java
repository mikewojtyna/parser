/**
 *
 */
package com.ef.parser;

import java.time.LocalDateTime;
import java.util.Optional;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * A simple data object representing a log entry. To create an instance of this
 * class use {@link LogEntryBuilder}.
 *
 * @author goobar
 *
 */
@SuppressWarnings("javadoc")
@Entity
public class LogEntry
{
	private final LocalDateTime date;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private final String ip;

	private final String request;

	private final String statusCode;

	private final String userAgent;

	private LogEntry(LogEntryBuilder builder)
	{
		date = builder.date;
		ip = builder.ip;
		request = builder.request;
		statusCode = builder.statusCode;
		userAgent = builder.userAgent;
	}

	/**
	 * Id is not included in equals and hashcode implementations.
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		LogEntry other = (LogEntry) obj;
		if (date == null)
		{
			if (other.date != null)
			{
				return false;
			}
		}
		else if (!date.equals(other.date))
		{
			return false;
		}
		if (ip == null)
		{
			if (other.ip != null)
			{
				return false;
			}
		}
		else if (!ip.equals(other.ip))
		{
			return false;
		}
		if (request == null)
		{
			if (other.request != null)
			{
				return false;
			}
		}
		else if (!request.equals(other.request))
		{
			return false;
		}
		if (statusCode == null)
		{
			if (other.statusCode != null)
			{
				return false;
			}
		}
		else if (!statusCode.equals(other.statusCode))
		{
			return false;
		}
		if (userAgent == null)
		{
			if (other.userAgent != null)
			{
				return false;
			}
		}
		else if (!userAgent.equals(other.userAgent))
		{
			return false;
		}
		return true;
	}

	/**
	 * @return the date
	 */
	public Optional<LocalDateTime> getDate()
	{
		return Optional.ofNullable(date);
	}

	/**
	 * @return the ip
	 */
	public Optional<String> getIp()
	{
		return Optional.ofNullable(ip);
	}

	/**
	 * @return the request
	 */
	public Optional<String> getRequest()
	{
		return Optional.ofNullable(request);
	}

	/**
	 * @return the statusCode
	 */
	public Optional<String> getStatusCode()
	{
		return Optional.ofNullable(statusCode);
	}

	/**
	 * @return
	 */
	public Optional<String> getUserAgent()
	{
		return Optional.ofNullable(userAgent);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
			+ ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result
			+ ((request == null) ? 0 : request.hashCode());
		result = prime * result
			+ ((statusCode == null) ? 0 : statusCode.hashCode());
		result = prime * result
			+ ((userAgent == null) ? 0 : userAgent.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "LogEntry [date=" + date + ", id=" + id + ", ip=" + ip
			+ ", request=" + request + ", statusCode=" + statusCode
			+ ", userAgent=" + userAgent + "]";
	}

	public static class LogEntryBuilder
	{
		public static LogEntryBuilder instance()
		{
			return new LogEntryBuilder();
		}

		private LocalDateTime date;

		private String ip;

		private String request;

		private String statusCode;

		private String userAgent;

		public LogEntry build()
		{
			return new LogEntry(this);
		}

		public LogEntryBuilder withDate(LocalDateTime date)
		{
			this.date = date;
			return this;
		}

		public LogEntryBuilder withIp(String ip)
		{
			this.ip = ip;
			return this;
		}

		public LogEntryBuilder withRequest(String request)
		{
			this.request = request;
			return this;
		}

		public LogEntryBuilder withStatusCode(String statusCode)
		{
			this.statusCode = statusCode;
			return this;
		}

		public LogEntryBuilder withUserAgent(String userAgent)
		{
			this.userAgent = userAgent;
			return this;
		}
	}
}
