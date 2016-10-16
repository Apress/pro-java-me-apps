/*
 * Tweet.java
 * 03/09/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.search;

import java.util.Hashtable;

import com.twitterapime.model.DefaultEntity;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.rest.GeoLocation;
import com.twitterapime.rest.TweetER;
import com.twitterapime.rest.UserAccount;

/**
 * <p>
 * This class defines an entity that represents a Tweet. A tweet is a message
 * posted by an user to Twitter.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.3
 * @since 1.0
 * @see SearchDevice
 * @see TweetER
 */
public final class Tweet extends DefaultEntity {
	/**
	 * <p>
	 * Content max number of characters.
	 * </p>
	 */
	public static final int MAX_CHARACTERS = 140;

	/**
	 * <p>
	 * Create an instance of Tweet class.
	 * </p>
	 */
	public Tweet() {
	}
	
	/**
	 * <p>
	 * Create an instance of Tweet class.
	 * </p>
	 * @param data The initial attributes/values.
	 */
	public Tweet(Hashtable data) {
		super(data);
	}
	
	/**
	 * <p>
	 * Create an instance of Tweet class.
	 * </p>
	 * @param content Content (status).
	 * @throws IllegalArgumentException If content is invalid.
	 */
	public Tweet(String content) {
		if (content == null) {
			throw new IllegalArgumentException("Content must not be null");
		}
		//
		Hashtable data = new Hashtable();
		data.put(MetadataSet.TWEET_CONTENT, content);
		setData(data);
		//
		validateContent();
	}
	
	/**
	 * <p>
	 * Create an instance of Tweet class.
	 * </p>
	 * <p>
	 * To set this tweet's location, enable Tweet Location settings of current
	 * authenticated user account on Twitter website. 
	 * </p>
	 * @param content Content (status).
	 * @param location Tweet's location.
	 * @throws IllegalArgumentException If content is invalid.
	 */
	public Tweet(String content, GeoLocation location) {
		this(content);
		//
		if (location != null) {
			data.put(MetadataSet.TWEET_LOCATION, location);
		}
	}

	/**
	 * <p>
	 * Create an instance of Tweet class.<br/>
	 * This constructor is used when the Tweet object is going to be sent as a
	 * Direct Message.
	 * </p>
	 * @param toUserNameOrID Username or ID from recipient user.
	 * @param content Content (message).
	 * @throws IllegalArgumentException If toUserNameOrID and/or content are
	 *         invalid.
	 */
	public Tweet(String toUserNameOrID, String content) {
		if (toUserNameOrID == null) {
			throw new IllegalArgumentException(
				"To username/ID must not be empty/null.");
		}
		if (content == null) {
			throw new IllegalArgumentException("Content must not be null");
		}
		//
		Hashtable tweetData = new Hashtable();
		tweetData.put(MetadataSet.TWEET_AUTHOR_USERNAME, toUserNameOrID);
		tweetData.put(MetadataSet.TWEET_CONTENT, content);
		Hashtable usrData = new Hashtable();
		usrData.put(MetadataSet.USERACCOUNT_USER_NAME, toUserNameOrID);
		usrData.put(MetadataSet.USERACCOUNT_ID, toUserNameOrID);
		tweetData.put(MetadataSet.TWEET_USER_ACCOUNT, new UserAccount(usrData));
		setData(tweetData);
		//
		validateRecipient();
		validateContent();
	}
	
	/**
	 * <p>
	 * Validate tweet's content.
	 * </p>
	 * @throws IllegalArgumentException If the content is null/empty or exceeds
	 *         140 characters.
	 */
	public void validateContent() {
		String text = getString(MetadataSet.TWEET_CONTENT);
		if (text == null || (text = text.trim()).length() == 0) {
			throw new IllegalArgumentException(
				"Content must not be empty/null.");
		} else if (text.length() > 140) {
			throw new IllegalArgumentException(
			   "Content must not be longer than"+MAX_CHARACTERS+" characters.");
		}
	}
	
	/**
	 * <p>
	 * Validate Direct Messsage's recipient info.
	 * </p>
	 * @throws IllegalArgumentException If the recipient info is null/empty.
	 */
	public void validateRecipient() {
		String r = getString(MetadataSet.TWEET_AUTHOR_USERNAME);
		//
		if (r == null || (r = r.trim()).length() == 0) {
			UserAccount ua = getUserAccount();
			if (ua != null) {
				r = ua.getString(MetadataSet.USERACCOUNT_USER_NAME);
				if (r == null || (r = r.trim()).length() == 0) {
					r = ua.getString(MetadataSet.USERACCOUNT_ID);
					if (r == null || (r = r.trim()).length() == 0) {
						throw new IllegalArgumentException(
							"Recipient's username/ID must not be empty/null.");
					}
				}
			} else {
				throw new IllegalArgumentException(
					"Recipient's username/ID must not be empty/null.");
			}
		}
	}

	/**
	 * <p>
	 * Get the user account.
	 * </p>
	 * @return User account.
	 */
	public UserAccount getUserAccount() {
		return (UserAccount)data.get(MetadataSet.TWEET_USER_ACCOUNT);
	}
	
	/**
	 * <p>
	 * Get the recipient account.<br/>Call this method to retrieve info about
	 * the recipient user by sending a Direct Message.
	 * </p>
	 * @return Recipient account.
	 */
	public UserAccount getRecipientAccount() {
		return (UserAccount)data.get(MetadataSet.TWEET_RECIPIENT_ACCOUNT);
	}
	
	/**
	 * <p>
	 * Get the reposted tweet object.
	 * </p>
	 * @return Tweet object.
	 */
	public Tweet getRepostedTweet() {
		return (Tweet)data.get(MetadataSet.TWEET_REPOSTED_TWEET);
	}
	
	/**
	 * <p>
	 * Get the tweet's location object.
	 * </p>
	 * @return Location object.
	 */
	public GeoLocation getLocation() {
		return (GeoLocation)data.get(MetadataSet.TWEET_LOCATION);
	}
}