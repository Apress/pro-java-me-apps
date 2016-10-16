/*
 * Timeline.java
 * 11/04/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import com.twitterapime.io.HttpRequest;
import com.twitterapime.io.HttpResponse;
import com.twitterapime.io.HttpResponseCodeInterpreter;
import com.twitterapime.parser.Handler;
import com.twitterapime.parser.Parser;
import com.twitterapime.parser.ParserFactory;
import com.twitterapime.rest.handler.DirectMessageHandler;
import com.twitterapime.rest.handler.TimelineHandler;
import com.twitterapime.search.Query;
import com.twitterapime.search.QueryComposer;
import com.twitterapime.search.SearchDeviceListener;

/**
 * <p>
 * This class defines the methods responsible for accessing the main timelines
 * (i.e. public, home, user, mentions and DMs) provided by Twitter.
 * </p>
 * <p>
 * <pre>
 * Credential c = new Credential(...);
 * UserAccountManager uam = UserAccountManager.getInstance(c);
 * 
 * if (uam.verifyCredential()) {
 *   Timeline ter = Timeline.getInstance(uam);
 * 
 *   Query q = QueryComposer.count(5); //return only 5 latest tweets.
 * 
 *   ter.startGetHomeTweets(q, new SearchDeviceListener() {
 *       public void searchCompleted() {}
 *       public void searchFailed(Throwable cause) {}
 *       public void tweetFound(Tweet tweet) {
 *           System.out.println(tweet);
 *       }
 *   });
 * }
 * </pre>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.1
 * @since 1.2
 * @see UserAccountManager
 * @see SearchDeviceListener
 * @see QueryComposer
 */
public final class Timeline {
	/**
	 * <p>
	 * Hold all Twitter API URL services.
	 * </p>
	 */
	private static final Hashtable SERVICES_URL;
	
	/**
	 * <p>
	 * Timeline pool used to cache instanced associated to user accounts.
	 * </p>
	 */
	private static Hashtable timelinePool;
	
	/**
	 * <p>
	 * Single instance of this class.
	 * </p>
	 */
	private static Timeline singleInstance;
	
	/**
	 * <p>
	 * Key for Twitter API URL service statuses public timeline.
	 * </p>
	 * @see Timeline#setServiceURL(String, String)
	 * @see Timeline#startGetPublicTweets(SearchDeviceListener)
	 */
	public static final String TWITTER_API_URL_SERVICE_STATUSES_PUBLIC_TIMELINE =
		"TWITTER_API_URL_SERVICE_STATUSES_PUBLIC_TIMELINE";

	/**
	 * <p>
	 * Key for Twitter API URL service statuses home timeline.
	 * </p>
	 * @see Timeline#setServiceURL(String, String)
	 * @see Timeline#startGetHomeTweets(Query, SearchDeviceListener)
	 */
	public static final String TWITTER_API_URL_SERVICE_STATUSES_HOME_TIMELINE =
		"TWITTER_API_URL_SERVICE_STATUSES_HOME_TIMELINE";

	/**
	 * <p>
	 * Key for Twitter API URL service statuses user timeline.
	 * </p>
	 * @see Timeline#setServiceURL(String, String)
	 * @see Timeline#startGetUserTweets(Query, SearchDeviceListener)
	 */
	public static final String TWITTER_API_URL_SERVICE_STATUSES_USER_TIMELINE =
		"TWITTER_API_URL_SERVICE_STATUSES_USER_TIMELINE";

	/**
	 * <p>
	 * Key for Twitter API URL service statuses mentions.
	 * </p>
	 * @see Timeline#setServiceURL(String, String)
	 * @see Timeline#startGetMentions(Query, SearchDeviceListener)
	 */
	public static final String TWITTER_API_URL_SERVICE_STATUSES_MENTIONS =
		"TWITTER_API_URL_SERVICE_STATUSES_MENTIONS";

