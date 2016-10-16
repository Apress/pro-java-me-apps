/*
 * UserAccountManager.java
 * 11/11/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

import com.twitterapime.io.HttpConnection;
import com.twitterapime.io.HttpRequest;
import com.twitterapime.io.HttpResponse;
import com.twitterapime.io.HttpResponseCodeInterpreter;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.Parser;
import com.twitterapime.parser.ParserException;
import com.twitterapime.parser.ParserFactory;
import com.twitterapime.rest.handler.AccountHandler;
import com.twitterapime.rest.handler.RateLimitStatusHandler;
import com.twitterapime.search.LimitExceededException;
import com.twitterapime.search.Query;
import com.twitterapime.util.StringUtil;
import com.twitterapime.xauth.Token;
import com.twitterapime.xauth.XAuthSigner;
import com.twitterapime.xauth.encoders.Base64Encoder;

/**
 * <p>
 * This class is responsible for managing the user account.
 * </p>
 * <p>
 * <pre>
 * Credential c = new Credential(...);
 * UserAccountManager uam = UserAccountManager.getInstance(c)
 * if (uam.verifyCredential()) {
 *   System.out.println("User logged in...");
 * }
 * uam.signOut();
 * </pre>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.2
 * @since 1.1
 */
public final class UserAccountManager {
	/**
	 * <p>
	 * UserAccountManager pool used to cache instanced associated to user
	 * credentials.
	 * </p>
	 */
	private static Hashtable userAccountMngrPoll;
	
	/**
	 * <p>
	 * Hold all Twitter API URL services.
	 * </p>
	 */
	private static final Hashtable SERVICES_URL;

	/**
	 * <p>
	 * Key for Twitter API URL service account verify credentials.
	 * </p>
	 * @see UserAccountManager#setServiceURL(String, String)
	 * @see UserAccountManager#verifyCredential()
	 */
	public static final String TWITTER_API_URL_SERVICE_ACCOUNT_VERIFY_CREDENTIALS =
		"TWITTER_API_URL_SERVICE_ACCOUNT_VERIFY_CREDENTIALS";

	/**
	 * <p>
	 * Key for Twitter API URL service OAuth access token.
	 * </p>
	 * @see UserAccountManager#setServiceURL(String, String)
	 * @see UserAccountManager#verifyCredential()
	 */
	public static final String TWITTER_API_URL_SERVICE_OAUTH_ACCESS_TOKEN =
		"TWITTER_API_URL_SERVICE_OAUTH_ACCESS_TOKEN";
	
	/**
	 * <p>
	 * Key for Twitter API URL service account rate limit status.
	 * </p>
	 * @see UserAccountManager#setServiceURL(String, String)
	 * @see UserAccountManager#getRateLimitStatus()
	 */
	public static final String TWITTER_API_URL_SERVICE_ACCOUNT_RATE_LIMIT_STATUS =
		"TWITTER_API_URL_SERVICE_ACCOUNT_RATE_LIMIT_STATUS";
	
	/**
	 * <p>
	 * Key for Twitter API URL service users show.
	 * </p>
	 * @see UserAccountManager#setServiceURL(String, String)
	 * @see UserAccountManager#getUserAccount()
	 */
	public static final String TWITTER_API_URL_SERVICE_USERS_SHOW =
		"TWITTER_API_URL_SERVICE_USERS_SHOW";
	
	/**
	 * <p>
	 * Key for Twitter API URL service account update profile.
	 * </p>
	 * @see UserAccountManager#setServiceURL(String, String)
	 * @see UserAccountManager#getUserAccount()
	 */
	public static final String TWITTER_API_URL_SERVICE_ACCOUNT_UPDATE_PROFILE =
		"TWITTER_API_URL_SERVICE_ACCOUNT_UPDATE_PROFILE";

