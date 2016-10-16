/*
 * AccountHandler.java
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
import com.twitterapime.rest.UserAccount;
import com.twitterapime.search.Tweet;

/**
 * <p>
 * Handler class for parsing the account's XML results from Twitter API. 
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.1
 * @since 1.2
 */
public final class AccountHandler extends DefaultXMLHandler {
	/**
	 * <p>
	 * Tweet xml handler object.
	 * </p>
	 */
	private TweetHandler tHandler = new TweetHandler();
	
	/**
	 * <p>
	 * User Account XML handler object.
	 * </p>
	 */
	private UserAccountHandler uaHandler = new UserAccountHandler();
	
	/**
	 * <p>
	 * Hash with user account values.
	 * </p>
	 */
	private Hashtable userAccountValues = new Hashtable(25);

	/**
	 * <p>
	 * Hash with last tweet values.
	 * </p>
	 */
	private Hashtable lastTweetValues = new Hashtable(10);

	/**
	 * @see com.twitterapime.parser.DefaultXMLHandler#text(java.lang.String)
	 */
	public void text(String text) throws ParserException {
		text = text.trim();
		//
		if (xmlPath.startsWith("/user/status/")) {
			tHandler.populate(lastTweetValues, xmlPath, text);
		} else if (xmlPath.startsWith("/user/")) {
			uaHandler.populate(userAccountValues, xmlPath, text);
		}
	}
	
	/**
	 * @see com.twitterapime.parser.DefaultXMLHandler#endDocument()
	 */
	public void endDocument() throws ParserException {
		userAccountValues.put(
			MetadataSet.USERACCOUNT_LAST_TWEET, new Tweet(lastTweetValues));
	}
	
	/**
	 * <p>
	 * Return the parsed user account.
	 * </p>
	 * @return User account.
	 */
	public UserAccount getParsedUserAccount() {
		return new UserAccount(userAccountValues);
	}
	
	/**
	 * <p>
	 * Load the parsed values into the given UserAccount.
	 * </p>
	 * @param user UserAccount to be loaded.
	 */
	public void loadParsedUserAccount(UserAccount user) {
		user.setData(userAccountValues);
	}
}
