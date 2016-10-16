/*
 * DefaultXMLHandler.java
 * 14/11/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.parser;

/**
 * <p>
 * This class defines a default XML document handler.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.1
 */
public class DefaultXMLHandler implements XMLHandler {
	/**
	 * <p>
	 * Current reading path.
	 * </p>
	 */
	protected String xmlPath = "";

	/**
	 * @see com.twitterapime.parser.XMLHandler#startDocument()
	 */
	public void startDocument() throws ParserException {
	}

	/**
	 * @see com.twitterapime.parser.XMLHandler#startElement(java.lang.String, java.lang.String, java.lang.String, com.twitterapime.parser.Attributes)
	 */
	public void startElement(String namespaceURI, String localName,
		String qName, Attributes attrs) throws ParserException {
		xmlPath += '/' + localName.toLowerCase();
	}

	/**
	 * @see com.twitterapime.parser.XMLHandler#text(java.lang.String)
	 */
	public void text(String text) throws ParserException {
	}

	/**
	 * @see com.twitterapime.parser.XMLHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String namespaceURI, String localName, String qName)
		throws ParserException {
		xmlPath = xmlPath.substring(0, xmlPath.lastIndexOf('/'));
	}

	/**
	 * @see com.twitterapime.parser.XMLHandler#endDocument()
	 */
	public void endDocument() throws ParserException {
	}
}