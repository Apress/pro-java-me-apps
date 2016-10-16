/*
 * FriendshipManager.java
 * 22/08/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.twitterapime.io.HttpConnection;
import com.twitterapime.io.HttpRequest;
import com.twitterapime.io.HttpResponse;
import com.twitterapime.io.HttpResponseCodeInterpreter;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.DefaultXMLHandler;
import com.twitterapime.parser.Parser;
import com.twitterapime.parser.ParserException;
import com.twitterapime.parser.ParserFactory;
import com.twitterapime.rest.handler.AccountHandler;
import com.twitterapime.search.InvalidQueryException;
import com.twitterapime.search.LimitExceededException;
import com.twitterapime.search.Query;
import com.twitterapime.search.QueryComposer;
import com.twitterapime.util.StringUtil;

/**
 * <p>
 * This class defines the methods responsible for managing friendship (friends 
 * and followers).
 * </p>
 * <p>
 * <pre>
 * Credential c = new Credential(...);
 * UserAccountManager uam = UserAccountManager.getInstance(c);
 * 
 * if (uam.verifyCredential()) {
 *   FriendshipManager fdr = FriendshipManager.getInstance(uam);
 *   String[] ids = fdr.getFriendsID(null);
 * }
 * 
 * ...
 * 
 * FriendshipManager fdr = FriendshipManager.getInstance();
 * String[] ids = fdr.getFriendsID(new UserAccount("twapime"), null);
 * </pre>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.4
 */
public final class FriendshipManager {
	/**
	 * <p>
	 * Hold all Twitter API URL services.
	 * </p>
	 */
	private static final Hashtable SERVICES_URL;

	/**
	 * <p>
	 * FriendshipManager pool used to cache instanced associated to user
	 * accounts.
	 * </p>
	 */
	private static Hashtable friendsMngrPool;

	/**
	 * <p>
	 * Single instance of this class.
	 * </p>
	 */
	private static FriendshipManager singleInstance;
	
	/**
	 * <p>
	 * Key for Twitter API URL service friends id.
	 * </p>
	 * @see FriendshipManager#setServiceURL(String, String)
	 * @see FriendshipManager#getFriendsID(Query)
	 */
	public static final String TWITTER_API_URL_SERVICE_FRIENDS_ID =
		"TWITTER_API_URL_SERVICE_FRIENDS_ID";
	
	/**
	 * <p>
	 * Key for Twitter API URL service followers id.
	 * </p>
	 * @see FriendshipManager#setServiceURL(String, String)
	 * @see FriendshipManager#getFollowersID(Query)
	 */
	public static final String TWITTER_API_URL_SERVICE_FOLLOWERS_ID =
		"TWITTER_API_URL_SERVICE_FOLLOWERS_ID";
	
	/**
	 * <p>
	 * Key for Twitter API URL service friendships create.
	 * </p>
	 * @see FriendshipManager#setServiceURL(String, String)
	 * @see FriendshipManager#follow(UserAccount)
	 */
	public static final String TWITTER_API_URL_SERVICE_FRIENDSHIPS_CREATE =
		"TWITTER_API_URL_SERVICE_FRIENDSHIPS_CREATE";
	
	/**
	 * <p>
	 * Key for Twitter API URL service friendships destroy.
	 * </p>
	 * @see FriendshipManager#setServiceURL(String, String)
	 * @see FriendshipManager#unfollow(UserAccount)
	 */
	public static final String TWITTER_API_URL_SERVICE_FRIENDSHIPS_DESTROY =
		"TWITTER_API_URL_SERVICE_FRIENDSHIPS_DESTROY";
	
	/**
	 * <p>
	 * Key for Twitter API URL service friendships exists.
	 * </p>
	 * @see FriendshipManager#setServiceURL(String, String)
	 * @see FriendshipManager#isFollowing(UserAccount)
	 */
	public static final String TWITTER_API_URL_SERVICE_FRIENDSHIPS_EXISTS =
		"TWITTER_API_URL_SERVICE_FRIENDSHIPS_EXISTS";
	