	/**
	 * <p>
	 * Key for Twitter API URL service direct messages.
	 * </p>
	 * @see Timeline#setServiceURL(String, String)
	 * @see Timeline#startGetDirectMessages(Query, boolean, SearchDeviceListener)
	 */
	public static final String TWITTER_API_URL_SERVICE_DIRECT_MESSAGES =
		"TWITTER_API_URL_SERVICE_DIRECT_MESSAGES";

	/**
	 * <p>
	 * Key for Twitter API URL service direct messages sent.
	 * </p>
	 * @see Timeline#setServiceURL(String, String)
	 * @see Timeline#startGetDirectMessages(Query, boolean, SearchDeviceListener)
	 */
	public static final String TWITTER_API_URL_SERVICE_DIRECT_MESSAGES_SENT =
		"TWITTER_API_URL_SERVICE_DIRECT_MESSAGES_SENT";

	static {
		SERVICES_URL = new Hashtable(6);
		//
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_STATUSES_PUBLIC_TIMELINE,
			"http://api.twitter.com/1/statuses/public_timeline.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_STATUSES_HOME_TIMELINE,
			"http://api.twitter.com/1/statuses/home_timeline.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_STATUSES_USER_TIMELINE,
			"http://api.twitter.com/1/statuses/user_timeline.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_STATUSES_MENTIONS,
			"http://api.twitter.com/1/statuses/mentions.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_DIRECT_MESSAGES,
			"http://api.twitter.com/1/direct_messages.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_DIRECT_MESSAGES_SENT,
			"http://api.twitter.com/1/direct_messages/sent.xml");
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
		if (timelinePool != null) {
			Enumeration keys = timelinePool.keys();
			Object key;
			Timeline value;
			//
			while (keys.hasMoreElements()) {
				key = keys.nextElement();
				value = (Timeline)timelinePool.get(key);
				//
				if (!value.userAccountMngr.isVerified()) {
					timelinePool.remove(key);
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
	 * @see Timeline#TWITTER_API_URL_SERVICE_DIRECT_MESSAGES
	 * @see Timeline#TWITTER_API_URL_SERVICE_DIRECT_MESSAGES_SENT
	 * @see Timeline#TWITTER_API_URL_SERVICE_STATUSES_HOME_TIMELINE
	 * @see Timeline#TWITTER_API_URL_SERVICE_STATUSES_MENTIONS
	 * @see Timeline#TWITTER_API_URL_SERVICE_STATUSES_PUBLIC_TIMELINE
	 * @see Timeline#TWITTER_API_URL_SERVICE_STATUSES_USER_TIMELINE
	 */
	public void setServiceURL(String serviceKey, String url) {
		SERVICES_URL.put(serviceKey, url);
	}
	
	/**
	 * <p>
	 * Get an instance of Timeline class and associate it to a given user
	 * account.
	 * </p>
	 * @param uam User account manager.
	 * @return Timeline instance.
	 * @throws IllegalArgumentException If UserAccountManager is null.
	 * @throws SecurityException If UserAccountManager is not verified.
	 */
	public synchronized static Timeline getInstance(UserAccountManager uam) {
		if (uam == null) {
			throw new IllegalArgumentException(
				"UserAccountManager must not be null.");
		}
		//
		if (!uam.isVerified()) {
			throw new SecurityException("User's credential must be verified.");
		}
		//
		if (timelinePool == null) {
			timelinePool = new Hashtable();
		}
		//
		Timeline ter = (Timeline)timelinePool.get(uam);
		if (ter == null) {
			ter = new Timeline(uam);
			timelinePool.put(uam, ter);
		}
		//
		return ter;
	}

	/**
	 * <p>
	 * Get a single instance of Timeline class, which is NOT associated to any
	 * user account.
	 * </p>
	 * @return Timeline single instance.
	 */
	public synchronized static Timeline getInstance() {
		if (singleInstance == null) {
			singleInstance = new Timeline();
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
	 * Create an instance of Timeline class.
	 * </p>
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 */
	private Timeline() {
	}
	
	/**
	 * <p>
	 * Create an instance of Timeline class.
	 * </p>
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 * @param uam User account manager.
	 */
	private Timeline(UserAccountManager uam) {
		userAccountMngr = uam;
	}
	
	/**
	 * <p>
	 * Get most recent tweets from non-protected users who have set a custom
	 * user icon.
	 * </p>
	 * <p>
	 * This method does not wait for the search process is completed to return.
	 * To have access to this search's result, a SearchDeviceListener object
	 * must be registered. 
	 * </p>
	 * @param l Listener object to be notified about the search's result.
	 * @throws IllegalArgumentException If listener is null.
	 */
	public void startGetPublicTweets(SearchDeviceListener l) {
		TimelineHandler h = new TimelineHandler();
		h.setSearchDeviceListener(l);
		//
		startGet(
			TWITTER_API_URL_SERVICE_STATUSES_PUBLIC_TIMELINE,
			null,
			l,
			h,
			false);
	}
	
	/**
	 * <p>
	 * Get most recent tweets, including retweets, posted by the
	 * authenticating user and the user's friends according to given filter.
	 * This is the equivalent to timeline home on the Web.
	 * </p>
	 * <p>
	 * This method does not wait for the search process is completed to return.
	 * To have access to this search's result, a SearchDeviceListener object
	 * must be registered. 
	 * </p>
	 * <p>
	 * In order to create the query, only the following methods can be used as
	 * filters:
	 * <ul>
	 * <li>{@link QueryComposer#sinceID(String)}</li>
	 * <li>{@link QueryComposer#maxID(String)}</li>
	 * <li>{@link QueryComposer#count(int)}</li>
	 * <li>{@link QueryComposer#page(int)}</li>
	 * </ul>
	 * </p>
	 * @param q The filter query. If null all tweets are returned.
	 * @param l Listener object to be notified about the search's result.
	 * @throws SecurityException If it is not authenticated.
	 * @throws IllegalArgumentException If listener is null.
	 */
	public void startGetHomeTweets(Query q, SearchDeviceListener l) {
		TimelineHandler h = new TimelineHandler();
		h.setSearchDeviceListener(l);
		//
		startGet(TWITTER_API_URL_SERVICE_STATUSES_HOME_TIMELINE, q, l, h, true);
	}
	
	/**
	 * <p>
	 * Get most recent tweets posted from the authenticating user
	 * and that user's friends according to given filter. This is the equivalent
	 * of the Web user page for your own user.
	 * </p>
	 * <p>
	 * This method does not wait for the search process is completed to return.
	 * To have access to this search's result, a SearchDeviceListener object
	 * must be registered. 
	 * </p>
	 * <p>
	 * In order to create the query, only the following methods can be used as
	 * filters:
	 * <ul>
	 * <li>{@link QueryComposer#sinceID(String)}</li>
	 * <li>{@link QueryComposer#maxID(String)}</li>
	 * <li>{@link QueryComposer#count(int)}</li>
	 * <li>{@link QueryComposer#page(int)}</li>
	 * </ul>
	 * </p>
	 * @param q The filter query. If null all tweets are returned.
	 * @param l Listener object to be notified about the search's result.
	 * @throws SecurityException If it is not authenticated.
	 * @throws IllegalArgumentException If listener is null.
	 */
	public void startGetUserTweets(Query q, SearchDeviceListener l) {
		TimelineHandler h = new TimelineHandler();
		h.setSearchDeviceListener(l);
		//
		startGet(TWITTER_API_URL_SERVICE_STATUSES_USER_TIMELINE, q, l, h, true);
	}
	
	/**
	 * <p>
	 * Get most recent mentions (status containing @username) for the
	 * authenticating user according to given filter.
	 * </p>
	 * <p>
	 * This method does not wait for the search process is completed to return.
	 * To have access to this search's result, a SearchDeviceListener object
	 * must be registered. 
	 * </p>
	 * <p>
	 * In order to create the query, only the following methods can be used as
	 * filters:
	 * <ul>
	 * <li>{@link QueryComposer#sinceID(String)}</li>
	 * <li>{@link QueryComposer#maxID(String)}</li>
	 * <li>{@link QueryComposer#count(int)}</li>
	 * <li>{@link QueryComposer#page(int)}</li>
	 * </ul>
	 * </p>
	 * @param q The filter query. If null all tweets are returned.
	 * @param l Listener object to be notified about the search's result.
	 * @throws SecurityException If it is not authenticated.
	 * @throws IllegalArgumentException If listener is null.
	 */
	public void startGetMentions(Query q, SearchDeviceListener l) {
		TimelineHandler h = new TimelineHandler();
		h.setSearchDeviceListener(l);
		//
		startGet(TWITTER_API_URL_SERVICE_STATUSES_MENTIONS, q, l, h, true);
	}
	
	/**
	 * <p>
	 * Get all Direct Messages from/to the authenticating user according to
	 * given filter.
	 * </p>
	 * <p>
	 * This method does not wait for the search process is completed to return.
	 * To have access to this search's result, a SearchDeviceListener object
	 * must be registered. 
	 * </p>
	 * <p>
	 * In order to create the query, only the following methods can be used as
	 * filters:
	 * <ul>
	 * <li>{@link QueryComposer#sinceID(String)}</li>
	 * <li>{@link QueryComposer#maxID(String)}</li>
	 * <li>{@link QueryComposer#count(int)}</li>
	 * <li>{@link QueryComposer#page(int)}</li>
	 * </ul>
	 * </p>
	 * @param q The filter query. If null all DMs are returned.
	 * @param l Listener object to be notified about the search's result.
	 * @param received Return received DMs (true), otherwise, sent ones.
	 * @throws SecurityException If it is not authenticated.
	 * @throws IllegalArgumentException If listener is null.
	 */
	public void startGetDirectMessages(Query q, boolean received, 
		SearchDeviceListener l) {
		final String urlKey = 
			received
				? TWITTER_API_URL_SERVICE_DIRECT_MESSAGES
				: TWITTER_API_URL_SERVICE_DIRECT_MESSAGES_SENT;
		DirectMessageHandler h = new DirectMessageHandler();
		h.setSearchDeviceListener(l);
		//
		startGet(urlKey, q, l, h, true);
	}
		
	/**
	 * <p>
	 * Start a retrieval of a given URL's response according to a filter.
	 * </p>
	 * @param servURLKey The service URL key.
	 * @param q The filter query.
	 * @param l Listener object to be notified about the search's result.
	 * @param h Content handler.
	 * @param checkAuth Authentication required (true).
	 * @throws SecurityException If it is not authenticated.
	 * @throws IllegalArgumentException Invalid parameters.
	 */
	private void startGet(final String servURLKey, final Query q,
		final SearchDeviceListener l, final Handler h, final boolean checkAuth){
		if (servURLKey == null || servURLKey.trim().length() == 0) {
			throw new IllegalArgumentException("Url must not be empty/null.");
		}
		if (l == null) {
			throw new IllegalArgumentException("Listener must not be null.");
		}
		if (h == null) {
			throw new IllegalArgumentException("Handler must not be null.");
		}
		//
		if (checkAuth) {
			checkUserAuth();
		}
		//
		Runnable r = new Runnable() {
			public void run() {
				HttpRequest req;
				String url = getURL(servURLKey);
				url = q != null ? url + '?' + q.toString() : url;
				//
				if (userAccountMngr != null) {
					req = userAccountMngr.createRequest(url);
				} else {
					req = new HttpRequest(url);
				}
				//
				try {
					HttpResponse resp = req.send();
					//
					HttpResponseCodeInterpreter.perform(resp);
					//
					Parser parser = ParserFactory.getDefaultParser();
					parser.parse(resp.getStream(), h);
					//
                                        resp.getStream().close();
					l.searchCompleted();
				} catch (Exception e) {
					l.searchFailed(e);
				} finally {
					try {
						req.close();
					} catch (IOException e) {e.printStackTrace();}
				}
			}
		};
		//
		Thread t = new Thread(r);
		t.start();
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