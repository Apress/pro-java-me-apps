/*
 * Credential.java
 * 11/11/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest;

import java.util.Hashtable;

import com.twitterapime.model.DefaultEntity;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.xauth.Token;

/**
 * <p>
 * This class defines an entity that represents a user's credential.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.2
 * @since 1.1
 * @see UserAccountManager
 */
public final class Credential extends DefaultEntity {
	/**
	 * <p>
	 * Create an instance of Credential class.
	 * </p>
	 * @param username Username.
	 * @param password Password.
	 * @throws IllegalArgumentException If username/password is empty or null.
	 * @deprecated
	 */
	public Credential(String username, String password) {
		this(username, password, (String)null, (String)null);
	}
	
	/**
	 * <p>
	 * Create an instance of Credential class.
	 * </p>
	 * @param username Username.
	 * @param password Password.
	 * @param consumerKey Consumer key.
	 * @param consumerSecret Consumer secret.
	 * @throws IllegalArgumentException If username/password is empty or null.
	 */
	public Credential(String username, String password, String consumerKey,
		String consumerSecret) {
		if (username == null || (username = username.trim()).length() == 0) {
			throw new IllegalArgumentException(
				"Username must not be empty/null");
		}
		if (password == null || (password = password.trim()).length() == 0) {
			throw new IllegalArgumentException(
				"Password must not be empty/null");
		}
		//
		Hashtable credtls = new Hashtable(4);
		credtls.put(MetadataSet.CREDENTIAL_USERNAME, username);
		credtls.put(MetadataSet.CREDENTIAL_PASSWORD, password);
		//
		if (consumerKey != null
				&& (consumerKey = consumerKey.trim()).length() > 0) {
			credtls.put(MetadataSet.CREDENTIAL_CONSUMER_KEY, consumerKey);
		}
		if (consumerSecret != null
				&& (consumerSecret = consumerSecret.trim()).length() > 0) {
			credtls.put(MetadataSet.CREDENTIAL_CONSUMER_SECRET, consumerSecret);
		}
		//
		setData(credtls);
	}
	
	/**
	 * <p>
	 * Create an instance of Credential class.
	 * </p>
	 * @param username Username.
	 * @param consumerKey Consumer key.
	 * @param consumerSecret Consumer secret.
	 * @param accessToken OAuth access token.
	 * @throws IllegalArgumentException If username/accessToken is null.
	 */
	public Credential(String username, String consumerKey,
		String consumerSecret, Token accessToken) {
		this(username, "ignored", consumerKey, consumerSecret);
		//
		if (accessToken == null) {
			throw new IllegalArgumentException("accessToken must not be null.");
		}
		//
		data.put(MetadataSet.CREDENTIAL_ACCESS_TOKEN, accessToken);
	}
	
	/**
	 * <p>
	 * Get the credential in HttpAuth format (e.g. username:password).
	 * </p>
	 * @return Credential.
	 */
	String getBasicHttpAuthCredential() {
		return getString(MetadataSet.CREDENTIAL_USERNAME) +
		       ':' +
		       getString(MetadataSet.CREDENTIAL_PASSWORD);
	}
	
	/**
	 * <p>
	 * Verify whether XAuth credentials were entered.
	 * </p>
	 * @return true credentials entered.
	 */
	boolean hasXAuthCredentials() {
		String key = getString(MetadataSet.CREDENTIAL_CONSUMER_KEY);
		if (key == null || key.trim().length() == 0) {
			return false;
		}
		//
		key = getString(MetadataSet.CREDENTIAL_CONSUMER_SECRET);
		if (key == null || key.trim().length() == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * <p>
	 * Get entered OAuth access token.
	 * <p>
	 * @return Token.
	 */
	Token getAccessToken() {
		return (Token)getObject(MetadataSet.CREDENTIAL_ACCESS_TOKEN);
	}
}