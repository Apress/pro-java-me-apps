/*
 * LimitExceededException.java
 * 16/08/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.search;

/**
 * <p>
 * This class represents an exception to notify that the number of calls to
 * Twitter Search API has exceeded the limit per IP within an hour period.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class LimitExceededException extends Exception {
	/**
	 * <p>
	 * Serial version UID.
	 * </p>
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <p>
	 * Create an instance of LimitExceededException class.
	 * </p> 
	 */
	public LimitExceededException() {
	}

	/**
	 * <p>
	 * Create an instance of LimitExceededException class that receives a custom
	 * message.
	 * </p> 
	 * @param msg The exception message.
	 */
	public LimitExceededException(String msg) {
		super(msg);
	}
}