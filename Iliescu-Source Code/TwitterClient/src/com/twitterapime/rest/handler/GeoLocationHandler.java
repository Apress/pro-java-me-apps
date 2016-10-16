/*
 * GeoLocationHandler.java
 * 18/08/2010
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
 * Handler class for parsing the XML geo location from Twitter API. 
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.4
 */
public final class GeoLocationHandler extends DefaultXMLHandler {
	/**
	 * <p>
	 * Populate the given hash according to the tags and their values
	 * </p>
	 * @param data Hash to be populated.
	 * @param path XML path.
	 * @param text Tag's text.
	 */
	public void populate(Hashtable data, String path, String text) {
		if (path.endsWith("/georss:point")) {
			String[] values = StringUtil.split(text, ' ');
			//
			data.put(MetadataSet.GEOLOCATION_LATITUDE, values[0]);
			data.put(MetadataSet.GEOLOCATION_LONGITUDE, values[1]);
		} else if (path.endsWith("/id")) {
			data.put(MetadataSet.GEOLOCATION_PLACE_ID, text);
		} else if (path.endsWith("/name")) {
			data.put(MetadataSet.GEOLOCATION_PLACE_NAME, text);
		} else if (path.endsWith("/full_name")) {
			data.put(MetadataSet.GEOLOCATION_PLACE_FULL_NAME, text);
		} else if (path.endsWith("/place_type")) {
			data.put(MetadataSet.GEOLOCATION_PLACE_TYPE, text);
		} else if (path.endsWith("/url")) {
			data.put(MetadataSet.GEOLOCATION_PLACE_URL, text);
		} else if (path.endsWith("/georss:polygon")) {
			data.put(
				MetadataSet.GEOLOCATION_POLYGON, StringUtil.split(text, ' '));
		} else if (path.endsWith("/country")) {
			data.put(MetadataSet.GEOLOCATION_COUNTRY, text);
		}
	}
}