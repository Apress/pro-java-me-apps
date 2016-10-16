/*
 * HttpConnection.java
 * 16/08/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.twitterapime.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * <p>
 * This interface defines the necessary methods and constants for an HTTP
 * connection. 
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.1
 * @since 1.0
 * @see HttpConnector
 */
public interface HttpConnection {
	/**
	 * <p>
	 * HTTP Get method.
	 * </p> 
	 */
	public static final String GET = "GET";
	
	/**
	 * <p>
	 * HTTP Post method. 
	 * </p>
	 */
	public static final String POST = "POST";

	/**
	 * <p>
	 * HTTP Head method.
	 * </p>
	 */
	public static final String HEAD = "HEAD";

	/**
	 * <p>
	 * 200: The request has succeeded. 
	 * </p> 
	 */
	public static final int HTTP_OK = 200;
	
	/**
	 * <p>
	 * 403: The server understood the request, but is refusing to fulfill it.
	 * </p> 
	 */
	public static final int HTTP_FORBIDDEN = 403;

	/**
	 * <p>
	 * 503: The server is currently unable to handle the request due to a
	 * temporary overloading or maintenance of the server.
	 * </p> 
	 */
	public static final int HTTP_UNAVAILABLE = 503;
	
	/**
	 * <p>
	 * 304: If the client has performed a conditional GET request and access is
	 * allowed, but the document has not been modified, the server SHOULD
	 * respond with this status code.
	 * </p> 
	 */
	public static final int HTTP_NOT_MODIFIED = 304;

	/**
	 * <p>
	 * 400: The request could not be understood by the server due to malformed
	 * syntax. 
	 * </p> 
	 */
	public static final int HTTP_BAD_REQUEST = 400;
	
	/**
	 * <p>
	 * 401: The request requires user authentication. The response MUST include
	 * a WWW-Authenticate header field containing a challenge applicable to the
	 * requested resource. 
	 * </p>
	 */
	public static final int HTTP_UNAUTHORIZED = 401;

	/**
	 * <p>
	 * 404: The server has not found anything matching the Request-URI.
	 * </p> 
	 */
	public static final int HTTP_NOT_FOUND = 404;
	
	/**
	 * <p>
	 * 406: The resource identified by the request is only capable of generating
	 * response entities which have content characteristics not acceptable
	 * according to the accept headers sent in the request. 
	 * </p> 
	 */
	public static final int HTTP_NOT_ACCEPTABLE = 406;

	/**
	 * <p>
	 * 500: The server encountered an unexpected condition which prevented it
	 * from fulfilling the request. 
	 * </p> 
	 */
	public static final int HTTP_INTERNAL_ERROR = 500;

	/**
	 * <p>
	 * 502: The server, while acting as a gateway or proxy, received an invalid
	 * response from the upstream server it accessed in attempting to fulfill
	 * the request. 
	 * </p> 
	 */
	public static final int HTTP_BAD_GATEWAY  = 502;

	/**
	 * <p>
	 * Create and open a Http connection. 
	 * </p>
	 * @param url The URL for the connection.
	 * @throws IOException If an I/O error occurs.
	 */
	public void open(String url) throws IOException;
	
	/**
	 * <p>
	 * Close the connection. 
	 * </p> 
	 * @throws IOException If an I/O error occurs.
	 */
	public void close() throws IOException;

	/**
	 * <p>
	 * Returns the HTTP response status code, e.g., 200 (HTTP_OK).
	 * </p> 
	 * @return the HTTP Status-Code or -1 if no status code can be discerned. 
	 * @throws IOException If an I/O error occurs.
	 */
	public int getResponseCode() throws IOException;

	/**
	 * <p>
	 * Open and return an input stream for a connection.
	 * </p> 
	 * @return An input stream.
	 * @throws IOException If an I/O error occurs.
	 */
	public InputStream openInputStream() throws IOException;

	/**
	 * <p>
	 * Open and return an output stream for a connection.
	 * </p> 
	 * @return An output stream.
	 * @throws IOException If an I/O error occurs.
	 */
	public OutputStream openOutputStream() throws IOException;

	/**
	 * <p>
	 * Set the method for the URL request, one of: GET, POST or HEAD.
	 * </p> 
	 * @param method The HTTP method.
	 * @throws IOException If an I/O error occurs.
	 */
	public void setRequestMethod(String method) throws IOException;

	/**
	 * <p>
	 * Sets the general request property. If a property with the key already
	 * exists, overwrite its value with the new value. 
	 * </p> 
	 * @param key The keyword by which the request is known (e.g., "accept").
	 * @param value the value associated with it.
	 * @throws IOException If an I/O error occurs.
	 */
	public void setRequestProperty(String key, String value) throws IOException;
	
	/**
	 * <p>
	 * Returns the value of the named general request property for this
	 * connection. 
	 * </p>
	 * @param key The keyword by which the request property is known
	 *            (e.g., "accept").
	 * @return the value of the named general request property for this
	 *         connection. If there is no key with the specified name then null
	 *         is returned.
	 * @throws IOException If an I/O error occurs.
	 */
	public String getRequestProperty(String key) throws IOException;

	/**
	 * <p>
	 * Returns the value of the named header field. 
	 * </p> 
	 * @param name Name of a header field.
	 * @return the value of the named header field, or null  if there is no such
	 *         field in the header.
	 * @throws IOException If an I/O error occurs.
	 */
	public String getHeaderField(String name) throws IOException;
}