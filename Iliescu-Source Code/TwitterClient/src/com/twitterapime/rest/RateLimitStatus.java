/*
 * RateLimitStatus.java
 * 11/11/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest;

import java.util.Hashtable;

import com.twitterapime.model.DefaultEntity;

/**
 * <p>
 * This class defines an entity that represents the rate limit status.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.1
 * @see UserAccountManager
 */
public final class RateLimitStatus extends DefaultEntity {
	/**
	 * <p>
	 * Create an instance of RateLimitStatus class.
	 * </p>
	 */
	public RateLimitStatus() {
	}

	/**
	 * <p>
	 * Create an instance of RateLimitStatus class.
	 * </p>
	 * @param data The initial attributes/values.
	 */
	public RateLimitStatus(Hashtable data) {
		super(data);
	}
}