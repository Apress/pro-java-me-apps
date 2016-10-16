/*
 * UserAccount.java
 * 11/11/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest;

import java.util.Hashtable;

import com.twitterapime.model.DefaultEntity;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.search.Tweet;
import com.twitterapime.util.StringUtil;

/**
 * <p>
 * This class defines an entity that represents a user's account.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.2
 * @since 1.1
 * @see UserAccountManager
 */
public final class UserAccount extends DefaultEntity {
	/**
	 * <p>
	 * Create an instance of UserAccount class.
	 * </p>
	 */
	public UserAccount() {
	}

	/**
	 * <p>
	 * Create an instance of UserAccount class.
	 * </p>
	 * @param data The initial attributes/values.
	 */
	public UserAccount(Hashtable data) {
		super(data);
	}
	
	/**
	 * <p>
	 * Create an instance of UserAccount class.<br/>
	 * This constructor is used when the UserAccount object is going to be
	 * used for Follow or Unfollow operations on {@link UserAccountManager}.
	 * </p>
	 * @param userNameOrID Username or ID.
	 * @throws IllegalArgumentException If userNameOrID is empty/null.
	 */
	public UserAccount(String userNameOrID) {
		if (StringUtil.isEmpty(userNameOrID)) {
			throw new IllegalArgumentException(
				"Username/ID must not be empty/null.");
		}
		//
		Hashtable data = new Hashtable();
		try {
			Long.parseLong(userNameOrID);
			data.put(MetadataSet.USERACCOUNT_ID, userNameOrID);
		} catch (Exception e) {
			//it is not supposed to be an ID, since it is not a number.
		}
		data.put(MetadataSet.USERACCOUNT_USER_NAME, userNameOrID);
		setData(data);
	}
	
	/**
	 * <p>
	 * Get the last tweet posted by the user.
	 * </p>
	 * @return Tweet.
	 */
	public Tweet getLastTweet() {
		return (Tweet)getObject(MetadataSet.USERACCOUNT_LAST_TWEET);
	}
	
	/**
	 * <p>
	 * Get username/ID.
	 * </p>
	 * @return Username/ID.
	 */
	String getUserNameOrID() {
		String id = getString(MetadataSet.USERACCOUNT_ID);
		if (StringUtil.isEmpty(id)) {
			id = getString(MetadataSet.USERACCOUNT_USER_NAME);
		}
		//
		return id;
	}
	
	/**
	 * <p>
	 * Get username/ID and consecutive param.
	 * </p>
	 * @return Username/ID and param.
	 */
	String[] getUserNameOrIDParamValue() {
		String[] paramValue = new String[2];
		//
		String id = getString(MetadataSet.USERACCOUNT_ID);
		if (!StringUtil.isEmpty(id)) {
			try {
				Long.parseLong(id); //test whether it is a number.
				//
				paramValue[0] = "user_id";
				paramValue[1] = id;
				//
				return paramValue;
			} catch (Exception e) {
				//it is not supposed to be an ID, since it is not a number.
			}
		}
		//
		String us = getString(MetadataSet.USERACCOUNT_USER_NAME);
		if (!StringUtil.isEmpty(us)) {
			paramValue[0] = "screen_name";
			paramValue[1] = us;
		}
		//
		return paramValue;
	}
	
	/**
	 * <p>
	 * Verify if username or ID is informed.
	 * </p>
	 * @throws IllegalArgumentException If username and ID are not informed.
	 */
	void validateUserNameOrID() {
		if (StringUtil.isEmpty(getUserNameOrID())) {
			throw new IllegalArgumentException(
				"Username/ID must no be empty/null");
		}
	}
}