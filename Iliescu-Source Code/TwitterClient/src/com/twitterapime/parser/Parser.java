/*
 * Parser.java
 * 24/10/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.parser;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * This interface defines the necessary methods of a parser in order to retrieve
 * the content of a document.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.1
 * @since 1.0
 * @see ParserFactory
 * @see Handler
 */
public abstract class Parser {
	/**
	 * <p>
	 * Parse the content of the input stream specified as XML using the
	 * specified {@link Handler}.
	 * </p>
	 * @param stream The InputSource containing the content to be parsed.
	 * @param handler The handler to use.
	 * @exception ParserException If there is an error in the document format.
	 * @exception IOException If an I/O error occurs.
	 */
	public abstract void parse(InputStream stream, Handler handler)
		throws IOException,	ParserException;
}