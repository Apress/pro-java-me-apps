/*
 * StringUtil.java
 * 28/11/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.util;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Vector;

/**
 * <p>
 * This class provides some useful methods to work with strings.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.2
 * @since 1.1
 */
public final class StringUtil {
	/**
	 * <p>
	 * Months abbreviations.
	 * </p>
	 */
	private static final String[] MONTHS_ABBREVIATION =
		new String[] {
			"jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep",
			"oct", "nov", "dec" };
	
	/**
	 * <p>
	 * Convert a given date according to a format used by Twitter to long. 
	 * </p>
	 * <p>
	 * Formats:
	 * <lu>
	 * <li>2009-11-28 21:43:12</li>
	 * <li>2009-12-01T01:25:00+00:00</li>
	 * <li>Sat Nov 07 21:30:03 +0000 2009</li>
	 * </lu>
	 * </p>
	 * @param date Tweet date value.
	 * @return Long date.
	 * @throws IllegalArgumentException If date is null/empty.
	 */
	public static long convertTweetDateToLong(String date) {
		if (date == null || (date = date.trim()).length() == 0) {
			throw new IllegalArgumentException("Date must not be null/empty.");
		}
		//
		char fc = date.charAt(0);
		//)
		if (Character.isDigit(fc)) { //is date format 1?
			date = formatTweetDate1(date);
		} else { //date format 2.
			date = formatTweetDate2(date);
		}
		//
		String[] parts = split(date, ' ');
		String[] dparts = split(parts[0], '-');
		String[] tparts = split(parts[1], ':');
		//
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, Integer.parseInt(dparts[0]));
		c.set(Calendar.MONTH, Integer.parseInt(dparts[1]) -1);
		c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dparts[2]));
		c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tparts[0]));
		c.set(Calendar.MINUTE, Integer.parseInt(tparts[1]));
		c.set(Calendar.SECOND, Integer.parseInt(tparts[2]));
		c.set(Calendar.MILLISECOND, 0);
		//
		String dim = Long.toString(c.getTime().getTime());
		dim = dim.substring(0, dim.length() -3);
		//
		return Long.parseLong(dim); 
	}
	
	/**
     * <p>
     * Split a string based on a given delimiter.
     * </p>
     * @param str String.
     * @param delimiter Delimiter.
     * @return String tokens.
     * @throws IllegalArgumentException If str is null.
     */
    public static final String[] split(String str, char delimiter) {
    	if (str == null) {
    		throw new IllegalArgumentException("Str must not be null.");
    	}
    	//
        Vector v = new Vector();
        int start = 0;
        int iof;
        //
        while ((iof = str.indexOf(delimiter, start)) != -1) {
            v.addElement(str.substring(start, iof).trim());
            start = iof +1;
        }
        //
        v.addElement(str.substring(start, str.length()).trim());
        String[] codes = new String[v.size()];
        v.copyInto(codes);
        //
        return codes;
    }
	
	/**
	 * <p>
	 * Format the Tweet ID returned by Twitter Search API.
	 * </p>
	 * @param id Tweet ID.
	 * @return ID (e.g. 18738430989).
	 * @throws IllegalArgumentException If ID is null.
	 */
	public static String formatTweetID(String id) {
    	if (id == null) {
    		throw new IllegalArgumentException("ID must not be null.");
    	}
    	//
    	int idx = id.lastIndexOf(':');
    	//
    	if (idx != -1) {
    		return id.substring(idx +1, id.length());
    	} else {
    		return id;
    	}
	}
	
	/**
	 * <p>
	 * Remove any tag occurrence from the given string.
	 * </p>
	 * @param str String to be parsed.
	 * @return String with no tags.
	 * @throws IllegalArgumentException If str is null.
	 */
	public static String removeTags(String str) {
		if (str == null) {
			throw new IllegalArgumentException("Str must not be null.");
		}
		//
		StringBuffer out = new StringBuffer();
		char cs[] = str.toCharArray();
		boolean tagFound = false;
		int i1 = 0;
		int l = 0;
		//
		for (int i = 0; i < cs.length; i++) {
			if (cs[i] == '<' && !tagFound) {
				out.append(cs, i1, l);
				//
				i1 = i;
				l = 0;
				tagFound = true;
				l++;
			} else if (cs[i] == '>' && tagFound) {
				i1 = i +1;
				l = 0;
				tagFound = false;
			} else {
				l++;
			}
		}
		if (l > 0) {
			out.append(cs, i1, l);
		}
		//
		return out.toString().trim();
	}
	
	/**
	 * <p>
	 * Split the author's username and name from the given string, according to
	 * the format: "<username> (<name>)".
	 * </p>
	 * @param name The name.
	 * @return The username [0] and full name [1].
	 * @throws IllegalArgumentException If str is null.
	 */
	public static String[] splitTweetAuthorNames(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Name must not be null.");
		}
		//
		String[] names = new String[2];
		name = name.trim();
		final int i = name.indexOf(' ');
		//
		if (i == -1) {
			names[0] = name;
			names[1] = name;
		} else {
			names[0] = name.substring(0, i);
			names[1] = name.substring(i +2, name.length() -1);
		}
		//
		return names;
	}
	
    /**
     * <p>
     * Return a padded string with leading zeros. 
     * </p>
     * @param n Number.
     * @param len String length.
     * @return Padded string.
     */
    public static String zeroPad(int n, int len) {
    	String s = n + "";
    	//
    	for (int i = len - s.length(); i > 0; i--) {
            s = '0' + s;
    	}
    	//
        return s;
    }
    
    /**
	 * <p>
	 * Encode a given string. If encode type is not informed, UTF-8 is
	 * considered.
	 * </p>
	 * @param s String to encode.
	 * @param enc Encode.
	 * @return Encoded string.
	 * @throws IllegalArgumentException If string is empty or null.
	 */
	public static String encode(String s, String enc) {
		if (s == null) {
			throw new IllegalArgumentException("String must not be null");
		}
		if (enc == null) {
			enc = "UTF-8";
		}
		//
		ByteArrayInputStream bIn;
		//
		try {
			bIn = new ByteArrayInputStream(s.getBytes(enc));
		} catch (UnsupportedEncodingException e) {
			bIn = new ByteArrayInputStream(s.getBytes());
		}
		//
		int c = bIn.read();
		StringBuffer ret = new StringBuffer();
		//
		while (c >= 0) {
			if ((c >= 'a' && c <= 'z')
					|| (c >= 'A' && c <= 'Z')
					|| (c >= '0' && c <= '9')
					|| c == '.'
					|| c == '-'
					|| c == '*'
					|| c == '_') {
				ret.append((char) c);
			} else if (c == ' ') {
				ret.append("%20");
			} else {
				if (c < 128) {
					ret.append(getHexChar(c));
				} else if (c < 224) {
					ret.append(getHexChar(c));
					ret.append(getHexChar(bIn.read()));
				} else if (c < 240) {
					ret.append(getHexChar(c));
					ret.append(getHexChar(bIn.read()));
					ret.append(getHexChar(bIn.read()));
				}
			}
			//
			c = bIn.read();
		}
		//
		return ret.toString();
	}
	
	/**
	 * <p>
	 * Verify whether the given string is null or empty.
	 * </p>
	 * @param str The string.
	 * @return true null/empty.
	 */
	public static final boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}
	
	/**
	 * <p>
	 * Private constructor to avoid object instantiation.
	 * </p>
	 */
	private StringUtil() {
	}

	/**
	 * <p>
	 * Convert a given date according to a format used by Twitter to String. For
	 * instance, "2009-11-28 21:43:12" or "2009-11-28T21:43:12+00:00" to
	 * "2009-11-28 21:43:12".
	 * </p>
	 * @param date Tweet date value.
	 * @return Date.
	 */
	private static String formatTweetDate1(String date) {
		//2009-11-28 21:43:12
		//2009-12-01T01:25:00+00:00
		return date.substring(0, 10) + ' ' + date.substring(11, 19);
	}

	/**
	 * <p>
	 * Convert a given date according to a format used by Twitter to String. For
	 * instance, "Sat Nov 07 21:30:03 +0000 2009" to "2009-11-07 21:30:03".
	 * </p>
	 * @param date Tweet date value.
	 * @return Date.
	 */
	private static String formatTweetDate2(String date) {
		//Sat Nov 07 21:30:03 +0000 2009
		//
		String[] dtps = split(date, ' ');
		String mon = dtps[1].toLowerCase();
		//
		for (int i = 0; i < MONTHS_ABBREVIATION.length; i++) {
			if (mon.equals(MONTHS_ABBREVIATION[i])) {
				return dtps[5] + '-' + zeroPad((i +1), 2) + '-' + dtps[2] +
					' ' + dtps[3];
			}
		}
		//
		throw new IllegalArgumentException("Invalid date format: " + date);
	}

	/**
	 * <p>
	 * Get a hex value of a char.
	 * </p>
	 * @param c Char.
	 */
	private static String getHexChar(int c) {
		return (c < 16 ? "%0" : "%") + Integer.toHexString(c).toUpperCase();
	}
}