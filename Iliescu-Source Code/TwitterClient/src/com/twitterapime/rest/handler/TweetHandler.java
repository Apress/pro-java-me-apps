/*
 * TweetHandler.java
 * 23/11/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest.handler;

import java.util.Hashtable;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.DefaultXMLHandler;
import com.twitterapime.util.StringUtil;

/**
 * <p>
 * Handler class for parsing the XML tweet from Twitter API. 
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.1
 * @since 1.1
 */
public final class TweetHandler extends DefaultXMLHandler {
	/**
	 * <p>
	 * Populate the given hash according to the tags and their values
	 * </p>
	 * @param data Hash to be populated.
	 * @param path XML path.
	 * @param text Tag's text.
	 */
	public void populate(Hashtable data, String path, String text) {
		if (path.endsWith("/created_at")) {
			data.put(
				MetadataSet.TWEET_PUBLISH_DATE,
				"" + StringUtil.convertTweetDateToLong(text));
		} else if (path.endsWith("/id")) {
			data.put(MetadataSet.TWEET_ID, text);
		} else if (path.endsWith("/text")) {
			data.put(MetadataSet.TWEET_CONTENT, text);
		} else if (path.endsWith("/source")) {
			data.put(MetadataSet.TWEET_SOURCE, StringUtil.removeTags(text));
		} else if (path.endsWith("/favorited")) {
			data.put(MetadataSet.TWEET_FAVOURITE, text);
		}
	}
}