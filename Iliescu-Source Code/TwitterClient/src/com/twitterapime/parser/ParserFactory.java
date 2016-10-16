/*
 * ParserFactory.java
 * 16/08/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.parser;

import com.twitterapime.platform.PlatformProvider;
import com.twitterapime.platform.PlatformProviderSelector;

/**
 * <p>
 * This factory class is responsible for creating new parser objects. 
 * </p>
 * <p>
 * The creation of a parser object is performed dynamically by looking up the
 * current platform provider from PlatformProviderSelector class. For each
 * supported platform, there is a specific implementation class.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.1
 * @since 1.0
 */
public final class ParserFactory {
	/**
	 * <p>
	 * Create the default parser instance according to the underlying platform.
	 * </p>
	 * @return Parser instance.
	 */
	public static Parser getDefaultParser() {
		final String JAVA_ME_PARSER_IMPL_CLASS =
			"impl.javame.com.twitterapime.parser.KXML2Parser";
		final String ANDROID_PARSER_IMPL_CLASS =
			"impl.android.com.twitterapime.parser.SAXParser";
		//
		final long PPID = PlatformProviderSelector.getCurrentProvider().getID();
		//
		//if JAVA ME PLATFORM
		if (PPID == PlatformProvider.PPID_JAVA_ME) {
			return newInstance(JAVA_ME_PARSER_IMPL_CLASS);
		} else if (PPID == PlatformProvider.PPID_ANDROID) {
			return newInstance(ANDROID_PARSER_IMPL_CLASS);
		} else {
			throw new IllegalArgumentException("Unknown platform ID: " + PPID);
		}
	}
	
	/**
	 * <p>
	 * Create an instance of the given class.
	 * </p>
	 * @param className Class name.
	 * @return Parser instance.
	 */
	private static Parser newInstance(String className) {
		try {
			return (Parser)Class.forName(className).newInstance();
		} catch (IllegalAccessException e) {
		} catch (InstantiationException e) {
		} catch (ClassNotFoundException e) {
		}
		//
		return null;
	}
	
	/**
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 */
	private ParserFactory() {
	}
}