/*
 * RateLimitStatusHandler.java
 * 19/11/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest.handler;

import java.util.Hashtable;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.DefaultXMLHandler;
import com.twitterapime.parser.ParserException;
import com.twitterapime.rest.RateLimitStatus;
import com.twitterapime.util.StringUtil;

/**
 * <p>
 * Handler class for parsing the XML rate limit status from Twitter API. 
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.1
 */
public class RateLimitStatusHandler extends DefaultXMLHandler {
	/**
	 * <p>
	 * Hash with rate limit status values.
	 * </p>
	 */
	private Hashtable limitStatusValues = new Hashtable(4);
	
	/**
	 * @see com.twitterapime.parser.DefaultXMLHandler#text(java.lang.String)
	 */
	public void text(String text) throws ParserException {
		text = text.trim();
		//
		if (xmlPath.equals("/hash/remaining-hits")) {
			limitStatusValues.put(
				MetadataSet.RATELIMITSTATUS_REMAINING_HITS, text);
		} else if (xmlPath.equals("/hash/hourly-limit")) {
			limitStatusValues.put(
				MetadataSet.RATELIMITSTATUS_HOURLY_LIMIT, text);
		} else if (xmlPath.equals("/hash/reset-time")) {
			limitStatusValues.put(
				MetadataSet.RATELIMITSTATUS_RESET_TIME,
				"" + StringUtil.convertTweetDateToLong(text));
		}
	}

	/**
	 * <p>
	 * Return the parsed rate limit status info.
	 * </p>
	 * @return Rate limit status.
	 */
	public RateLimitStatus getParsedRateLimitStatus() {
		return new RateLimitStatus(limitStatusValues);
	}
}