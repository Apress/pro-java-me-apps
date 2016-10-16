/*
 * XMLHandler.java
 * 14/11/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.parser;

/**
 * <p>
 * This interface defines a XML document handler.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.1
 * @see DefaultXMLHandler
 */
public interface XMLHandler extends Handler {
	/**
	 * <p>
	 * Callback method called to notify the start of the document.
	 * </p>
	 * @throws ParserException If a parser error occurs.
	 */
	public void startDocument() throws ParserException;

	/**
	 * <p>
	 * Callback method called to notify the start of a element in the document.
	 * </p>
	 * @param namespaceURI URI.
	 * @param localName The local name.
	 * @param qName The qualified name.
	 * @param attrs The specified or defaulted attributes.
	 * @throws ParserException If a parser error occurs.
	 */
	public void startElement(String namespaceURI, String localName,
		String qName, Attributes attrs) throws ParserException;
	
	/**
	 * <p>
	 * Callback method called to notify a string data inside an element.
	 * </p>
	 * @param text String data.
	 * @throws ParserException If a parser error occurs.
	 */
	public void text(String text) throws ParserException;

	/**
	 * <p>
	 * Callback method called to notify the end of a element in the document.
	 * </p>
	 * @param namespaceURI URI.
	 * @param localName The local name.
	 * @param qName The qualified name.
	 * @throws ParserException If a parser error occurs.
	 */
	public void endElement(String namespaceURI, String localName, String qName)
		throws ParserException;

	/**
	 * <p>
	 * Callback method called to notify the end of the document.
	 * </p>
	 * @throws ParserException If a parser error occurs.
	 */
	public void endDocument() throws ParserException;
}