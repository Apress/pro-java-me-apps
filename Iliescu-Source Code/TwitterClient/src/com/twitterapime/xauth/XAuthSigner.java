/*
 * XAuthSigner.java
 * 05/06/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.xauth;

import java.util.Enumeration;
import java.util.Hashtable;

import com.twitterapime.io.HttpRequest;
import com.twitterapime.util.StringUtil;
import com.twitterapime.xauth.encoders.Base64Encoder;
import com.twitterapime.xauth.encoders.HMAC;

/**
 * <p>
 * This class implements a xAuth signer.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.3
 */
public final class XAuthSigner {
	/**
	 * <p>
	 * Consumer key.
	 * </p>
	 */
	private final String consumerKey;

	/**
	 * <p>
	 * Consumer secret.
	 * </p>
	 */
	private final String consumerSecret;

	/**
	 * <p>
	 * Generate a signature from the given base string.
	 * </p>
	 * @param baseString Base string.
	 * @param consumerSecret Consumer secret.
	 * @param tokenSecret Token secret.
	 * @return Signature.
	 */
	private static String getSignature(String baseString, String consumerSecret,
		String tokenSecret) {
		byte[] b = HMAC.getHmac(baseString, consumerSecret + '&' + tokenSecret);
		//
		return Base64Encoder.encode(b);
	}

	/**
	 * <p>
	 * Create an instance of XAuthSigner class.
	 * </p>
	 * @param consumerKey Consumer key.
	 * @param consumerSecret Consumer secret.
	 * @throws IllegalArgumentException If consumer key/secret is empty or null.
	 */
	public XAuthSigner(String consumerKey, String consumerSecret) {
		if (consumerKey == null
				|| (consumerKey = consumerKey.trim()).length() == 0) {
			throw new IllegalArgumentException(
				"Consumer key must not be empty/null");
		}
		if (consumerSecret == null
				|| (consumerSecret = consumerSecret.trim()).length() == 0) {
			throw new IllegalArgumentException(
				"Consumer secret must not be empty/null");
		}
		//
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
	}

	/**
	 * <p>
	 * Sign the given request to obtain the access token.
	 * </p>
	 * @param req Http request.
	 * @param username User name.
	 * @param password Password.
	 */
	public void signForAccessToken(HttpRequest req, String username,
		String password) {
		req.setBodyParameter(XAuthConstants.MODE, "client_auth");
		req.setBodyParameter(XAuthConstants.USERNAME, username);
		req.setBodyParameter(XAuthConstants.PASSWORD, password);
		//
		OAuthParameters params = new OAuthParameters(consumerKey);
		//
		String str = getSignatureBaseString(req, params);
		//
		str = getSignature(
			str, consumerSecret, XAuthConstants.EMPTY_TOKEN_SECRET);
		params.put(XAuthConstants.SIGNATURE, str);
		//
		str = params.getAuthorizationHeaderValue();
		req.setHeaderField(XAuthConstants.HEADER, str);
	}

	/**
	 * <p>
	 * Sign the given request.
	 * </p>
	 * @param req Http request.
	 * @param access Access token.
	 */
	public void sign(HttpRequest req, Token access) {
		OAuthParameters params = new OAuthParameters(consumerKey);
		params.put(XAuthConstants.TOKEN, access.getToken());
		//
		String str = getSignatureBaseString(req, params);
		//
		str = getSignature(str, consumerSecret, access.getSecret());
		params.put(XAuthConstants.SIGNATURE, str);
		//
		str = params.getAuthorizationHeaderValue();
		req.setHeaderField(XAuthConstants.HEADER, str);
	}

	/**
	 * <p>
	 * Get the signature base string from the given request and parameters.
	 * </p>
	 * @param req Http request.
	 * @param params OAuth parameters.
	 * @return Signature base string.
	 */
	private String getSignatureBaseString(HttpRequest req,
		OAuthParameters params) {
		String method = StringUtil.encode(req.getMethod(), "UTF-8");
		String url = StringUtil.encode(req.getSanitizedURL(), "UTF-8");
		//
		addParams(params, req.getQueryStringParams());
		addParams(params, req.getBodyParameters());
		//
		String sortedParams =
			StringUtil.encode(
				params.getSortedEncodedParamsAsString(), "UTF-8");
		//
		return method + '&' + url + '&' + sortedParams;
	}
	
	/**
	 * <p>
	 * Add the given Hashtable parameters into OAuth parameters.
	 * </p>
	 * @param params OAuth parameters.
	 * @param p Hashtable parameters.
	 */
	private void addParams(OAuthParameters params, Hashtable p) {
		String key;
		Enumeration keys = p.keys();
		//
		while (keys.hasMoreElements()) {
			key = (String)keys.nextElement();
			params.put(key, (String)p.get(key));
		}
	}
}