/*
 * XAuthConstants.java
 * 05/06/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.xauth;

/**
 * <p>
 * This class defines some constants used by xAuth implementation.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.3
 */
final class XAuthConstants {
	/**
	 * <p>
	 * TIMESTAMP.
	 * </p>
	 */
	public static final String TIMESTAMP = "oauth_timestamp";
	
	/**
	 * <p>
	 * SIGN_METHOD.
	 * </p>
	 */
	public static final String SIGN_METHOD = "oauth_signature_method";

	/**
	 * <p>
	 * SIGNATURE.
	 * </p>
	 */
	public static final String SIGNATURE = "oauth_signature";

	/**
	 * <p>
	 * CONSUMER_KEY.
	 * </p>
	 */
	public static final String CONSUMER_KEY = "oauth_consumer_key";

	/**
	 * <p>
	 * VERSION.
	 * </p>
	 */
	public static final String VERSION = "oauth_version";

	/**
	 * <p>
	 * NONCE.
	 * </p>
	 */
	public static final String NONCE = "oauth_nonce";

	/**
	 * <p>
	 * PARAM_PREFIX.
	 * </p>
	 */
	public static final String PARAM_PREFIX = "oauth_";

	/**
	 * <p>
	 * TOKEN.
	 * </p>
	 */
	public static final String TOKEN = "oauth_token";

	/**
	 * <p>
	 * EMPTY_TOKEN_SECRET.
	 * </p>
	 */
	public static final String EMPTY_TOKEN_SECRET = "";

	/**
	 * <p>
	 * HEADER.
	 * </p>
	 */
	public static final String HEADER = "Authorization";

	/**
	 * <p>
	 * MODE.
	 * </p>
	 */
	public static final String MODE = "x_auth_mode";

	/**
	 * <p>
	 * USERNAME.
	 * </p>
	 */
	public static final String USERNAME = "x_auth_username";

	/**
	 * <p>
	 * PASSWORD.
	 * </p>
	 */
	public static final String PASSWORD = "x_auth_password";
	
	/**
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 */
	private XAuthConstants() {
	}
}