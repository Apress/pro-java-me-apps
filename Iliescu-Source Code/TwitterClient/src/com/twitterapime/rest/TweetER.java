/*
 * TweetER.java
 * 11/11/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import com.twitterapime.io.HttpConnection;
import com.twitterapime.io.HttpRequest;
import com.twitterapime.io.HttpResponse;
import com.twitterapime.io.HttpResponseCodeInterpreter;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.Parser;
import com.twitterapime.parser.ParserException;
import com.twitterapime.parser.ParserFactory;
import com.twitterapime.rest.handler.DirectMessageHandler;
import com.twitterapime.rest.handler.StatusHandler;
import com.twitterapime.search.InvalidQueryException;
import com.twitterapime.search.LimitExceededException;
import com.twitterapime.search.Tweet;

/**
 * <p>
 * This class defines the methods responsible for managing (e.g. post, retrieve,
 * etc) tweets.
 * </p>
 * <p>
 * <pre>
 * Credential c = new Credential(...);
 * UserAccountManager uam = UserAccountManager.getInstance(c);
 * 
 * if (uam.verifyCredential()) {
 *   TweetER ter = TweetER.getInstance(uam);
 *   Tweet t = ter.post(new Tweet("status message"));
 * }
 * ...
 * TweetER ter = TweetER.getInstance();
 * Tweet t = ter.findByID("12635687984");
 * </pre>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.3
 * @since 1.1
 * @see Tweet
 * @see UserAccountManager
 */
public final class TweetER {
	/**
	 * <p>
	 * Hold all Twitter API URL services.
	 * </p>
	 */
	private static final Hashtable SERVICES_URL;
	
	/**
	 * <p>
	 * TweetER pool used to cache instanced associated to user accounts.
	 * </p>
	 */
	private static Hashtable tweetERPool;

	/**
	 * <p>
	 * Single instance of this class.
	 * </p>
	 */
	private static TweetER singleInstance;

	/**
	 * <p>
	 * Key for Twitter API URL service statuses update.
	 * </p>
	 * @see TweetER#setServiceURL(String, String)
	 * @see TweetER#post(Tweet)
	 */
	public static final String TWITTER_API_URL_SERVICE_STATUSES_UPDATE =
		"TWITTER_API_URL_SERVICE_STATUSES_UPDATE";
	
	/**
	 * <p>
	 * Key for Twitter API URL service statuses show.
	 * </p>
	 * @see TweetER#setServiceURL(String, String)
	 * @see TweetER#findByID(String)
	 */
	public static final String TWITTER_API_URL_SERVICE_STATUSES_SHOW =
		"TWITTER_API_URL_SERVICE_STATUSES_SHOW";

	/**
	 * <p>
	 * Key for Twitter API URL service statuses retweet.
	 * </p>
	 * @see TweetER#setServiceURL(String, String)
	 * @see TweetER#repost(Tweet)
	 */
	public static final String TWITTER_API_URL_SERVICE_STATUSES_RETWEET =
		"TWITTER_API_URL_SERVICE_STATUSES_RETWEET";
	
	/**
	 * <p>
	 * Key for Twitter API URL service direct messages new.
	 * </p>
	 * @see TweetER#setServiceURL(String, String)
	 * @see TweetER#send(Tweet)
	 */
	public static final String TWITTER_API_URL_SERVICE_DIRECT_MESSAGES_NEW =
		"TWITTER_API_URL_SERVICE_DIRECT_MESSAGES_NEW";

