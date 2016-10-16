/*
 * SearchDevice.java
 * 23/09/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.search;

import java.io.IOException;
import java.util.Hashtable;

import com.twitterapime.io.HttpConnection;
import com.twitterapime.io.HttpRequest;
import com.twitterapime.io.HttpResponse;
import com.twitterapime.io.HttpResponseCodeInterpreter;
import com.twitterapime.parser.Parser;
import com.twitterapime.parser.ParserException;
import com.twitterapime.parser.ParserFactory;
import com.twitterapime.rest.RateLimitStatus;
import com.twitterapime.rest.handler.RateLimitStatusHandler;
import com.twitterapime.search.handler.SearchResultHandler;

/**
 * <p>
 * This class is the entry point of Search API, which defines the methods
 * responsible for submitting a query to Twitter Search API.
 * </p>
 * <p>
 * <pre>
 * SearchDevice sd = SearchDevice.getInstance();
 * Query q1 = QueryComposer.from("twitteruser");
 * Query q2 = QueryComposer.containAny("search api");
 * Query q = QueryComposer.append(q1, q2);
 * Tweet[] ts = sd.searchTweets(q);
 * for (int i = 0; i < ts.length; i++) {
 *   list.append(ts[i].getString(MetadataSet.TWEET_CONTENT), null);
 * }
 * </pre>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.3
 * @since 1.0
 * @see SearchDeviceListener
 * @see QueryComposer
 */
public final class SearchDevice {
	/**
	 * <p>
	 * Hold all Twitter API URL services.
	 * </p>
	 */
	private static final Hashtable SERVICES_URL;

	/**
	 * <p>
	 * Query prefix.
	 * </p>
	 */
	private static final String TWITTER_QUERY_STRING_PREFIX = "q=";

	/**
	 * <p>
	 * Single instance of this class.
	 * </p>
	 */
	private static SearchDevice device;
	
	/**
	 * <p>
	 * Time at which Twitter Search API was access by this class.
	 * </p>
	 */
	private static long lastCallTime;
	
	/**
	 * <p>
	 * Number of calls to Twitter Search API since this class was loaded.
	 * </p>
	 */
	private static int apiCallsCount;
	
	/**
	 * <p>
	 * Key for Twitter API URL service search.
	 * </p>
	 * @see SearchDevice#setServiceURL(String, String)
	 * @see SearchDevice#searchTweets(Query)
	 * @see SearchDevice#searchTweets(String)
	 * @see SearchDevice#startSearchTweets(Query, SearchDeviceListener)
	 * @see SearchDevice#startSearchTweets(String, SearchDeviceListener)
	 */
	public static final String TWITTER_API_URL_SERVICE_SEARCH =
		"TWITTER_API_URL_SERVICE_SEARCH";

	/**
	 * <p>
	 * Key for Twitter API URL service account rate limit status.
	 * </p>
	 * @see SearchDevice#setServiceURL(String, String)
	 * @see SearchDevice#getRateLimitStatus()
	 */
	public static final String TWITTER_API_URL_SERVICE_ACCOUNT_RATE_LIMIT_STATUS =
		"TWITTER_API_URL_SERVICE_ACCOUNT_RATE_LIMIT_STATUS";

