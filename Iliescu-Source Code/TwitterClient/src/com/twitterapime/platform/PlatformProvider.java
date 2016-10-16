/*
 * PlatformProvider.java
 * 16/08/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.platform;

/**
 * <p>
 * This class defines a platform provider which represents a given platform
 * supported by Twitter API ME.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.1
 * @since 1.0
 * @see PlatformProviderSelector
 */
public final class PlatformProvider {
	/**
	 * <p>
	 * Constant that identifies the platform provider ID that represents the
	 * Java Micro Edition platform. 
	 * </p>
	 */
	public static final int PPID_JAVA_ME = 1;
	
	/**
	 * <p>
	 * Constant that identifies the platform provider ID that represents the
	 * Android platform.
	 * </p>
	 * <p>
	 * Coming soon!
	 * </p>
	 */
	public static final int PPID_ANDROID = 2;

	/**
	 * <p>
	 * Constant that identifies the platform provider name that represents the
	 * Java Micro Edition platform. 
	 * </p>
	 */
	static final String PPNM_JAVA_ME = "Java Micro Edition platform";
	
	/**
	 * <p>
	 * Constant that identifies the platform provider name that represents the
	 * Android platform.
	 * </p>
	 * <p>
	 * Coming soon!
	 * </p>
	 */
	static final String PPNM_ANDROID = "Android platform";

	/**
	 * <p>
	 * Platform provider ID.
	 * </p>
	 */
	private int id;

	/**
	 * <p>
	 * Platform provider name.
	 * </p>
	 */
	private String name;

	/**
	 * <p>
	 * Create an instance of PlatformProvider class.
	 * </p>
	 * @param id Platform ID.
	 * @param name Platform name.
	 */
	PlatformProvider(int id, String name) {
		this.id = id;
		this.name = name;
	}

	/**
	 * <p>
	 * Get the platform provider ID.
	 * </p>
	 * @return The platform ID.
	 */
	public long getID() {
		return id;
	}
	
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (o == null || !(o instanceof PlatformProvider)) {
			return false;
		} else {
			return id == ((PlatformProvider)o).id;
		}
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return id;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return name;
	}
}