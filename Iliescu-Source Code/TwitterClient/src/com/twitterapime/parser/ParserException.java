/*
 * ParserException.java
 * 16/08/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.parser;

/**
 * <p>
 * This class represents an exception to notify about situations where there is
 * any error in the document format.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.1
 * @since 1.0
 */
public class ParserException extends Exception {
	/**
	 * <p>
	 * Serial version UID.
	 * </p>
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <p>
	 * Create an instance of ParserException class.
	 * </p> 
	 */
	public ParserException() {
	}

	/**
	 * <p>
	 * Create an instance of ParserException class that receives a custom
	 * message.
	 * </p> 
	 * @param msg The exception message.
	 */
	public ParserException(String msg) {
		super(msg);
	}
}