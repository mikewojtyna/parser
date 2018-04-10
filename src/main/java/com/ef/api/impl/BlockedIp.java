/**
 *
 */
package com.ef.api.impl;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * A blocked IP db entity.
 *
 * @author goobar
 *
 */
@SuppressWarnings("javadoc")
@Entity
public class BlockedIp
{
	private String comment;

	@Id
	private String ip;

	public BlockedIp()
	{
		super();
	}

	public BlockedIp(String ip, String comment)
	{
		this.ip = ip;
		this.comment = comment;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
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
		BlockedIp other = (BlockedIp) obj;
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
		return true;
	}

	/**
	 * @return the comment
	 */
	public String getComment()
	{
		return comment;
	}

	/**
	 * @return the ip
	 */
	public String getIp()
	{
		return ip;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "BlockedIp [comment=" + comment + ", ip=" + ip + "]";
	}
}
