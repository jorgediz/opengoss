/*
 * Copyright 2005-2006 the original authors and www.opengoss.org community.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opengoss.core;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * UID is the abbreviation of Universal Identifier that comply with URN rules.
 * <p>
 * UID Format: "uid:${feature}:${plugin}:${service};${prop}=${val}&${prop}=${val}"
 * </p>
 * <p>
 * UID to URN: "urn:${feature}:${plugin}:${service};${prop}=${val}&${prop}=${val}"
 * </p>
 * <p>
 * UID to URL: "/${feature}/${plugin}/${service}?${prop}=${val}&${prop}=${val}"
 * </p>
 * 
 * @author Ery Lee(ery.lee@gmail.com)
 * @version 1.0 2006-11-18
 */
@SuppressWarnings("serial")
public final class UID implements Serializable {
	
	/**
	 * UID Schema.
	 */
	public static final String SCHEMA = "uid";

	/**
	 * Seperator between name and property.
	 */
	static final String SEPERATOR = ";";
	
	/**
	 * Seperator between name parts.
	 */
	static final String NAME_SEPERATOR = ":";
	
	/**
	 * Seperator between properties.
	 */
	static final String PROP_SEPERATOR = "&";
	
	/**
	 * Name parts.
	 */
	private String[] names = new String[0];
	
	/**
	 * Property parts.
	 */
	private String[] properties = new String[0];
	
	public UID(String uid) throws MalformedUIDException {
		parse(uid);
	}

	private void parse(String uid) throws MalformedUIDException {
		//name and property seperator
		int index = uid.indexOf(SEPERATOR);
		boolean hasProperty = true;
		if(index == -1) {
			hasProperty = false;
			index = uid.length();
		}
		//: name seperator
		String namePart = uid.substring(0, index);
		String[] names = namePart.split(NAME_SEPERATOR);
		if(names.length < 1) {
			throw new MalformedUIDException("No name part error!");
		}
		this.names = names;

		if(hasProperty) {
			//; property seperator
			String propPart = uid.substring(index + 1);
			String[] props = propPart.split(PROP_SEPERATOR);
			this.properties = new String[props.length * 2];
			for (int i = 0; i < props.length; i++) {
				String[] keyValue = props[i].split("=");
				if(keyValue.length != 2) {
					throw new MalformedUIDException("Invalid Property!");
				}
				properties[2 * i] = keyValue[0];
				properties[2 * i + 1] = keyValue[1];
			}
		}
	}
	
	private UID(String[] names, String[] properties) {
		this.names = new String[names.length];
		System.arraycopy(names, 0, this.names, 0, names.length);
		this.properties = new String[properties.length];
		System.arraycopy(properties, 0, this.properties, 0, properties.length);
	}

	public UID append(UID uid) {
		String[] names = new String[this.names.length + uid.names.length];
		System.arraycopy(this.names, 0, names, 0, this.names.length);
		System.arraycopy(uid.names, 0, names, this.names.length, uid.names.length);

		String[] properties = new String[this.properties.length + uid.properties.length];
		System.arraycopy(this.properties, 0, properties, 0, this.properties.length);
		System.arraycopy(uid.properties, 0, properties, this.properties.length, uid.properties.length);
		
		return new UID(names, properties);
	}

	public String getNameString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < names.length; i++) {
			sb.append(names[i]).append(NAME_SEPERATOR);
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	public String[] getNames() {
		String[] temp = new String[names.length];
		System.arraycopy(names, 0, temp, 0, names.length);
		return temp;
	}
	
	public Map<String, String> getProperties() {
		Map<String, String> propertyMap = new HashMap<String, String>(properties.length / 2);
		for (int i = 0; i < properties.length / 2; i++) {
			propertyMap.put(properties[2 * i], properties[2 * i + 1]);
		}
		return propertyMap;
	}

	public String toFilter() {
		return "(" + UID.SCHEMA + "=" + getNameString() + ")";
	}
	
	public Dictionary<String, String> toDictionary() {
		Dictionary<String, String> dictionary = new Hashtable<String, String>();
		dictionary.put(UID.SCHEMA, getNameString());
		return dictionary;
	}
	
	public String toURN() {
		return "urn:" + toString();
	}
	
	public String toURI() {
		String[] names = getNames();
		StringBuilder sb = new StringBuilder();
		for (String name : names) {
			sb.append("/").append(name);
		}
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true;
		if( !(obj instanceof UID) )	{
			return false;
		}
		UID uid = (UID)obj;
		return Arrays.equals(names, uid.names);
	}

	@Override
	public int hashCode() {
		int hashCode = 0;
		for (String name : names) {
			hashCode = 37 * hashCode + name.hashCode();
		}
		return hashCode;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(SCHEMA);
		for (String name : names) {
			sb.append(":").append(name);
		}
		if(properties.length > 0) {
			sb.append(";");
		}
		boolean isFirst = true;
		for (int i = 0; i < properties.length / 2; i++) {
			if(isFirst) {
				isFirst = false;
			} else {
				sb.append("&");
			}
			sb.append(properties[2 * i]).append("=").append(properties[2 * i + 1]);
		}
		return sb.toString();
	}
	
	@SuppressWarnings("serial")
	public class MalformedUIDException extends RuntimeException {

		public MalformedUIDException(String msg) {
			super(msg);
		}

	}

	public String getId() {
		return getNameString();
	}
}
