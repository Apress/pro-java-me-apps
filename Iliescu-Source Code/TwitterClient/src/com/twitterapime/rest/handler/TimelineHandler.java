/*
 * TimelineHandler.java
 * 10/04/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest.handler;

import java.util.Hashtable;
import java.util.Vector;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.Attributes;
import com.twitterapime.parser.DefaultXMLHandler;
import com.twitterapime.parser.ParserException;
import com.twitterapime.rest.GeoLocation;
import com.twitterapime.rest.UserAccount;
import com.twitterapime.search.SearchDeviceListener;
import com.twitterapime.search.Tweet;

/**
 * <p>
 * Handler class for parsing the XML timeline from Twitter API. 
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.2
 */
public final class TimelineHandler extends DefaultXMLHandler {
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
	 * List of tweets.
	 * </p>
	 */
	private Vector tweetList = new Vector(10);
	
	/**
	 * <p>
	 * Hash with user account values.
	 * </p>
	 */
	private Hashtable userValues;

	/**
	 * <p>
	 * Hash with tweet values.
	 * </p>
	 */
	private Hashtable tweetValues;
	
	/**
	 * <p>
	 * Hash with user account values from reposted tweet.
	 * </p>
	 */
	private Hashtable reuserValues;

	/**
	 * <p>
	 * Hash with reposted tweet values.
	 * </p>
	 */
	private Hashtable retweetValues;
	
	/**
	 * <p>
	 * Hash with tweet's location values.
	 * </p>
	 */
	private Hashtable locationValues;
	
	/**
	 * <p>
	 * Search device listener object.
	 * </p> 
	 */
	private SearchDeviceListener listener;
	
	/**
	 * @see com.twitterapime.parser.DefaultXMLHandler#startElement(java.lang.String, java.lang.String, java.lang.String, com.twitterapime.parser.Attributes)
	 */
	public void startElement(String namespaceURI, String localName,
		String qName, Attributes attrs) throws ParserException {
		super.startElement(namespaceURI, localName, qName, attrs);
		//
		if (localName.toLowerCase().equals("status")) {
			tweetValues = new Hashtable(5);
			userValues = new Hashtable(25);
			retweetValues = new Hashtable(5);
			reuserValues = new Hashtable(25);
			locationValues = new Hashtable(10);
			//
			tweetValues.put(
				MetadataSet.TWEET_USER_ACCOUNT, new UserAccount(userValues));
			//
			tweetList.addElement(new Tweet(tweetValues));
		}
	}
	
	/**
	 * @see com.twitterapime.parser.DefaultXMLHandler#text(java.lang.String)
	 */
	public void text(String text) throws ParserException {
		text = text.trim();
		//
		if (xmlPath.startsWith("/statuses/status/retweeted_status/user/")) {
			uaHandler.populate(reuserValues, xmlPath, text);
		} else if (xmlPath.startsWith("/statuses/status/retweeted_status/")) {
			tHandler.populate(retweetValues, xmlPath, text);
		} else if (xmlPath.startsWith("/statuses/status/user/")) {
			uaHandler.populate(userValues, xmlPath, text);
		} else if (xmlPath.startsWith("/statuses/status/geo/")) {
			lHandler.populate(locationValues, xmlPath, text);
		} else if (xmlPath.startsWith("/statuses/status/place/")) {
			lHandler.populate(locationValues, xmlPath, text);
		} else if (xmlPath.startsWith("/statuses/status/")) {
			tHandler.populate(tweetValues, xmlPath, text);
		}
	}
	
	/**
	 * @see com.twitterapime.parser.DefaultXMLHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String namespaceURI, String localName, String qName)
		throws ParserException {
		super.endElement(namespaceURI, localName, qName);
		//
		if (localName.toLowerCase().equals("status")) {
			if (retweetValues.size() > 0) {  // is it a retweet?
				retweetValues.put(
					MetadataSet.TWEET_USER_ACCOUNT,
					new UserAccount(reuserValues));
				tweetValues.put(
					MetadataSet.TWEET_REPOSTED_TWEET, retweetValues);
			}
			//
			if (locationValues.size() > 0) {
				tweetValues.put(
					MetadataSet.TWEET_LOCATION,
					new GeoLocation(locationValues));
			}
			//
			fireTweetParsed((Tweet) tweetList.lastElement());
		}
	}
	
	/**
	 * <p>
	 * Return the parsed tweets.
	 * </p>
	 * @return Array of tweets.
	 */
	public Tweet[] getParsedTweets() {
		Tweet[] ts = new Tweet[tweetList.size()];
		tweetList.copyInto(ts);
		//
		return ts;
	}
	
	/**
	 * <p>
	 * Set the search device listener object.
	 * </p>
	 * @param listener Listener object.
	 */
	public void setSearchDeviceListener(SearchDeviceListener listener) {
		this.listener = listener;
	}
	
	/**
	 * <p>
	 * Fire the listened when a tweet is parsed.
	 * </p>
	 * @param tweet Parsed tweet.
	 */
	private void fireTweetParsed(Tweet tweet) {
		if (listener != null) {
			listener.tweetFound(tweet);
		}
	}
}