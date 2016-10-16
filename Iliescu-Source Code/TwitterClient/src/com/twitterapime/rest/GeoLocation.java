/*
 * GeoLocation.java
 * 18/08/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.rest;

import java.util.Hashtable;

import com.twitterapime.model.DefaultEntity;
import com.twitterapime.model.MetadataSet;
import com.twitterapime.search.Tweet;

/**
 * <p>
 * This class defines an entity that represents a geographic location.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.4
 * @see Tweet
 */
public class GeoLocation extends DefaultEntity {
	/**
	 * <p>
	 * Create an instance of GeoLocation class.
	 * </p>
	 */
	public GeoLocation() {
	}

	/**
	 * <p>
	 * Create an instance of GeoLocation class.
	 * </p>
	 * @param data The initial attributes/values.
	 */
	public GeoLocation(Hashtable data) {
		super(data);
	}

	/**
	 * <p>
	 * Create an instance of GeoLocation class.
	 * </p>
	 * @param placeID Place ID.
	 * @throws IllegalArgumentException If placeID is null/empty.
	 */
	public GeoLocation(String placeID) {
		if (placeID == null || (placeID = placeID.trim()).length() == 0) {
			throw new IllegalArgumentException(
				"PlaceID must not be null/empty.");
		}
		//
		Hashtable t = new Hashtable();
		t.put(MetadataSet.GEOLOCATION_PLACE_ID, placeID);
		setData(t);
	}

	/**
	 * <p>
	 * Create an instance of GeoLocation class.
	 * </p>
	 * @param latitude Latitude. Must be between -90.0 and +90.0.
	 * @param longitude Longitude. Must be between -180.0 to +180.0.
	 * @throws IllegalArgumentException If latitude/longitude is null/empty.
	 */
	public GeoLocation(String latitude, String longitude) {
		if (latitude == null || (latitude = latitude.trim()).length() == 0) {
			throw new IllegalArgumentException(
				"Latitude must not be null/empty.");
		}
		if (longitude == null || (longitude = longitude.trim()).length() == 0) {
			throw new IllegalArgumentException(
				"Latitude must not be null/empty.");
		}
		//
		Hashtable t = new Hashtable();
		t.put(MetadataSet.GEOLOCATION_LATITUDE, latitude);
		t.put(MetadataSet.GEOLOCATION_LONGITUDE, longitude);
		setData(t);
	}
}