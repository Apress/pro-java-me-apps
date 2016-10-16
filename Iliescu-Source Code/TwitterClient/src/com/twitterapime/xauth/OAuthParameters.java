/*
 * OAuthParameters.java
 * 05/06/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.xauth;

import java.util.Enumeration;
import java.util.Hashtable;

import com.twitterapime.util.QSort;
import com.twitterapime.util.StringUtil;

/**
 * <p>
 * This class implements an OAuth parameter.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.3
 */
final class OAuthParameters {
	/**
	 * <p>
	 * Parameters.
	 * </p>
	 */
	private Hashtable params;

	/**
	 * <p>
	 * Create an instance of OAuthParameters class.
	 * </p>
	 * @param consumerSecret Consumer secret.
	 * @throws IllegalArgumentException If consumer key/secret is empty or null.
	 */
	public OAuthParameters(String consumerKey) {
		if (consumerKey == null
				|| (consumerKey = consumerKey.trim()).length() == 0) {
			throw new IllegalArgumentException(
				"Consumer key must not be empty/null");
		}
		//
		params = new Hashtable();
		params.put(XAuthConstants.TIMESTAMP, getTimestampInSeconds());
		params.put(XAuthConstants.SIGN_METHOD, "HMAC-SHA1");
		params.put(XAuthConstants.VERSION, "1.0");
		params.put(XAuthConstants.NONCE, getTimestampInSeconds());
		params.put(XAuthConstants.CONSUMER_KEY, consumerKey);
	}

	/**
	 * <p>
	 * Get a string with all the parameters sorted.
	 * </p>
	 * @return Sorted string.
	 */
	public String getSortedEncodedParamsAsString() {
		StringBuffer buffer = new StringBuffer();
		String[] sKeys = sortedKeys();
		//
		for (int i = 0; i < sKeys.length; i++) {
			buffer.append(StringUtil.encode(sKeys[i], "UTF-8"));
			buffer.append('=');
			buffer.append(
				StringUtil.encode((String)params.get(sKeys[i]), "UTF-8"));
			//
			if (i +1 < sKeys.length) {
				buffer.append('&');
			}
		}
		//
		return buffer.toString();
	}

	/**
	 * <p>
	 * Get the Authorization header value.
	 * </p>
	 * @return Value.
	 */
	public String getAuthorizationHeaderValue() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("OAuth ");
		//
		String[] sKeys = sortedKeys();
		for (int i = 0; i < sKeys.length; i++) {
			if (sKeys[i].startsWith(XAuthConstants.PARAM_PREFIX)) {
				buffer.append(sKeys[i]);
				buffer.append('=');
				buffer.append('"');
				buffer.append(
					StringUtil.encode((String)params.get(sKeys[i]), "UTF-8"));
				buffer.append("\", ");
			}
		}
		//
		return buffer.toString().substring(0, buffer.length() - 2);
	}

	/**
	 * <p>
	 * Put a given parameter.
	 * </p>
	 * @param key Parameter key.
	 * @param value Parameter value.
	 */
	public void put(String key, String value) {
		params.put(key, value);
	}

	/**
	 * <p>
	 * Get an array with all parameter keys sorted.
	 * </p>
	 * @return Sorted keys.
	 */
	private String[] sortedKeys() {
		int i = 0;
		String[] sKeys = new String[params.size()];
		Enumeration keys = params.keys();
		//
		while (keys.hasMoreElements()) {
			sKeys[i++] = (String)keys.nextElement();
		}
		//
		QSort qsort = new QSort();
		qsort.quicksort(sKeys, 0, sKeys.length -1);
		//
		return sKeys;
	}

	/**
	 * <p>
	 * Get the current time in seconds.
	 * </p>
	 * @return Timestamp.
	 */
	private String getTimestampInSeconds() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}
}