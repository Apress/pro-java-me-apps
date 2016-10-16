/*
 * HttpConnectionImpl.java
 * 16/08/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package impl.javame.com.twitterapime.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;

import com.twitterapime.io.HttpConnection;

/**
 * <p>
 * This class defines the implementation of HttpConnection for Java ME platform.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public final class HttpConnectionImpl implements HttpConnection {
	/**
	 * <p>
	 * Http connection object.
	 * </p>
	 */
	private javax.microedition.io.HttpConnection httpConn;
	
	/**
	 * @see com.twitterapime.io.HttpConnection#open(java.lang.String)
	 */
	public void open(String url) throws IOException {
		httpConn = (javax.microedition.io.HttpConnection)Connector.open(url);
	}

	/**
	 * @see com.twitterapime.io.HttpConnection#close()
	 */
	public void close() throws IOException {
		httpConn.close();
	}

	/**
	 * @see com.twitterapime.io.HttpConnection#getResponseCode()
	 */
	public int getResponseCode() throws IOException {
		return httpConn.getResponseCode();
	}

	/**
	 * @see com.twitterapime.io.HttpConnection#openInputStream()
	 */
	public InputStream openInputStream() throws IOException {
		return httpConn.openInputStream();
	}

	/**
	 * @see com.twitterapime.io.HttpConnection#openOutputStream()
	 */
	public OutputStream openOutputStream() throws IOException {
		return httpConn.openOutputStream();
	}

	/**
	 * @see com.twitterapime.io.HttpConnection#setRequestMethod(java.lang.String)
	 */
	public void setRequestMethod(String method) throws IOException {
		httpConn.setRequestMethod(method);
	}

	/**
	 * @see com.twitterapime.io.HttpConnection#setRequestProperty(java.lang.String, java.lang.String)
	 */
	public void setRequestProperty(String key, String value) throws IOException{
		httpConn.setRequestProperty(key, value);
	}
	
	/**
	 * @see com.twitterapime.io.HttpConnection#getRequestProperty(java.lang.String)
	 */
	public String getRequestProperty(String key) throws IOException {
		return httpConn.getRequestProperty(key);
	}
	
	/**
	 * @see com.twitterapime.io.HttpConnection#getHeaderField(java.lang.String)
	 */
	public String getHeaderField(String name) throws IOException {
		return httpConn.getHeaderField(name);		
	}
}