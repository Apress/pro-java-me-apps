/*
 * HttpResponse.java
 * 05/06/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * <p>
 * This class implements a Http response.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.3
 */
public final class HttpResponse {
	/**
	 * <p>
	 * Response code.
	 * </p>
	 */
	private int code;
	
	/**
	 * <p>
	 * Body content.
	 * </p>
	 */
	private String body;
	
	/**
	 * <p>
	 * Response stream.
	 * </p>
	 */
	private InputStream stream;

	/**
	 * <p>
	 * Create an instance of HttpResponse class.
	 * </p>
	 * @param conn Http connection.
	 * @throws IOException If an I/O error occurs.
	 */
	HttpResponse(HttpConnection conn) throws IOException {
		code = conn.getResponseCode();
		stream = conn.openInputStream();
	}

	/**
	 * <p>
	 * Returns whether the request was OK.
	 * </p>
	 * @return OK (true).
	 */
	public boolean wasSuccessful() {
		return code >= 200 && code < 400;
	}

	/**
	 * <p>
	 * Get the response's body content.
	 * </p>
	 * @return Content.
	 * @throws IOException If an I/O error occurs.
	 */
	public String getBodyContent() throws IOException {
		return body != null ? body : (body = parseBody(stream));
	}
	
	/**
	 * <p>
	 * Get the response's stream.
	 * </p>
	 * @return Stream.
	 */
	public InputStream getStream() {
		return stream;
	}

	/**
	 * <p>
	 * Get the response code.
	 * </p>
	 * @return Code.
	 */
	public int getCode() {
		return code;
	}

	/**
	 * <p>
	 * Parse the response's body content.
	 * </p>
	 * @param in Response's stream.
	 * @return Content.
	 * @throws IOException If an I/O error occurs.
	 */
	private String parseBody(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
		byte[] buffer = new byte[1024];
		//
		for (int n; (n = in.read(buffer)) > 0;) {
			out.write(buffer, 0, n);
		}
		//
		try {
			return new String(out.toByteArray(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IOException(e.getMessage());
		}
	}
}