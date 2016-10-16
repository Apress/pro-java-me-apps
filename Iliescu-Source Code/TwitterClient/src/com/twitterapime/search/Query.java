/*
 * Query.java
 * 16/08/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.search;

import java.io.UnsupportedEncodingException;

/**
 * <p>
 * This class defines a structure of a query. A Query object represents a query
 * statement that is sent to Twitter Search API, so that it can return all the
 * tweets that match a criteria.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.1
 * @since 1.0
 * @see QueryComposer
 * @see SearchDevice
 */
public final class Query {
	/**
	 * <p>
	 * Hold the string that represents a query.
	 * </p>
	 */
	private String query;

	/**
	 * <p>
	 * Create an instance of Query class.
	 * </p>
	 * @param query The string of query.
	 * @throws InvalidQueryException If query is not a valid UTF-8 string.
	 * @throws IllegalArgumentException If query is null.
	 */
	Query(String query) {
		if (query == null) {
			throw new IllegalArgumentException("Query must not be null.");
		}
		//
		try {
			this.query = new String(query.getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new InvalidQueryException("Invalid UTF-8 string: " + query);
		}
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (o == null || !(o instanceof Query)) {
			return false;
		} else {
			return query.equals(((Query)o).query);
		}
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return query.hashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return query;
	}
}