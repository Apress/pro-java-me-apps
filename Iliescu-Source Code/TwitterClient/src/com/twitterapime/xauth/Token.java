/*
 * Token.java
 * 05/06/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.xauth;

import com.twitterapime.util.StringUtil;

/**
 * <p>
 * This class implements an OAuth token.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.3
 */
public final class Token {
	/**
	 * <p>
	 * Access token.
	 * </p>
	 */
	private String token;
	
	/**
	 * <p>
	 * Secret token.
	 * </p>
	 */
	private String secret;

	/**
	 * <p>
	 * Parse an access token string and creates a Token object.
	 * </p>
	 * @param accessTokenString Token string.
	 */
	public static Token parse(String accessTokenString) {
		String[] tokens = StringUtil.split(accessTokenString, '&');
		String access = StringUtil.split(tokens[0], '=')[1];
		String secret = StringUtil.split(tokens[1], '=')[1];
		//
 		return new Token(access, secret);
	}
	
	/**
	 * <p>
	 * Create an instance of Token class.
	 * </p>
	 * @param token Access token.
	 * @param secret Secret token.
	 * @throws IllegalArgumentException If token/secret is empty or null.
	 */
	public Token(String token, String secret) {
		if (token == null || (token = token.trim()).length() == 0) {
			throw new IllegalArgumentException("Token must not be empty/null");
		}
		if (secret == null || (secret = secret.trim()).length() == 0) {
			throw new IllegalArgumentException("Secret must not be empty/null");
		}
		//
		this.token = token;
		this.secret = secret;
	}

	/**
	 * <p>
	 * Get the access token.
	 * </p>
	 * @return Token.
	 */
	public String getToken() {
		return token;
	}

	/**
	 * <p>
	 * Get the secret token.
	 * </p>
	 * @return Token.
	 */
	public String getSecret() {
		return secret;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "oauth_token: " + token + " oauth_token_secret: " + secret;
	}
}