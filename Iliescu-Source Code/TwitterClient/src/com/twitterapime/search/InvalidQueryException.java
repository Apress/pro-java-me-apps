/*
 * InvalidQueryException.java
 * 16/08/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.search;

/**
 * <p>
 * This class represents an exception to notify about an error in a query sent
 * to Twitter Search API.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class InvalidQueryException extends RuntimeException {
	/**
	 * <p>
	 * Serial version UID.
	 * </p>
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <p>
	 * Create an instance of InvalidQueryException class.
	 * </p> 
	 */
	public InvalidQueryException() {
	}

	/**
	 * <p>
	 * Create an instance of InvalidQueryException class that receives a custom
	 * message.
	 * </p> 
	 * @param msg The exception message.
	 */
	public InvalidQueryException(String msg) {
		super(msg);
	}
}