	static {
		SERVICES_URL = new Hashtable(2);
		//
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_SEARCH,
			"http://search.twitter.com/search.atom");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_ACCOUNT_RATE_LIMIT_STATUS,
			"http://api.twitter.com/1/account/rate_limit_status.xml");
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
	 * @see SearchDevice#TWITTER_API_URL_SERVICE_SEARCH
	 * @see SearchDevice#TWITTER_API_URL_SERVICE_ACCOUNT_RATE_LIMIT_STATUS
	 */
	public void setServiceURL(String serviceKey, String url) {
		SERVICES_URL.put(serviceKey, url);
	}

	/**
	 * <p>
	 * Get an instance of SearchDevice class.
	 * </p>
	 * @return A SearchDevice object.
	 */
	public synchronized static SearchDevice getInstance() {
		if (device == null) {
			device = new SearchDevice();
		}
		//
		return device;
	}
	
	/**
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 */
	private SearchDevice() {
	}

	/**
	 * <p>
	 * Search for tweets that match the given query. This method gets blocked
	 * until the search is completed or an exception is thrown.
	 * </p>
	 * @param query The query.
	 * @return The result.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 * @throws IllegalArgumentException If query is null.
	 */
	public Tweet[] searchTweets(Query query) throws IOException,
		LimitExceededException {
		if (query == null) {
			throw new IllegalArgumentException("Query must not be null.");
		}
		//
		return searchTweets(query, null);
	}

	/**
	 * <p>
	 * Search for tweets that match the given query string. This method gets
	 * blocked until the search is completed or an exception is thrown.
	 * </p>
	 * @param queryString The query string.
	 * @return The result.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 * @throws IllegalArgumentException If queryString is null/empty.
	 */
	public Tweet[] searchTweets(String queryString) throws IOException,
		LimitExceededException {
		if (queryString == null
				|| (queryString = queryString.trim()).length() == 0) {
			throw new IllegalArgumentException(
				"QueryString must not be null/empty.");
		}
		//
		return searchTweets(new Query(queryString), null);
	}
	
	/**
	 * <p>
	 * Search for tweets that match the given query. This method does not wait
	 * for the search process is completed to return. To have access to this
	 * search's result, a SearchDeviceListener object must be registered. 
	 * </p>
	 * @param query The query.
	 * @param listener Listener object to be notified about the search's result.
	 * @throws IllegalArgumentException If query is null.
	 */
	public void startSearchTweets(Query query, SearchDeviceListener listener) {
		if (query == null) {
			throw new IllegalArgumentException("Query must not be null.");
		}
		//
		startSearchTweets(query.toString(), listener);
	}

	/**
	 * <p>
	 * Search for tweets that match the given query string. This method does not
	 * wait for the search process is completed to return. To have access to
	 * this search's result, a SearchDeviceListener object must be registered. 
	 * </p>
	 * @param queryString The query string.
	 * @param listener Listener object to be notified about the search's result.
	 * @throws IllegalArgumentException If queryString is null/empty.
	 */
	public void startSearchTweets(final String queryString,
		final SearchDeviceListener listener) {
		if (queryString == null || queryString.length() == 0) {
			throw new IllegalArgumentException(
				"QueryString must not be null/empty.");
		}
		//
		Runnable r = new Runnable() {
			public void run() {
				try {
					searchTweets(new Query(queryString), listener);
					if (listener != null) {
						listener.searchCompleted();
					}
				} catch (Exception e) {
					if (listener != null) {
						listener.searchFailed(e);
					}
				}
			}
		};
		//
		Thread t = new Thread(r);
		t.start();
	}
	
	/**
	 * <p>
	 * Return a set of info about the number of API requests available to the
	 * requesting IP address before the REST API limit is reached for the
	 * current hour.
	 * </p>
	 * <p>
	 * Stay aware of these limits, since it can impact the usage of some methods
	 * of this API.
	 * </p>
	 * @return Rate limiting status info.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @see SearchDevice#getAPICallsCount()
	 */
	public RateLimitStatus getRateLimitStatus() throws IOException,
		LimitExceededException {
		HttpRequest req = new HttpRequest(
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
	 * Get the calls count submitted to Twitter Search API since the API was
	 * loaded by class loader. So this number is reseted when the API is
	 * unloaded.
	 * </p>
	 * @return Call count.
	 * @see SearchDevice#getRateLimitStatus()
	 */
	public int getAPICallsCount() {
		return apiCallsCount;
	}

	/**
	 * <p>
	 * Get the time at which the last call was submitted to Twitter Search API.
	 * </p>
	 * @return Time of last call.
	 */
	public long getLastAPICallTime() {
		return lastCallTime;
	}
	
	/**
	 * <p>
	 * Search for tweets that match the given query. If the listener parameter
	 * is not passed, this method gets blocked until the search is completed or
	 * an exception is thrown. Otherwise, the result is returned through the
	 * listener.
	 * </p>
	 * @param queryString The query string.
	 * @param l The listener object.
	 * @return The result.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 */
	private Tweet[] searchTweets(Query query, final SearchDeviceListener l)
		throws IOException, LimitExceededException {
		updateAPIInfo();
		//
		HttpRequest req = createRequest(query.toString());
		//
		try {
			HttpResponse resp = req.send();
			//
			if (resp.getCode() == HttpConnection.HTTP_FORBIDDEN) {
				throw new InvalidQueryException(
					HttpResponseCodeInterpreter.getErrorMessage(resp));
			}
			//verify whether there is an error in the request.
			HttpResponseCodeInterpreter.perform(resp);
			//
			Parser parser = ParserFactory.getDefaultParser();
			SearchResultHandler handler = new SearchResultHandler();
			handler.setSearchDeviceListener(l);
			parser.parse(resp.getStream(), handler);
			//
			return handler.getParsedTweets();
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		} finally {
			req.close();
		}
	}
	
	/**
	 * <p>
	 * Get a Http request to the given query string.
	 * </p>
	 * @param queryStr The query string.
	 * @return The Http request object.
	 */
	private HttpRequest createRequest(String queryStr) {
		if (queryStr == null || (queryStr = queryStr.trim()).length() == 0) {
			throw new IllegalArgumentException(
				"Query String must not be empty/null.");
		}
		//
		if (!queryStr.startsWith("?")) {
			if (!queryStr.startsWith(TWITTER_QUERY_STRING_PREFIX)) {
				queryStr = '?' + TWITTER_QUERY_STRING_PREFIX + '&' + queryStr;
			} else {
				queryStr = '?' + queryStr;
			}
		}
		//
		return new HttpRequest(
			getURL(TWITTER_API_URL_SERVICE_SEARCH) + queryStr);
	}
	
	/**
	 * <p>
	 * Update some internal information regarding the API.
	 * </p>
	 */
	private void updateAPIInfo() {
		lastCallTime = System.currentTimeMillis();
		apiCallsCount++;
	}
}