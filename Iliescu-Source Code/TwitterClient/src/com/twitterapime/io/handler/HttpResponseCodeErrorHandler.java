/*
 * HttpResponseCodeErrorHandler.java
 * 14/11/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.io.handler;

import com.twitterapime.parser.DefaultXMLHandler;
import com.twitterapime.parser.ParserException;

/**
 * <p>
 * Handler class for parsing the XML error messages from Twitter API.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.1
 */
public final class HttpResponseCodeErrorHandler extends DefaultXMLHandler {
	/**
	 * <p>
	 * Error message.
	 * </p>
	 */
	private String error;
	
	/**
	 * <p>
	 * Request message.
	 * </p>
	 */
	private String request;
	
	/**
	 * @see com.twitterapime.parser.XMLHandler#text(java.lang.String)
	 */
	public void text(String text) throws ParserException {
		text = text.trim();
		//
		if (xmlPath.equals("/hash/request")) {
			request = text;
		} else if (xmlPath.equals("/hash/error")) {
			error = text;
		}
	}
	
	/**
	 * <p>
	 * Return the parsed error message.
	 * </p>
	 * @return Message.
	 */
	public String getParsedErrorMessage() {
		return error;
	}
	
	/**
	 * <p>
	 * Return the parsed request message.
	 * </p>
	 * @return Message.
	 */
	public String getParsedRequestMessage() {
		return request;
	}
}