	static {
		SERVICES_URL = new Hashtable(10);
		//
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_ACCOUNT_VERIFY_CREDENTIALS,
			"http://api.twitter.com/1/account/verify_credentials.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_OAUTH_ACCESS_TOKEN,
			"http://api.twitter.com/oauth/access_token");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_ACCOUNT_RATE_LIMIT_STATUS,
			"http://api.twitter.com/1/account/rate_limit_status.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_USERS_SHOW,
			"http://api.twitter.com/1/users/show.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_ACCOUNT_UPDATE_PROFILE,
			"http://api.twitter.com/1/account/update_profile.xml");
	}

	/**
	 * <p>
	 * User's credentials.
	 * </p>
	 */
	private Credential credential;
	
	/**
	 * <p>
	 * Flag that indicates whether the user's credentials were verified.
	 * </p>
	 */
	private boolean verified;
	
	/**
	 * <p>
	 * Marks the instance as invalidated.
	 * </p>
	 */
	private boolean invalidated;
	
	/**
	 * <p>
	 * Access token.
	 * </p>
	 */
	private Token token;
	
	/**
	 * <p>
	 * XAuth signer instance.
	 * </p>
	 */
	private XAuthSigner signer;
	
	/**
	 * <p>
	 * Get an URL related to the given service key.
	 * </p>
	 * @param serviceKey Service key.
	 * @return URL.
	 */
	private String getURL(String serviceKey) {
		return (String)SERVICES_URL.get(serviceKey);
	}
	
	/**
	 * <p>
	 * Set a new URL to a given Twitter API service. This method is very useful
	 * in case Twitter API decides to change a service's URL. So there is no
	 * need to wait for a new version of this API to get it working back.
	 * </p>
	 * <p>
	 * <b>Be careful about using this method, since it can cause unexpected
	 * results, case you enter an invalid URL.</b>
	 * </p>
	 * @param serviceKey Service key.
	 * @param url New URL.
	 * @see UserAccountManager#TWITTER_API_URL_SERVICE_ACCOUNT_RATE_LIMIT_STATUS
	 * @see UserAccountManager#TWITTER_API_URL_SERVICE_ACCOUNT_VERIFY_CREDENTIALS
	 * @see UserAccountManager#TWITTER_API_URL_SERVICE_OAUTH_ACCESS_TOKEN
	 * @see UserAccountManager#TWITTER_API_URL_SERVICE_USERS_SHOW
	 */
	public void setServiceURL(String serviceKey, String url) {
		SERVICES_URL.put(serviceKey, url);
	}
	
	/**
	 * <p>
	 * Get an instance of UserAccountManager class and associate it to a given
	 * user credential.
	 * </p>
	 * @param c Credentials.
	 * @return AccountManager object.
	 * @throws IllegalArgumentException If credential is null.
	 */
	public synchronized static UserAccountManager getInstance(Credential c) {
		if (c == null) {
			throw new IllegalArgumentException("Credential must not be null.");
		}
		//
		UserAccountManager uam = null;
		//
		if (userAccountMngrPoll == null) {
			userAccountMngrPoll = new Hashtable();
		} else {
			synchronized (userAccountMngrPoll) {
				uam = (UserAccountManager)userAccountMngrPoll.get(c);
			}
		}
		//
		return uam != null ? uam : new UserAccountManager(c);
	}
	
	/**
	 * <p>
	 * Create an instance of UserAccountManager class.
	 * </p>
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 * @param c User's credential.
	 */
	private UserAccountManager(Credential c) {
		credential = c;
		//
		if (c.hasXAuthCredentials()) {
			String conKey = c.getString(MetadataSet.CREDENTIAL_CONSUMER_KEY);
			String conSec = c.getString(MetadataSet.CREDENTIAL_CONSUMER_SECRET);
			//
			signer = new XAuthSigner(conKey, conSec);
		}
	}

	/**
	 * <p>
	 * Returns a set of info about the number of API requests available to the
	 * requesting user before the REST API limit is reached for the current
	 * hour.
	 * </p>
	 * <p>
	 * Stay aware of these limits, since it can impact the usage of some methods
	 * of this API.
	 * </p>
	 * @return Rate limiting status info.
	 * @throws IOException If an I/O error occurs.
	 * @throws SecurityException If it is not properly logged in.
	 * @throws LimitExceededException If limit has been hit.
	 */
	public RateLimitStatus getRateLimitStatus() throws IOException,
		LimitExceededException {
		checkValid();
		checkVerified();
		//
		HttpRequest req = createRequest(
			getURL(TWITTER_API_URL_SERVICE_ACCOUNT_RATE_LIMIT_STATUS));
		//
		try {
			HttpResponse resp = req.send();
			//
			HttpResponseCodeInterpreter.perform(resp);
			//
			Parser parser = ParserFactory.getDefaultParser();
			RateLimitStatusHandler handler = new RateLimitStatusHandler();
			parser.parse(resp.getStream(), handler);
			//
			return handler.getParsedRateLimitStatus();
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		} finally {
			req.close();
		}
	}

	/**
	 * <p>
	 * Return whether it is properly verified.
	 * </p>
	 * @return Verified (true).
	 */
	public boolean isVerified() {
		checkValid();
		//
		return verified;
	}
	
	/**
	 * <p>
	 * Verify whether the given user's credential are valid. This method
	 * authenticates the API to Twitter REST API.
	 * </p>
	 * @return Valid credentials (true).
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 */
	public synchronized boolean verifyCredential() throws IOException,
		LimitExceededException {
		checkValid();
		//
		if (verified) {
			return true; //already verified.
		}
		//
		HttpRequest req;
		if (credential.hasXAuthCredentials()) {
			token = credential.getAccessToken();
			//
			if (token != null) {
				verified = true;
				//
				return true;
			}
			//
			String user = credential.getString(MetadataSet.CREDENTIAL_USERNAME);
			String pass = credential.getString(MetadataSet.CREDENTIAL_PASSWORD);
			//
			req = createRequest(
				getURL(TWITTER_API_URL_SERVICE_OAUTH_ACCESS_TOKEN));
			req.setMethod(HttpConnection.POST);
			//
			signer.signForAccessToken(req, user, pass);
		} else {
			req = createRequest(
				getURL(TWITTER_API_URL_SERVICE_ACCOUNT_VERIFY_CREDENTIALS));
		}
		//
		try {
			HttpResponse resp = req.send();
			//
			if (resp.getCode() == HttpConnection.HTTP_OK) {
				if (credential.hasXAuthCredentials()) {
					token = Token.parse(resp.getBodyContent()); //access token.
				}
				verified = true;
				//
				saveSelfOnPool();
			} else if (resp.getCode() == HttpConnection.HTTP_UNAUTHORIZED) {
				verified = false;
			} else {
				HttpResponseCodeInterpreter.perform(resp);
			}
		} finally {
			req.close();
		}
		//
		return verified;
	}
	
	/**
	 * <p>
	 * Ends the session of the authenticating user.
	 * </p>
	 * <p>
	 * Once signed out, this instance is no longer valid for use as well as
	 * another one dependent of it. Dump them!
	 * </p>
	 * @throws IOException If an I/O error occurs.
	 * @throws SecurityException If it is not properly logged in.
	 * @throws LimitExceededException If limit has been hit.
	 */
	public synchronized void signOut() throws IOException,
		LimitExceededException {
		checkValid();
		//
		if (verified) {
			verified = false;
			token = null;
			signer = null;
			userAccountMngrPoll.remove(credential);
			Timeline.cleanPool();
			TweetER.cleanPool();
			FriendshipManager.cleanPool();
			//
			invalidated = true;
		}
	}
	
	/**
	 * <p>
	 * Get the logged user's account.
	 * </p>
	 * @return User account object.
	 * @throws SecurityException If it is not properly logged in.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 */
	public UserAccount getUserAccount() throws IOException,
		LimitExceededException {
		return getUserAccount(
			new UserAccount(
				credential.getString(MetadataSet.CREDENTIAL_USERNAME)));
	}
	
	/**
	 * <p>
	 * Get the account of a given user.
	 * </p>
	 * @param user User.
	 * @return Username/id.
	 * @throws SecurityException If it is not properly logged in.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws IllegalArgumentException If user/id/username is null/empty.
	 */
	public UserAccount getUserAccount(UserAccount user)
		throws IOException, LimitExceededException {
		checkValid();
		checkVerified();
		//
		if (user == null) {
			throw new IllegalArgumentException("User must not be null.");
		}
		user.validateUserNameOrID();
		//
		String[] pv = user.getUserNameOrIDParamValue();
		String param = "?" + pv[0] + "=" + pv[1];
		//
		HttpRequest req = createRequest(
			getURL(TWITTER_API_URL_SERVICE_USERS_SHOW) + param);
		//
		try {
			HttpResponse resp = req.send();
			//
			HttpResponseCodeInterpreter.perform(resp);
			//
			Parser parser = ParserFactory.getDefaultParser();
			AccountHandler handler = new AccountHandler();
			parser.parse(resp.getStream(), handler);
			//
			handler.loadParsedUserAccount(user);
			//
			return user;
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		} finally {
			req.close();
		}		
	}
	
	/**
	 * {@link FriendshipManager#follow(UserAccount)}
	 */
	public UserAccount follow(UserAccount ua) throws IOException,
		LimitExceededException {
		checkValid();
		//
		return FriendshipManager.getInstance(this).follow(ua);
	}
	
	/**
	 * {@link FriendshipManager#unfollow(UserAccount)}
	 */
	public UserAccount unfollow(UserAccount ua) throws IOException,
		LimitExceededException {
		checkValid();
		//
		return FriendshipManager.getInstance(this).unfollow(ua);
	}
	
	/**
	 * {@link FriendshipManager#isFollowing(UserAccount)}
	 */
	public boolean isFollowing(UserAccount ua) throws IOException,
		LimitExceededException {
		checkValid();
		//
		return FriendshipManager.getInstance(this).isFollowing(ua);
	}

	/**
	 * {@link FriendshipManager#block(UserAccount)}
	 */
	public UserAccount block(UserAccount ua) throws IOException,
		LimitExceededException {
		checkValid();
		//
		return FriendshipManager.getInstance(this).block(ua);
	}
	
	/**
	 * {@link FriendshipManager#unblock(UserAccount)}
	 */
	public UserAccount unblock(UserAccount ua) throws IOException,
		LimitExceededException {
		checkValid();
		//
		return FriendshipManager.getInstance(this).unblock(ua);
	}
	
	/**
	 * {@link FriendshipManager#isBlocking(UserAccount)}
	 */
	public boolean isBlocking(UserAccount ua) throws IOException,
		LimitExceededException {
		checkValid();
		//
		return FriendshipManager.getInstance(this).isBlocking(ua);
	}
	
	/**
	 * {@link FriendshipManager#getFriendsID(Query)}
	 */
	public String[] getFriendsID(Query query) throws IOException,
		LimitExceededException {
		checkValid();
		//
		return FriendshipManager.getInstance(this).getFriendsID(query);
	}
	
	/**
	 * {@link FriendshipManager#getFollowersID(Query)}
	 */
	public String[] getFollowersID(Query query) throws IOException,
		LimitExceededException {
		checkValid();
		//
		return FriendshipManager.getInstance(this).getFollowersID(query);
	}
	
	/**
	 * <p>
	 * Update account information of authenticated user. Only name, description,
	 * URL and location can be changed.
	 * </p>
	 * @param newUserInfo
	 * @return New user account info.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws SecurityException If it is not authenticated.
	 * @throws IllegalArgumentException If user info is null/empty.
	 * @see MetadataSet#USERACCOUNT_USER_NAME
	 * @see MetadataSet#USERACCOUNT_DESCRIPTION
	 * @see MetadataSet#USERACCOUNT_URL
	 * @see MetadataSet#USERACCOUNT_LOCATION
	 */
	public UserAccount updateProfile(UserAccount newUserInfo)
		throws IOException, LimitExceededException {
		checkValid();
		checkVerified();
		//
		if (newUserInfo == null) {
			throw new IllegalArgumentException(
				"New user info must not be null.");
		}
		//
		HttpRequest req =
			createRequest(
				getURL(TWITTER_API_URL_SERVICE_ACCOUNT_UPDATE_PROFILE));
		req.setMethod(HttpConnection.POST);
		//
		String info = newUserInfo.getString(MetadataSet.USERACCOUNT_NAME);
		if (!StringUtil.isEmpty(info)) {
			req.setBodyParameter("name", info);
		}
		info = newUserInfo.getString(MetadataSet.USERACCOUNT_DESCRIPTION);
		if (!StringUtil.isEmpty(info)) {
			final int MAX_LEN = 156;
			//
			if (info.trim().length() > MAX_LEN) {
				throw new IllegalArgumentException(
					"Description must not be longer than " +
					MAX_LEN +
					" characters.");
			}
			//
			req.setBodyParameter("description", info);
		}
		info = newUserInfo.getString(MetadataSet.USERACCOUNT_URL);
		if (!StringUtil.isEmpty(info)) {
			req.setBodyParameter("url", info);
		}
		info = newUserInfo.getString(MetadataSet.USERACCOUNT_LOCATION);
		if (!StringUtil.isEmpty(info)) {
			req.setBodyParameter("location", info);
		}
		//
		if (req.getBodyParameters().size() == 0) {
			throw new IllegalArgumentException(
				"New user info must not be empty.");
		}
		//
		try {
			HttpResponse resp = req.send();
			//
			HttpResponseCodeInterpreter.perform(resp);
			//
			Parser parser = ParserFactory.getDefaultParser();
			AccountHandler handler = new AccountHandler();
			parser.parse(resp.getStream(), handler);
			//
			handler.loadParsedUserAccount(newUserInfo);
			//
			return newUserInfo;
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		} finally {
			req.close();
		}		
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (o == null || !(o instanceof UserAccountManager)) {
			return false;
		} else {
			return credential.equals(((UserAccountManager)o).credential);
		}
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return credential.hashCode();
	}
	
	/**
	 * <p>
	 * Get the user's credentials.
	 * </p>
	 * @return Credential object.
	 */
	Credential getCredential() {
		return credential;
	}
	
	/**
	 * <p>
	 * Create a HttpRequest object.
	 * </p>
	 * @param url URL.
	 * @param method Http method.
	 * @return Request object.
	 */
	synchronized HttpRequest createRequest(String url) {
		HttpRequest req = new HttpRequest(url);
		//
		if (credential.hasXAuthCredentials()) {
			if (token != null) {
				req.setSigner(signer, token);
			}
		} else {
			try {
				String crdntls = credential.getBasicHttpAuthCredential();
				crdntls = Base64Encoder.encode(crdntls.getBytes("UTF-8"));
				req.setHeaderField("Authorization", "Basic " + crdntls);
			} catch (UnsupportedEncodingException e) {
				throw new IllegalArgumentException(
					"Invalid UTF-8 credentials.");
			}
		}
		//
		return req;
	}

	/**
	 * <p>
	 * Check whether the instance is still valid.
	 * </p>
	 * @throws IllegalStateException Instance invalidated.
	 */
	private synchronized void checkValid() {
		if (invalidated) {
			throw new IllegalStateException(
				"This instance is no longer valid. Get a new one!");
		}
	}

	/**
	 * <p>
	 * Check whether it is verified.
	 * </p>
	 * @throws SecurityException If it is not properly verified.
	 */
	private void checkVerified() {
		if (!verified) {
			throw new SecurityException(
				"User's credentials have not been verified yet.");
		}
	}

	/**
	 * <p>
	 * Save the instance on pool.
	 * </p>
	 */
	private synchronized void saveSelfOnPool() {
		if (userAccountMngrPoll.get(credential) == null) {
			userAccountMngrPoll.put(credential, this);
		}
	}
}