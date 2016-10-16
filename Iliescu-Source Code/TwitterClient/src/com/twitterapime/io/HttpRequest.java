/*
 * HttpRequest.java
 * 05/06/2010
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import com.twitterapime.util.StringUtil;
import com.twitterapime.xauth.Token;
import com.twitterapime.xauth.XAuthSigner;

/**
 * <p>
 * This class implements a Http request.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.3
 */
public final class HttpRequest {
	/**
	 * <p>
	 * URL.
	 * </p>
	 */
	private String url;
	
	/**
	 * <p>
	 * Http method.
	 * </p>
	 */
	private String method;
	
	/**
	 * <p>
	 * Body params.
	 * </p>
	 */
	private Hashtable bodyParams;
	
	/**
	 * <p>
	 * Header params.
	 * </p>
	 */
	private Hashtable headers;
	
	/**
	 * <p>
	 * Http connection object.
	 * </p>
	 */
	private HttpConnection conn;
	
	/**
	 * <p>
	 * xAuthSigner object.
	 * </p>
	 */
	private XAuthSigner signer;
	
	/**
	 * <p>
	 * xAuth access token.
	 * </p>
	 */
	private Token token;

	/**
	 * <p>
	 * Create an instance of HttpRequest class.
	 * </p>
	 * @param url URL.
	 * @throws IllegalArgumentException If URL is empty or null.
	 */
	public HttpRequest(String url) {
		if (url == null || (url = url.trim()).length() == 0) {
			throw new IllegalArgumentException("URL must not be empty/null");
		}
		//
		this.url = url;
		method = HttpConnection.GET;
		bodyParams = new Hashtable();
		headers = new Hashtable();
	}

	/**
	 * <p>
	 * Send a given to the URL.
	 * </p>
	 * @return Http response object.
	 * @throws IOException If an I/O error occurs.
	 */
	public HttpResponse send() throws IOException {
		close();
		//
		String nurl = getSanitizedURL();
		if (getQueryString() != null) {
			nurl += '?' + queryString(getQueryStringParams());
		}
		//
		conn = HttpConnector.open(nurl);
		conn.setRequestMethod(method);
		//
		if (signer != null && token != null) {
			signer.sign(this, token); //sign request.
		}
		//
		setHeaderFields(conn);
		if (HttpConnection.POST.equals(method)) {
			setBodyParameters(conn);
		}
		//
		return new HttpResponse(conn);
	}
	
	/**
	 * <p>
	 * Close the request object connection.
	 * </p>
	 * @throws IOException If an I/O error occurs.
	 */
	public void close() throws IOException {
		if (conn != null) {
			conn.close();
			conn = null;
		}
	}

	/**
	 * <p>
	 * Set a header field's value.
	 * </p>
	 * @param key Field key.
	 * @param value Field value.
	 */
	public void setHeaderField(String key, String value) {
		this.headers.put(key, value);
	}

	/**
	 * <p>
	 * Set a body parameter value.
	 * </p>
	 * @param key Paramenter key.
	 * @param value Parameter value.
	 */
	public void setBodyParameter(String key, String value) {
		this.bodyParams.put(key, value);
	}

	/**
	 * <p>
	 * Get header fields.
	 * </p>
	 * @return Fields.
	 */
	public Hashtable getHeaderFields() {
		return headers;
	}
	
	/**
	 * <p>
	 * Set Http method.
	 * </p>
	 * @param method Method.
	 * @throws IllegalArgumentException If method is invalid.
	 * @see HttpConnection#GET
	 * @see HttpConnection#POST
	 */
	public void setMethod(String method) {
		if (HttpConnection.GET.equals(method)
				|| HttpConnection.POST.equals(method)) {
			this.method = method;
		} else {
			throw new IllegalArgumentException("Invalid Http method: "+method);
		}
	}

	/**
	 * <p>
	 * Get body parameters.
	 * </p>
	 * @return Parameters.
	 */
	public Hashtable getBodyParameters() {
		return bodyParams;
	}
	
	/**
	 * <p>
	 * Get URL's query string parameters.
	 * </p>
	 * @return Parameters.
	 */
	public Hashtable getQueryStringParams() {
		Hashtable params = new Hashtable();
		String query = getQueryString();
		//
		if (query != null) {
			String[] ps = StringUtil.split(query, '&');
			String[] pv;
			//
			for (int i = 0; i < ps.length; i++) {
				pv = StringUtil.split(ps[i], '=');
				params.put(pv[0], pv[1]);
			}
		}
		//
		return params;
	}

	/**
	 * <p>
	 * Get URL.
	 * </p>
	 * @return URL.
	 */
	public String getURL() {
		return url;
	}

	/**
	 * <p>
	 * Get Http method.
	 * </p>
	 * @return Method.
	 */
	public String getMethod() {
		return method; 
	}

	/**
	 * <p>
	 * Get sanitized URL.
	 * </p>
	 * @return URL.
	 */
	public String getSanitizedURL() {
		final int i = url.indexOf('?');
		//
		return i != -1 ? url.substring(0, i) : url;
	}

	/**
	 * <p>
	 * Get URL's query string.
	 * </p>
	 * @return Query string.
	 */
	public String getQueryString() {
		final int i = url.indexOf('?');
		//
		return i != -1 ? url.substring(i +1, url.length()) : null;
	}
	
	/**
	 * <p>
	 * Set xAuth signer and access token.
	 * </p>
	 * @param signer Signer.
	 * @param token Access token.
	 */
	public void setSigner(XAuthSigner signer, Token token) {
		this.signer = signer;
		this.token = token;
	}

	/**
	 * <p>
	 * Set header fields into connection.
	 * </p>
	 * @param conn Http connection.
	 * @throws IOException If an I/O error occurs.
	 */
	private void setHeaderFields(HttpConnection conn) throws IOException {
		String key;
		Enumeration keys = headers.keys();
		//
		while (keys.hasMoreElements()) {
			key = (String)keys.nextElement();
			conn.setRequestProperty(key, (String)headers.get(key));
		}
	}

	/**
	 * <p>
	 * Set body fields into connection.
	 * </p>
	 * @param conn Http connection.
	 * @throws IOException If an I/O error occurs.
	 */
	private void setBodyParameters(HttpConnection conn) throws IOException {
		conn.setRequestProperty(
			"Content-Type", "application/x-www-form-urlencoded");
		//
		if (bodyParams.size() > 0) {
			byte[] content = queryString(bodyParams).getBytes("UTF-8");
			//
			conn.setRequestProperty(
				"Content-Length", String.valueOf(content.length));
			//
			OutputStream out = conn.openOutputStream();
			out.write(content);
			out.flush();
			out.close();
		}
	}

	/**
	 * <p>
	 * Create query string from a set of parameters.
	 * </p>
	 * @param p Parameters.
	 * @return Query string.
	 */
	private String queryString(Hashtable p) {
		String key;
		StringBuffer queryStr = new StringBuffer();
		Enumeration keys = p.keys();
		//
		while (keys.hasMoreElements()) {
			key = (String)keys.nextElement();
			//
			queryStr.append(StringUtil.encode(key, "UTF-8"));
			queryStr.append('=');
			queryStr.append(StringUtil.encode((String)p.get(key), "UTF-8"));
			
			if (keys.hasMoreElements()) {
				queryStr.append('&');
			}
		}
		//
		return queryStr.toString();
	}
}