	/**
	 * <p>
	 * Key for Twitter API URL service blocks create.
	 * </p>
	 * @see FriendshipManager#setServiceURL(String, String)
	 * @see FriendshipManager#block(UserAccount)
	 */
	public static final String TWITTER_API_URL_SERVICE_BLOCKS_CREATE =
		"TWITTER_API_URL_SERVICE_BLOCKS_CREATE";
	
	/**
	 * <p>
	 * Key for Twitter API URL service blocks destroy.
	 * </p>
	 * @see FriendshipManager#setServiceURL(String, String)
	 * @see FriendshipManager#unblock(UserAccount)
	 */
	public static final String TWITTER_API_URL_SERVICE_BLOCKS_DESTROY =
		"TWITTER_API_URL_SERVICE_BLOCKS_DESTROY";
	
	/**
	 * <p>
	 * Key for Twitter API URL service friendships exists.
	 * </p>
	 * @see FriendshipManager#setServiceURL(String, String)
	 * @see FriendshipManager#isBlocking(UserAccount)
	 */
	public static final String TWITTER_API_URL_SERVICE_BLOCKS_EXISTS =
		"TWITTER_API_URL_SERVICE_BLOCKS_EXISTS";

	static {
		SERVICES_URL = new Hashtable(4);
		//
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_FRIENDS_ID,
			"http://api.twitter.com/1/friends/ids.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_FOLLOWERS_ID,
			"http://api.twitter.com/1/followers/ids.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_FRIENDSHIPS_CREATE,
			"http://api.twitter.com/1/friendships/create.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_FRIENDSHIPS_DESTROY,
			"http://api.twitter.com/1/friendships/destroy.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_FRIENDSHIPS_EXISTS,
			"http://api.twitter.com/1/friendships/exists.json");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_BLOCKS_CREATE,
			"http://api.twitter.com/1/blocks/create.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_BLOCKS_DESTROY,
			"http://api.twitter.com/1/blocks/destroy.xml");
		SERVICES_URL.put(
			TWITTER_API_URL_SERVICE_BLOCKS_EXISTS,
			"http://api.twitter.com/1/blocks/exists/");
	}
	
	/**
	 * <p>
	 * User account manager.
	 * </p>
	 */
	private UserAccountManager userAccountMngr;

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
		if (friendsMngrPool != null) {
			Enumeration keys = friendsMngrPool.keys();
			Object key;
			FriendshipManager value;
			//
			while (keys.hasMoreElements()) {
				key = keys.nextElement();
				value = (FriendshipManager)friendsMngrPool.get(key);
				//
				if (!value.userAccountMngr.isVerified()) {
					friendsMngrPool.remove(key);
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
	 * @see FriendshipManager#TWITTER_API_URL_SERVICE_FRIENDS_ID
	 * @see FriendshipManager#TWITTER_API_URL_SERVICE_FOLLOWERS_ID
	 * @see FriendshipManager#TWITTER_API_URL_SERVICE_BLOCKS_CREATE
	 * @see FriendshipManager#TWITTER_API_URL_SERVICE_BLOCKS_DESTROY
	 * @see FriendshipManager#TWITTER_API_URL_SERVICE_BLOCKS_EXISTS
	 * @see FriendshipManager#TWITTER_API_URL_SERVICE_FRIENDSHIPS_CREATE
	 * @see FriendshipManager#TWITTER_API_URL_SERVICE_FRIENDSHIPS_DESTROY
	 * @see FriendshipManager#TWITTER_API_URL_SERVICE_FRIENDSHIPS_EXISTS
	 */
	public void setServiceURL(String serviceKey, String url) {
		SERVICES_URL.put(serviceKey, url);
	}
	
	/**
	 * <p>
	 * Get an instance of FriendshipManager class and associate it to a given
	 * user account.
	 * </p>
	 * @param uam User account manager.
	 * @return FriendshipManager instance.
	 * @throws IllegalArgumentException If UserAccountManager is null.
	 * @throws SecurityException If UserAccountManager is not verified.
	 */
	public synchronized static FriendshipManager getInstance(
		UserAccountManager uam) {
		if (uam == null) {
			throw new IllegalArgumentException(
				"UserAccountManager must not be null.");
		}
		//
		if (!uam.isVerified()) {
			throw new SecurityException("User's credential must be verified.");
		}
		//
		if (friendsMngrPool == null) {
			friendsMngrPool = new Hashtable();
		}
		//
		FriendshipManager fsmr = (FriendshipManager)friendsMngrPool.get(uam);
		if (fsmr == null) {
			fsmr = new FriendshipManager(uam);
			friendsMngrPool.put(uam, fsmr);
		}
		//
		return fsmr;
	}
	
	/**
	 * <p>
	 * Get a single instance of FriendshipManager class, which is NOT associated
	 * to any user account.
	 * </p>
	 * @return FriendshipManager single instance.
	 */
	public synchronized static FriendshipManager getInstance() {
		if (singleInstance == null) {
			singleInstance = new FriendshipManager();
		}
		//
		return singleInstance;
	}
	
	/**
	 * <p>
	 * Create an instance of FriendshipManager class.
	 * </p>
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 */
	private FriendshipManager() {
	}
	
	/**
	 * <p>
	 * Create an instance of FriendshipManager class.
	 * </p>
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 * @param uam User account manager.
	 */
	private FriendshipManager(UserAccountManager uam) {
		userAccountMngr = uam;
	}
	
	/**
	 * <p>
	 * Get the friends ID list of verified user.
	 * </p>
	 * <p>
	 * As the number of friends of a given user can be very large, you have to
	 * specify a max count. Passing <code>null</code> will return all of them.
	 * But be aware it may take a little awhile and consume a lot of memory.
	 * </p>
	 * @param query Max count of IDs to be returned. Use
	 *              {@link QueryComposer#count(int)} only.
	 * @return Friends id.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 * @throws SecurityException If user account manager is not informed.
	 * @see UserAccountManager#getUserAccount(UserAccount)
	 */
	public String[] getFriendsID(Query query) throws IOException,
		LimitExceededException {
		checkUserAuth();
		//
		return retrieveIDs(
			getURL(TWITTER_API_URL_SERVICE_FRIENDS_ID),
			userAccountMngr.getUserAccount(),
			query);
	}

	/**
	 * <p>
	 * Get the friends ID list of a given user.
	 * </p>
	 * <p>
	 * As the number of friends of a given user can be very large, you have to
	 * specify a max count. Passing <code>null</code> will return all of them.
	 * But be aware it may take a little awhile and consume a lot of memory.
	 * </p>
	 * @param user User.
	 * @param query Max count of IDs to be returned. Use
	 *              {@link QueryComposer#count(int)} only.
	 * @return Friends id.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 * @throws IllegalArgumentException If user is not informed.
	 * @throws SecurityException Given user is protected.
	 * @see UserAccountManager#getUserAccount(UserAccount)
	 */
	public String[] getFriendsID(UserAccount user, Query query)
		throws IOException, LimitExceededException {
		return retrieveIDs(
			getURL(TWITTER_API_URL_SERVICE_FRIENDS_ID), user, query);
	}

	/**
	 * <p>
	 * Get the followers ID list of verified user.
	 * </p>
	 * <p>
	 * As the number of followers of a given user can be very large, you have to
	 * specify a max count. Passing <code>null</code> will return all of them.
	 * But be aware it may take a little awhile and consume a lot of memory.
	 * </p>
	 * @param query Max count of IDs to be returned. Use
	 *              {@link QueryComposer#count(int)} only.
	 * @return Friends id.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 * @throws SecurityException If user account manager is not informed.
	 * @see UserAccountManager#getUserAccount(UserAccount)
	 */
	public String[] getFollowersID(Query query) throws IOException,
		LimitExceededException {
		checkUserAuth();
		//
		return retrieveIDs(
			getURL(TWITTER_API_URL_SERVICE_FOLLOWERS_ID),
			userAccountMngr.getUserAccount(),
			query);
	}
	
	/**
	 * <p>
	 * Get the followers ID list of a given user.
	 * </p>
	 * <p>
	 * As the number of followers of a given user can be very large, you have to
	 * specify a max count. Passing <code>null</code> will return all of them.
	 * But be aware it may take a little awhile and consume a lot of memory.
	 * </p>
	 * @param user User.
	 * @param query Max count of IDs to be returned. Use
	 *              {@link QueryComposer#count(int)} only.
	 * @return Friends id.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 * @throws IllegalArgumentException If user is not informed.
	 * @throws SecurityException Given user is protected.
	 * @see UserAccountManager#getUserAccount(UserAccount)
	 */
	public String[] getFollowersID(UserAccount user, Query query)
		throws IOException,	LimitExceededException {
		return retrieveIDs(
			getURL(TWITTER_API_URL_SERVICE_FOLLOWERS_ID), user, query);
	}
	
	/**
	 * <p>
	 * Allows the authenticating user to follow the user specified in the given
	 * UserAccount object.
	 * </p>
	 * @param ua UserAccount object containing the user name or ID.
	 * @return Info from followed user.
	 * @throws IOException If an I/O error occurs.
	 * @throws InvalidQueryException User already followed or does not exist.
	 * @throws SecurityException If it is not authenticated.
	 * @throws LimitExceededException If limit has been hit.
	 */
	public UserAccount follow(UserAccount ua) throws IOException,
		LimitExceededException {
		return manageFriendship(TWITTER_API_URL_SERVICE_FRIENDSHIPS_CREATE, ua);
	}
	
	/**
	 * <p>
	 * Allows the authenticating user to unfollow the user specified in the
	 * given UserAccount object.
	 * </p>
	 * @param ua UserAccount object containing the user name or ID.
	 * @return Info from unfollowed user.
	 * @throws IOException If an I/O error occurs.
	 * @throws InvalidQueryException User already unfollowed or does not exist.
	 * @throws SecurityException If it is not authenticated.
	 * @throws LimitExceededException If limit has been hit.
	 */
	public UserAccount unfollow(UserAccount ua) throws IOException,
		LimitExceededException {
		return manageFriendship(
			TWITTER_API_URL_SERVICE_FRIENDSHIPS_DESTROY, ua);
	}
	
	/**
	 * <p>
	 * Verify whether the authenticating user is following the user specified in
	 * the given UserAccount object.
	 * </p>
	 * @param ua UserAccount object containing the user name or ID.
	 * @return Following (true).
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws InvalidQueryException If user does not exist or is protected.
	 * @throws SecurityException If it is not authenticated.
	 */
	public boolean isFollowing(UserAccount ua) throws IOException,
		LimitExceededException {
		if (ua == null) {
			throw new IllegalArgumentException(
				"UserAccount object must not me null.");
		}
		ua.validateUserNameOrID();
		//
		checkUserAuth();
		//
		Credential c = userAccountMngr.getCredential();
		final String qryStr =
			"?user_a=" + c.getString(MetadataSet.CREDENTIAL_USERNAME) +
			"&user_b=" + ua.getUserNameOrID();
		//
		HttpRequest req = userAccountMngr.createRequest(
			getURL(TWITTER_API_URL_SERVICE_FRIENDSHIPS_EXISTS) + qryStr);
		//
		try {
			HttpResponse resp = req.send();
			//
			HttpResponseCodeInterpreter.perform(resp);
			//
			return resp.getBodyContent().toLowerCase().equals("true");
		} finally {
			req.close();
		}
	}
	
	/**
	 * <p>
	 * Allows the authenticating user to block the user specified in the given
	 * UserAccount object.
	 * </p>
	 * @param ua UserAccount object containing the user name or ID.
	 * @return Info from blocked user.
	 * @throws IOException If an I/O error occurs.
	 * @throws InvalidQueryException User does not exist.
	 * @throws SecurityException If it is not authenticated.
	 * @throws LimitExceededException If limit has been hit.
	 */
	public UserAccount block(UserAccount ua) throws IOException,
		LimitExceededException {
		return manageFriendship(TWITTER_API_URL_SERVICE_BLOCKS_CREATE, ua);
	}
	
	/**
	 * <p>
	 * Allows the authenticating user to unblock the user specified in the
	 * given UserAccount object.
	 * </p>
	 * @param ua UserAccount object containing the user name or ID.
	 * @return Info from unblocked user.
	 * @throws IOException If an I/O error occurs.
	 * @throws InvalidQueryException User does not exist.
	 * @throws SecurityException If it is not authenticated.
	 * @throws LimitExceededException If limit has been hit.
	 */
	public UserAccount unblock(UserAccount ua) throws IOException,
		LimitExceededException {
		return manageFriendship(TWITTER_API_URL_SERVICE_BLOCKS_DESTROY, ua);
	}
	
	/**
	 * <p>
	 * Verify whether the authenticating user is blocking the user specified in
	 * the given UserAccount object.
	 * </p>
	 * @param ua UserAccount object containing the user name or ID.
	 * @return Blocking (true).
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If limit has been hit.
	 * @throws InvalidQueryException If user does not exist or is protected.
	 * @throws SecurityException If it is not authenticated.
	 */
	public boolean isBlocking(UserAccount ua) throws IOException,
		LimitExceededException {
		if (ua == null) {
			throw new IllegalArgumentException(
				"UserAccount object must not me null.");
		}
		ua.validateUserNameOrID();
		//
		checkUserAuth();
		//
		HttpRequest req = userAccountMngr.createRequest(
			getURL(TWITTER_API_URL_SERVICE_BLOCKS_EXISTS) +
			ua.getUserNameOrID() +
			".xml");
		//
		try {
			HttpResponse resp = req.send();
			if (resp.getCode() == HttpConnection.HTTP_NOT_FOUND) {
				return false; //not blocked!
			}
			//
			HttpResponseCodeInterpreter.perform(resp);
			//
			return true;
		} finally {
			req.close();
		}
	}
	
	/**
	 * <p>
	 * Perform an operation on authenticating user regarding the friendship
	 * management, e.g., follow, unfollow, block or unblock users.
	 * </p>
	 * @param servURLKey Service URL Key.
	 * @param ua UserAccount object containing the user name or ID.
	 * @throws IOException If an I/O error occurs.
	 * @throws InvalidQueryException User already affected by the action or does
	 *         not exist.
	 * @throws SecurityException If the user is not authenticated.
	 * @throws LimitExceededException If limit has been hit.
	 */
	private UserAccount manageFriendship(String servURLKey, UserAccount ua)
		throws IOException, LimitExceededException {
		if (ua == null) {
			throw new IllegalArgumentException(
				"UserAccount object must not me null.");
		}
		ua.validateUserNameOrID();
		//
		checkUserAuth();
		//
		String[] pv = ua.getUserNameOrIDParamValue();
		HttpRequest req = userAccountMngr.createRequest(getURL(servURLKey));
		req.setMethod(HttpConnection.POST);
		req.setBodyParameter(pv[0], pv[1]);
		//
		try {
			HttpResponse resp = req.send();
			//
			if (resp.getCode() == HttpConnection.HTTP_FORBIDDEN) {
				//already following/blocking.
				throw new InvalidQueryException(
					HttpResponseCodeInterpreter.getErrorMessage(resp));
			}
			//
			HttpResponseCodeInterpreter.perform(resp);
			//
			Parser parser = ParserFactory.getDefaultParser();
			AccountHandler handler = new AccountHandler();
			parser.parse(resp.getStream(), handler);
			//
			return handler.getParsedUserAccount();
		} catch (ParserException e) {
			throw new IOException(e.getMessage());
		} finally {
			req.close();
		}
	}
	
	/**
	 * <p>
	 * Retrieve friends/followers IDs of a given user.
	 * </p>
	 * @param url Service URL.
	 * @param user User.
	 * @param query Max count of IDs to be returned.
	 * @return IDs.
	 * @throws IOException If an I/O error occurs.
	 * @throws LimitExceededException If the limit of access is exceeded.
	 * @throws IllegalArgumentException If user is not informed.
	 * @throws SecurityException Given user is protected.
	 */
	private String[] retrieveIDs(String url, UserAccount user, Query query)
		throws IOException, LimitExceededException {
		if (user == null) {
			throw new IllegalArgumentException("User must not be null.");
		}
		user.validateUserNameOrID();
		//
		String[] pv = user.getUserNameOrIDParamValue();
		url += "?" + pv[0] + "=" + pv[1];
		//
		long maxCount = Long.MAX_VALUE;
		if (query != null) {
			maxCount =
				Long.parseLong(StringUtil.split(query.toString(), '=')[1]);
		}
		//
		long loadedCount = 0;
		long cursorNextIdx = -1;
		Vector idsList = new Vector(20);
		IDsHandler handler = new IDsHandler(maxCount);
		Parser parser = ParserFactory.getDefaultParser();
		//
		do {
			HttpRequest req;
			if (userAccountMngr != null) {
				req = userAccountMngr.createRequest(
					url + "&cursor=" + cursorNextIdx);
			} else {
				req = new HttpRequest(url + "&cursor=" + cursorNextIdx);
			}
			//
			try {
				HttpResponse resp = req.send();
				//
				HttpResponseCodeInterpreter.perform(resp);
				//
				parser.parse(resp.getStream(), handler);
				//
				loadedCount += copyVector(idsList, handler.getIDsList());
				cursorNextIdx = handler.getCursorNextIndex();
				//
				handler.clear();
			} catch (ParserException e) {
				throw new IOException(e.getMessage());
			} finally {
				req.close();
			}
		} while (loadedCount < maxCount && cursorNextIdx != 0);
		//
		String[] ids = new String[idsList.size()];
		idsList.copyInto(ids);
		//
		return ids;
	}
	
	/**
	 * <p>
	 * Copy all elements from a vector to another one. 
	 * </p>
	 * @param to Vector to.
	 * @param from Vector from.
	 * @return Count of items copied.
	 */
	private int copyVector(Vector to, Vector from) {
		int size = from.size();
		for (int i = 0; i < size; i++) {
			to.addElement(from.elementAt(i));
		}
		//
		return size;
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
	
	/**
	 * <p>
	 * XML handlers for parsing a list of IDs.
	 * </p>
	 * 
	 * @author Ernandes Mourao Junior (ernandes@gmail.com)
	 * @version 1.0
	 * @since 1.4
	 */
	private static class IDsHandler extends DefaultXMLHandler {
		/**
		 * <p>
		 * Max count of IDs.
		 * </p>
		 */
		private final long maxCount;
		
		/**
		 * <p>
		 * Count of IDs read.
		 * </p>
		 */
		private long count;

		/**
		 * <p>
		 * Cursor next index.
		 * </p>
		 */
		private long cursorNextIdx;
		
		/**
		 * <p>
		 * IDs list.
		 * </p>
		 */
		private Vector idsList = new Vector(20);
		
		/**
		 * <p>
		 * Create an instance of IDsHandler class.
		 * </p>
		 * @param maxCount Max count of IDs.
		 */
		public IDsHandler(long maxCount) {
			this.maxCount = maxCount;
		}
		
		/**
		 * @see com.twitterapime.parser.DefaultXMLHandler#text(java.lang.String)
		 */
		public void text(String text) throws ParserException {
			text = text.trim();
			//
			if (xmlPath.equals("/id_list/ids/id")) {
				if (count < maxCount) {
					idsList.addElement(text);
					count++;
				}
			} else if (xmlPath.equals("/id_list/next_cursor")) {
				cursorNextIdx = Long.parseLong(text);
			}
		}
		
		/**
		 * <p>
		 * Get IDs list.
		 * </p>
		 * @return List.
		 */
		public Vector getIDsList() {
			return idsList;
		}
		
		/**
		 * <p>
		 * Cursor next index.
		 * </p>
		 * @return Index.
		 */
		public long getCursorNextIndex() {
			return cursorNextIdx;
		}
		
		/**
		 * <p>
		 * Clear internal state of handler.
		 * </p>
		 */
		public void clear() {
			idsList.removeAllElements();
			cursorNextIdx = 0;
		}
	}
}