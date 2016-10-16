/*
 * UserAccountHandler.java
 * 14/11/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest.handler;

import java.util.Hashtable;

import com.twitterapime.model.MetadataSet;
import com.twitterapime.parser.DefaultXMLHandler;
import com.twitterapime.util.StringUtil;

/**
 * <p>
 * Handler class for parsing the XML user account from Twitter API. 
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.2
 * @since 1.1
 */
public final class UserAccountHandler extends DefaultXMLHandler {
	/**
	 * <p>
	 * Populate the given hash according to the tags and their values
	 * </p>
	 * @param data Hash to be populated.
	 * @param path XML path.
	 * @param text Tag's text.
	 */
	public void populate(Hashtable data, String path, String text) {
		if (path.endsWith("/id")) {
			data.put(MetadataSet.USERACCOUNT_ID, text);
		} else if (path.endsWith("/name")) {
			data.put(MetadataSet.USERACCOUNT_NAME, text);
		} else if (path.endsWith("/screen_name")) {
			data.put(MetadataSet.USERACCOUNT_USER_NAME, text);
		} else if (path.endsWith("/location")) {
			data.put(MetadataSet.USERACCOUNT_LOCATION, text);
		} else if (path.endsWith("/description")) {
			data.put(MetadataSet.USERACCOUNT_DESCRIPTION, text);
		} else if (path.endsWith("/profile_image_url")) {
			data.put(MetadataSet.USERACCOUNT_PICTURE_URI, text);
		} else if (path.endsWith("/url")) {
			data.put(MetadataSet.USERACCOUNT_URL, text);
		} else if (path.endsWith("/protected")) {
			data.put(MetadataSet.USERACCOUNT_PROTECTED, text);
		} else if (path.endsWith("/followers_count")) {
			data.put(MetadataSet.USERACCOUNT_FOLLOWERS_COUNT, text);
		} else if (path.endsWith("/profile_background_color")) {
			data.put(MetadataSet.USERACCOUNT_PROFILE_BACKGROUND_COLOR, text);
		} else if (path.endsWith("/profile_text_color")){
			data.put(MetadataSet.USERACCOUNT_PROFILE_TEXT_COLOR, text);
		} else if (path.endsWith("/profile_link_color")){
			data.put(MetadataSet.USERACCOUNT_PROFILE_LINK_COLOR, text);
		} else if (path.endsWith("/friends_count")) {
			data.put(MetadataSet.USERACCOUNT_FRIENDS_COUNT, text);
		} else if (path.endsWith("/created_at")) {
			data.put(
				MetadataSet.USERACCOUNT_CREATE_DATE,
				"" + StringUtil.convertTweetDateToLong(text));
		} else if (path.endsWith("/favourites_count")) {
			data.put(MetadataSet.USERACCOUNT_FAVOURITES_COUNT, text);
		} else if (path.endsWith("/utc_offset")) {
			data.put(MetadataSet.USERACCOUNT_UTC_OFFSET, text);
		} else if (path.endsWith("/time_zone")) {
			data.put(MetadataSet.USERACCOUNT_TIME_ZONE, text);
		} else if (path.endsWith("/profile_background_image_url")){
			data.put(MetadataSet.USERACCOUNT_PROFILE_BACKGROUND_IMAGE_URI,text);
		} else if (path.endsWith("/statuses_count")) {
			data.put(MetadataSet.USERACCOUNT_TWEETS_COUNT, text);
		} else if (path.endsWith("/notifications")) {
			data.put(MetadataSet.USERACCOUNT_NOTIFICATIONS, text);
		} else if (path.endsWith("/verified")) {
			data.put(MetadataSet.USERACCOUNT_VERIFIED, text);
		} else if (path.endsWith("/geo_enabled")) {
			data.put(MetadataSet.USERACCOUNT_GEO_ENABLED, text);
		}
	}
}