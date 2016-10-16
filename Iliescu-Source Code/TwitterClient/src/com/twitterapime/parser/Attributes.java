/*
 * Attributes.java
 * 16/11/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.parser;

/**
 * <p>
 * This interface defines a list of attributes. 
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.1
 */
public interface Attributes {
	/**
	 * <p>
	 * Get the index value of a given qualified name.
	 * </p>
	 * @param qName Qualified name.
	 * @return Index.
	 */
	public int getIndex(String qName);
	
	/**
	 * <p>
	 * Get the value associated to a given qualified name.
	 * </p>
	 * @param qName Qualified name.
	 * @return Value.
	 */
	public String getValue(String qName);
	
	/**
	 * <p>
	 * Get the number of attributes in the list.
	 * </p>
	 * @return Number.
	 */
	public int getLength();
	
	/**
	 * <p>
	 * Get the qualified name associated to the given index value.
	 * </p>
	 * @param index Index value.
	 * @return Qualified name.
	 */
	public String getQName(int index);
	
	/**
	 * <p>
	 * Get the type of a qualified name associated to the given index value.
	 * </p>
	 * @param index Index value.
	 * @return Type.
	 */
	public String getType(int index);
	
	/**
	 * <p>
	 * Get the value of a qualified name associated to the given index value.
	 * </p>
	 * @param index Index value.
	 * @return Value.
	 */
	public String getValue(int index);
}