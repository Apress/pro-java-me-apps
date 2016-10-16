/*
 * SearchDeviceListener
 * 02/10/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.search;

import com.twitterapime.rest.Timeline;

/**
 * <p>
 * This interface defines the methods of a listener interested on the result of
 * a search operation from SearchDevice class.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.1
 * @since 1.0
 * @see SearchDevice
 * @see Tweet
 * @see Timeline
 */
public interface SearchDeviceListener {
	/**
	 * <p>
	 * This method is called when a tweet is received.
	 * </p>
	 * @param tweet Tweet found.
	 */
	public void tweetFound(Tweet tweet);
	
	/**
	 * <p>
	 * This method is called when the search operation is completed.
	 * </p>
	 */
	public void searchCompleted();

	/**
	 * <p>
	 * This method is called when an exception is thrown during the search
	 * operation. When a exception is thrown the search is finished.
	 * </p>
	 * @param cause The thrown exception.
	 */
	public void searchFailed(Throwable cause);
}