	static {
		SERVICES_URL = new Hashtable(4);
		//
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_STATUSES_UPDATE,
			"http://api.twitter.com/1/statuses/update.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_STATUSES_SHOW,
			"http://api.twitter.com/1/statuses/show.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_STATUSES_RETWEET,
			"http://api.twitter.com/1/statuses/retweet/");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_DIRECT_MESSAGES_NEW,
			"http://api.twitter.com/1/direct_messages/new.xml");
	}
	
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
	 * Release the objects which account is no longer authenticated.
	 * </p>
	 */
	static synchronized void cleanPool() {
		if (tweetERPool != null) {
			Enumeration keys = tweetERPool.keys();
			Object key;
			TweetER value;
			//
			while (keys.hasMoreElements()) {
				key = keys.nextElement();
				value = (TweetER)tweetERPool.get(key);
				//
				if (!value.userAccountMngr.isVerified()) {
					tweetERPool.remove(key);
				}
			}
		}
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
	 * @see TweetER#TWITTER_API_URL_SERVICE_STATUSES_UPDATE
	 * @see TweetER#TWITTER_API_URL_SERVICE_STATUSES_SHOW
	 * @see TweetER#TWITTER_API_URL_SERVICE_STATUSES_RETWEET
	 * @see TweetER#TWITTER_API_URL_SERVICE_DIRECT_MESSAGES_NEW
	 */
	public void setServiceURL(String serviceKey, String url) {
		SERVICES_URL.put(serviceKey, url);
	}
	
	/**
	 * <p>
	 * Get an instance of TweetER class and associate it to a given user
	 * account.
	 * </p>
	 * @param uam User account manager.
	 * @return TweetER instance.
	 * @throws IllegalArgumentException If UserAccountManager is null.
	 * @throws SecurityException If UserAccountManager is not verified.
	 */
	public synchronized static TweetER getInstance(UserAccountManager uam) {
		if (uam == null) {
			throw new IllegalArgumentException(
				"UserAccountManager must not be null.");
		}
		//
		if (!uam.isVerified()) {
			throw new SecurityException("User's credential must be verified.");
		}
		//
		if (tweetERPool == null) {
			tweetERPool = new Hashtable();
		}
		//
		TweetER ter = (TweetER)tweetERPool.get(uam);
		if (ter == null) {
			ter = new TweetER(uam);
			tweetERPool.put(uam, ter);
		}
		//
		return ter;
	}
	
	/**
	 * <p>
	 * Get a single instance of TweetER class, which is NOT associated to any
	 * user account.
	 * </p>
	 * @return TweetER single instance.
	 */
	public synchronized static TweetER getInstance() {
		if (singleInstance == null) {
			singleInstance = new TweetER();
		}
		//
		return singleInstance;
	}
	
	/**
	 * <p>
	 * User account manager.
	 * </p>
	 */
	private UserAccountManager userAccountMngr;

	/**
	 * <p>
	 * Create an instance of TweetER class.
	 * </p>
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 */
	private TweetER() {
	}
	
	/**
	 * <p>
	 * Create an instance of TweetER class.
	 * </p>
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 * @param uam User account manager.
	 */
	private TweetER(UserAccountManager uam) {
		userAccountMngr = uam;
	}

	/**
	 * <p>
	 * Find the tweet associated to the given ID. This method does not require
	 * to be logged in to Twitter to use it. In other words, you can use the
	 * TweetER single instance ({@link TweetER#getInstance()}). If the given ID
	 * does not exist, <code>null</code> will be returned.
	 * </p>
	 * @param id Tweet's ID.
	 * @return Tweet.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 * @throws IOException If an I/O error occurs.
	 * @throws SecurityException If the requested tweet is protected.
	 * @throws IllegalArgumentException If the given ID is empty/null.
	 */
	public Tweet findByID(String id) throws LimitExceededException,
		IOException {
		if (id == null || (id = id.trim()).length() == 0) {
			throw new IllegalArgumentException("ID must not be empty/null.");
		}
		//
		String url = getURL(TWITTER_API_URL_SERVICE_STATUSES_SHOW) + "?id=" +id;
		//
		HttpRequest req;
		if (userAccountMngr != null) {
			checkUserAuth();
			req = userAccountMngr.createRequest(url);
		} else {
			req = new HttpRequest(url);
		}
		//
		try {
			HttpResponse resp = req.send();
			//
			if (resp.getCode() == HttpConnection.HTTP_FORBIDDEN) {
				throw new SecurityException(); //the refereed tweet id is protected.
			} else if (resp.getCode() == HttpConnection.HTTP_NOT_FOUND) {
				return (Tweet)null; //tweet id not found.
			}
			//
			HttpResponseCodeInterpreter.perform(resp);
			//
			Parser parser = ParserFactory.getDefaultParser();
			StatusHandler handler = new StatusHandler();
			parser.parse(resp.getStream(), handler);
			//
			return handler.getParsedTweet();
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		} finally {
			req.close();
		}
	}

	/**
	 * <p>
	 * Post a given tweet to Twitter. The tweet's content must be up to 140
	 * characters and must be properly logged in ({@link UserAccountManager}).
	 * </p>
	 * @param tweet Tweet to be posted.
	 * @return Tweet post with some additional data.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws SecurityException If it is not properly logged in.
	 * @throws IllegalArgumentException If the given tweet is null/empty, etc.
	 */
	public Tweet post(Tweet tweet) throws IOException, LimitExceededException {
		if (tweet == null) {
			throw new IllegalArgumentException("Tweet must not be null.");
		}
		//
		tweet.validateContent();
		//
		checkUserAuth();
		//
		HttpRequest req = userAccountMngr.createRequest(
			getURL(TWITTER_API_URL_SERVICE_STATUSES_UPDATE));
		req.setMethod(HttpConnection.POST);
		req.setBodyParameter(
			"status", tweet.getString(MetadataSet.TWEET_CONTENT));
		//
		GeoLocation location = tweet.getLocation();
		//
		if (location != null) {
			String lat = location.getString(MetadataSet.GEOLOCATION_LATITUDE);
			String lon = location.getString(MetadataSet.GEOLOCATION_LONGITUDE);
			String pid = location.getString(MetadataSet.GEOLOCATION_PLACE_ID);
			//
			if (lat != null && lon != null) {
				req.setBodyParameter("lat", lat);
				req.setBodyParameter("long", lon);
				req.setBodyParameter("display_coordinates", "true");
			} else if (pid != null) {
				req.setBodyParameter("place_id", pid);
				req.setBodyParameter("display_coordinates", "true");
			}
		}
		//
		try {
			HttpResponse resp = req.send();
			//
			HttpResponseCodeInterpreter.perform(resp);
			//
			Parser parser = ParserFactory.getDefaultParser();
			StatusHandler handler = new StatusHandler();
			parser.parse(resp.getStream(), handler);
			handler.loadParsedTweet(tweet);
			//
			return tweet;
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		} finally {
			req.close();
		}
	}
	
	/**
	 * <p>
	 * Repost a given tweet to Twitter.
	 * </p>
	 * @param tweet Tweet to be reposted.
	 * @return Tweet reposted.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws SecurityException If it is not properly logged in.
	 * @throws IllegalArgumentException If the given tweet is null/empty, etc.
	 * @throws InvalidQueryException If tweet's ID is invalid.
	 */
	public Tweet repost(Tweet tweet) throws IOException, LimitExceededException{
		if (tweet == null) {
			throw new IllegalArgumentException("Tweet must not be null.");
		}
		//
		String id = tweet.getString(MetadataSet.TWEET_ID);
		if (id == null || (id = id.trim()).length() == 0) {
			throw new IllegalArgumentException(
				"Tweet ID must not be empty/null.");
		}
		//
		checkUserAuth();
		//
		String url = getURL(TWITTER_API_URL_SERVICE_STATUSES_RETWEET)+id+".xml";
		//
		HttpRequest req = userAccountMngr.createRequest(url);
		req.setMethod(HttpConnection.POST);
		//
		try {
			HttpResponse resp = req.send();
			//
			HttpResponseCodeInterpreter.perform(resp);
			//
			Parser parser = ParserFactory.getDefaultParser();
			StatusHandler handler = new StatusHandler();
			parser.parse(resp.getStream(), handler);
			handler.loadParsedTweet(tweet);
			//
			return tweet;
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		} finally {
			req.close();
		}
	}
	
	/**
	 * <p>
	 * Send a given Direct Message to a given recipient. The DM's content must
	 * be up to 140 characters, addressed to a proper user (he/she must also
	 * follow you) and must be properly logged in ({@link UserAccountManager}).
	 * </p>
	 * @param dm Direct Message to be sent.
	 * @return Sent Direct Message.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws InvalidQueryException If recipient does not exist or the sender
	 *         does not follow the recipient and vice versa.
	 * @throws SecurityException If it is not properly logged in.
	 * @throws IllegalArgumentException If the given DM is null/empty, etc.
	 */
	public Tweet send(Tweet dm) throws IOException, LimitExceededException {
		if (dm == null) {
			throw new IllegalArgumentException("DM must not be null.");
		}
		//
		dm.validateRecipient();
		dm.validateContent();
		//
		checkUserAuth();
		//
		HttpRequest req = userAccountMngr.createRequest(
			getURL(TWITTER_API_URL_SERVICE_DIRECT_MESSAGES_NEW));
		req.setMethod(HttpConnection.POST);
		//
		String recipient = dm.getString(MetadataSet.TWEET_AUTHOR_USERNAME);
		if (recipient == null) {
			UserAccount ua = dm.getUserAccount();
			recipient = ua.getString(MetadataSet.USERACCOUNT_ID);
			if (recipient == null) {
				recipient =	ua.getString(MetadataSet.USERACCOUNT_USER_NAME);
			}
		}
		req.setBodyParameter("user", recipient);
		req.setBodyParameter("text", dm.getString(MetadataSet.TWEET_CONTENT));
		//
		try {
			HttpResponse resp = req.send();
			//
			HttpResponseCodeInterpreter.perform(resp);
			//
			Parser parser = ParserFactory.getDefaultParser();
			DirectMessageHandler handler = new DirectMessageHandler();
			parser.parse(resp.getStream(), handler);
			handler.loadParsedTweet(dm, 0);
			//
			return dm;
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		} finally {
			req.close();
		}
	}
	
	/**
	 * <p>
	 * Check if the user's is authenticated.
	 * </p>
	 * @throws SecurityException User is not authenticated.
	 */
	private void checkUserAuth() {
		if (userAccountMngr == null || !userAccountMngr.isVerified()) {
			throw new SecurityException(
			    "User's credential must be entered to perform this operation.");
		}
	}
}