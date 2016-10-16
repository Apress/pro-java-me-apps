/*
 * DirectMessageHandler.java
 * 06/04/2010
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
import com.twitterapime.rest.UserAccount;
import com.twitterapime.search.SearchDeviceListener;
import com.twitterapime.search.Tweet;

/**
 * <p>
 * Handler class for parsing the XML Direct Message from Twitter API. 
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.2
 */
public final class DirectMessageHandler extends DefaultXMLHandler {
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
	 * List of tweets.
	 * </p>
	 */
	private Vector dmList = new Vector(10);
	
	/**
	 * <p>
	 * Hash with user account values.
	 * </p>
	 */
	private Hashtable senderValues;

	/**
	 * <p>
	 * Hash with recipient user account values.
	 * </p>
	 */
	private Hashtable recipientValues;

	/**
	 * <p>
	 * Hash with tweet values.
	 * </p>
	 */
	private Hashtable dmValues;
	
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
		if (localName.toLowerCase().equals("direct_message")) {
			dmValues = new Hashtable(5);
			senderValues = new Hashtable(25);
			recipientValues = new Hashtable(25);
			//
			dmValues.put(
				MetadataSet.TWEET_USER_ACCOUNT, new UserAccount(senderValues));
			dmValues.put(
				MetadataSet.TWEET_RECIPIENT_ACCOUNT,
				new UserAccount(recipientValues));
			//
			dmList.addElement(new Tweet(dmValues));
		}
	}
	
	/**
	 * @see com.twitterapime.parser.DefaultXMLHandler#text(java.lang.String)
	 */
	public void text(String text) throws ParserException {
		text = text.trim();
		//
		if (xmlPath.indexOf("/direct_message/sender/") != -1) {
			uaHandler.populate(senderValues, xmlPath, text);
		} else if (xmlPath.indexOf("/direct_message/recipient/") != -1) {
			uaHandler.populate(recipientValues, xmlPath, text);
		} else {
			tHandler.populate(dmValues, xmlPath, text);
		}
	}
	
	/**
	 * @see com.twitterapime.parser.DefaultXMLHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String namespaceURI, String localName, String qName)
		throws ParserException {
		super.endElement(namespaceURI, localName, qName);
		//
		if (localName.toLowerCase().equals("direct_message")) {
			fireTweetParsed((Tweet) dmList.lastElement());
		}
	}
	
	/**
	 * <p>
	 * Return the parsed tweets.
	 * </p>
	 * @return Array of tweets.
	 */
	public Tweet[] getParsedTweets() {
		Tweet[] ts = new Tweet[dmList.size()];
		dmList.copyInto(ts);
		//
		return ts;
	}
	
	/**
	 * <p>
	 * Load the parsed values, from a given index, into the given tweet.
	 * </p>
	 * @param dm Tweet to be loaded.
	 * @param index Tweet's index which values will be copied into given tweet.
	 */
	public void loadParsedTweet(Tweet dm, int index) {
		dm.setData((Tweet)dmList.elementAt(index));
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