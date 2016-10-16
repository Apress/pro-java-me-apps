/*
 * StatusHandler.java
 * 09/04/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest.handler;

import java.util.Hashtable;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.DefaultXMLHandler;
import com.twitterapime.parser.ParserException;
import com.twitterapime.rest.GeoLocation;
import com.twitterapime.rest.UserAccount;
import com.twitterapime.search.Tweet;

/**
 * <p>
 * Handler class for parsing the status' XML results from Twitter API. 
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.1
 * @since 1.2
 */
public final class StatusHandler extends DefaultXMLHandler {
	/**
	 * <p>
	 * User Account XML handler object.
	 * </p>
	 */
	private UserAccountHandler uaHandler = new UserAccountHandler();
	
	/**
	 * <p>
	 * Tweet XML handler object.
	 * </p>
	 */
	private TweetHandler tHandler = new TweetHandler();
	
	/**
	 * <p>
	 * GeoLocation XML handler object.
	 * </p>
	 */
	private GeoLocationHandler lHandler = new GeoLocationHandler();
	
	/**
	 * <p>
	 * Hash with user account values.
	 * </p>
	 */
	private Hashtable userAccountValues = new Hashtable(25);
	
	/**
	 * <p>
	 * Hash with user account values from reposted tweet.
	 * </p>
	 */
	private Hashtable reuserAccountValues = new Hashtable(25);

	/**
	 * <p>
	 * Hash with tweet values.
	 * </p>
	 */
	private Hashtable tweetValues = new Hashtable(10);
	
	/**
	 * <p>
	 * Hash with reposted tweet values.
	 * </p>
	 */
	private Hashtable retweetValues = new Hashtable(10);
	
	/**
	 * <p>
	 * Hash with tweet's location values.
	 * </p>
	 */
	private Hashtable locationValues = new Hashtable(10);

	/**
	 * @see com.twitterapime.parser.DefaultXMLHandler#text(java.lang.String)
	 */
	public void text(String text) throws ParserException {
		text = text.trim();
		//
		if (xmlPath.startsWith("/status/retweeted_status/user/")) {
			uaHandler.populate(reuserAccountValues, xmlPath, text);
		} else if (xmlPath.startsWith("/status/retweeted_status/")) {
			tHandler.populate(retweetValues, xmlPath, text);
		} else if (xmlPath.startsWith("/status/user/")) {
			uaHandler.populate(userAccountValues, xmlPath, text);
		} else if (xmlPath.startsWith("/status/geo/")) {
			lHandler.populate(locationValues, xmlPath, text);
		} else if (xmlPath.startsWith("/status/place/")) {
			lHandler.populate(locationValues, xmlPath, text);
		} else if (xmlPath.startsWith("/status/")) {
			tHandler.populate(tweetValues, xmlPath, text);
		}
	}
	
	/**
	 * @see com.twitterapime.parser.DefaultXMLHandler#endDocument()
	 */
	public void endDocument() throws ParserException {
		tweetValues.put(
			MetadataSet.TWEET_USER_ACCOUNT, new UserAccount(userAccountValues));
		//
		if (retweetValues.size() > 0) { // is it a retweet?
			retweetValues.put(
				MetadataSet.TWEET_USER_ACCOUNT,
				new UserAccount(reuserAccountValues));
			tweetValues.put(
				MetadataSet.TWEET_REPOSTED_TWEET, new Tweet(retweetValues));
		}
		//
		if (locationValues.size() > 0) {
			tweetValues.put(
				MetadataSet.TWEET_LOCATION,
				new GeoLocation(locationValues));
		}
	}
	
	/**
	 * <p>
	 * Return the parsed tweet.
	 * </p>
	 * @return Tweet.
	 */
	public Tweet getParsedTweet() {
		return new Tweet(tweetValues);
	}
	
	/**
	 * <p>
	 * Load the parsed values into the given tweet.
	 * </p>
	 * @param tweet Tweet to be loaded.
	 */
	public void loadParsedTweet(Tweet tweet) {
		tweet.setData(tweetValues);
	}
}
