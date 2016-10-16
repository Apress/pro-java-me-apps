/*
 * KXML2Attributes.java
 * 16/11/2009
 * Twitter API Micro Edition
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package impl.javame.com.twitterapime.parser;

import java.util.Vector;

import org.kxml2.io.KXmlParser;

import com.twitterapime.parser.Attributes;

/**
 * <p>
 * This class defines a list of KXML2 attributes.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.1
 */
public final class KXML2Attributes implements Attributes {
	/**
	 * <p>
	 * List of attributes.
	 * </p>
	 */
	private Vector attributes = new Vector(5);
	
	/**
	 * <p>
	 * Create an instance of KXML2Attributes class.
	 * </p>
	 */
	public KXML2Attributes() {
	}
	
	/**
	 * <p>
	 * Create an instance of KXML2Attributes class.
	 * </p>
	 * @param parser Kxml parser object.
	 */
	public KXML2Attributes(KXmlParser parser) {
		loadAttributes(parser);
	}

	/**
	 * @see com.twitterapime.parser.Attributes#getIndex(java.lang.String)
	 */
	public int getIndex(String qName) {
		for (int i = attributes.size() -1; i >= 0; i--) {
			if (((Attribute)attributes.elementAt(i)).qName.equals(qName)) {
				return i;
			}
		}
		//
		return -1;
	}

	/**
	 * @see com.twitterapime.parser.Attributes#getLength()
	 */
	public int getLength() {
		return attributes.size();
	}

	/**
	 * @see com.twitterapime.parser.Attributes#getQName(int)
	 */
	public String getQName(int index) {
		return ((Attribute)attributes.elementAt(index)).qName;
	}

	/**
	 * @see com.twitterapime.parser.Attributes#getType(int)
	 */
	public String getType(int index) {
		return ((Attribute)attributes.elementAt(index)).type;
	}

	/**
	 * @see com.twitterapime.parser.Attributes#getValue(java.lang.String)
	 */
	public String getValue(String qName) {
		final int i = getIndex(qName);
		//
		return i != -1 ? ((Attribute)attributes.elementAt(i)).value : null;
	}

	/**
	 * @see com.twitterapime.parser.Attributes#getValue(int)
	 */
	public String getValue(int index) {
		return ((Attribute)attributes.elementAt(index)).value;
	}
	
	/**
	 * <p>
	 * Load all the attributes from the tag being read.
	 * </p>
	 * @param parser Kxml parser object.
	 */
	public void loadAttributes(KXmlParser parser) {
		attributes.removeAllElements();
		Attribute attr;
		//
		for (int i = parser.getAttributeCount() -1; i >= 0; i--) {
			attr = new Attribute();
			attr.qName = parser.getAttributeName(i).toLowerCase();
			attr.value = parser.getAttributeValue(i);
			attr.type = parser.getAttributeType(i);
			//
			attributes.addElement(attr);
		}
	}
	
	/**
	 * <p>
	 * This class defines an attribute.
	 * </p>
	 * 
	 * @author Ernandes Mourao Junior (ernandes@gmail.com)
	 * @version 1.0
	 * @since 1.1
	 */
	private static class Attribute {
		/**
		 * <p>
		 * Qualified name.
		 * </p>
		 */
		public String qName;
		
		/**
		 * <p>
		 * Value.
		 * </p>
		 */
		public String value;
		
		/**
		 * <p>
		 * Type.
		 * </p>
		 */
		public String type;
	}
}