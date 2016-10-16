/*
 * PlatformProviderSelector.java
 * 16/08/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.platform;

import java.util.Vector;

/**
 * <p>
 * This class is responsible for managing the platform providers available in
 * this API, besides defining which one is current one. The definition of which
 * platform provide is the selected will indicate which underlying
 * implementation will provide all the services provided by this API.
 * </p>
 * <p>
 * Platform Provider selector always defines a given platform provider by
 * default, however, the developer can select another one.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.1
 * @since 1.0
 * @see PlatformProvider
 */
public final class PlatformProviderSelector {
	//#ifdef PP_JAVA_ME
	//#define PP_JAVA_ME = 1
	//#else
	//#define PP_JAVA_ME = 0
	//#endif

	//#ifdef PP_ANDROID
	//#define PP_ANDROID = 1
	//#else
	//#define PP_ANDROID = 0
	//#endif

	//#if PP_JAVA_ME == 0 && PP_ANDROID == 0
	//#define PP_ALL = 1
	//#else
	//#define PP_ALL = 0
	//#endif

	//#if PP_JAVA_ME == 1 || PP_ALL == 1
	/**
	 * <p>
	 * Platform provider that represents Java Micro Edition platform.
	 * </p>
	 */
	private static final PlatformProvider javameProvider =
		new PlatformProvider(
			PlatformProvider.PPID_JAVA_ME, PlatformProvider.PPNM_JAVA_ME);
	//#endif

	//#if PP_ANDROID == 1 || PP_ALL == 1
	/**
	 * <p>
	 * Platform provider that represents Android platform.
	 * </p>
	 * <p>
	 * Coming soon!
	 * </p>
	 */
	private static final PlatformProvider androidProvider =
		new PlatformProvider(
			PlatformProvider.PPID_ANDROID, PlatformProvider.PPNM_ANDROID);
	//#endif
	
	/**
	 * <p>
	 * Hold the default platform provider object defined by Platform Provider
	 * selector.
	 * </p>
	 */
	//#if PP_JAVA_ME == 1 || PP_ALL == 1
	private static final PlatformProvider defaultPlatform = javameProvider;
	//#elif PP_ANDROID == 1
//@	private static final PlatformProvider defaultPlatform = androidProvider;
	//#endif

	/**
	 * <p>
	 * Hold the current platform provider object defined by Platform Provider
	 * selector/developer.
	 * </p>
	 */
	private static PlatformProvider currentPlatform;
	
	static {
		select(defaultPlatform);
	}

	/**
	 * <p>
	 * Get all the platform providers supported by this API.
	 * </p>
	 * @return Array with platform providers.
	 */
	public static PlatformProvider[] getAvailableProviders() {
		Vector v = new Vector(2);
		//#if PP_JAVA_ME == 1 || PP_ALL == 1
		v.addElement(javameProvider);
		//#endif
		//#if PP_ANDROID == 1 || PP_ALL == 1
		v.addElement(androidProvider);
		//#endif
		//
		PlatformProvider[] plats = new PlatformProvider[v.size()];
		v.copyInto(plats);
		//
		return plats;
	}

	/**
	 * <p>
	 * Get the current platform provider which is providing all the services
	 * of this API.
	 * </p>
	 * @return The current platform provider object.
	 */
	public static PlatformProvider getCurrentProvider() {
		return currentPlatform;
	}

	/**
	 * <p>
	 * Get the default platform provider suggested by Platform Provider
	 * selector.
	 * </p>
	 * @return The default platform provider object.
	 */
	public static PlatformProvider getDefaultProvider() {
		return defaultPlatform;
	}

	/**
	 * <p>
	 * Set the given platform provider object as the current platform that will
	 * provide the services available in this API.
	 * </p>
	 * @param pp The platform provider object.
	 * @throws IllegalArgumentException If pp is null.
	 */
	public static void select(PlatformProvider pp) {
		if (pp == null) {
			throw new IllegalArgumentException("Platform must not be null.");
		}
		//
		currentPlatform = pp;
	}

	/**
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 */
	private PlatformProviderSelector() {
	}
}