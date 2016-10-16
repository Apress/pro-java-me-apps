/*
 * Entity.java
 * 16/08/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.model;

import java.util.Date;

/**
 * <p>
 * This interface defines some basic accessor methods to access different typed
 * attributes from a data entity. 
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.1
 * @since 1.0
 * @see DefaultEntity
 */
public interface Entity {
	/**
	 * <p>
	 * Get the values of an array attribute. 
	 * </p>
	 * @param attr Array attribute to query the data for. 
	 * @return The array values of the given attribute.
	 */
	public Object[] getArray(String attr);

	/**
	 * <p>
	 * Get the value of a date attribute. 
	 * </p>
	 * @param attr Date attribute to query the data for. 
	 * @return The date value of the given attribute.
	 */
	public Date getDate(String attr);

	/**
	 * <p>
	 * Get the value of an int attribute. 
	 * </p>
	 * @param attr Int attribute to query the data for. 
	 * @return The int value of the given attribute.
	 */
	public int getInt(String attr);

	/**
	 * <p>
	 * Get the value of a long attribute. 
	 * </p>
	 * @param attr Long attribute to query the data for. 
	 * @return The long value of the given attribute.
	 */
	public long getLong(String attr);

	/**
	 * <p>
	 * Get the value of an object attribute. 
	 * </p>
	 * @param attr Object attribute to query the data for. 
	 * @return The object value of the given attribute.
	 */
	public Object getObject(String attr);

	/**
	 * <p>
	 * Get the value of a string attribute. 
	 * </p>
	 * @param attr String attribute to query the data for. 
	 * @return The string value of the given attribute.
	 */
	public String getString(String attr);
}